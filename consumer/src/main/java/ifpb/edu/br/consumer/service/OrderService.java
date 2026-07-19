package ifpb.edu.br.consumer.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import ifpb.edu.br.consumer.entity.PaymentEntity;
import ifpb.edu.br.consumer.entity.PaymentStatus;
import ifpb.edu.br.consumer.record.PaymentReceivedEvent;
import ifpb.edu.br.consumer.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayService paymentGatewayService;

    @Value("${consumer.processing-delay-ms:0}")
    private long processingDelayMs;

    @KafkaListener(topics = "payment-topic", containerFactory = "orderKafkaListenerContainerFactory")
    public void orderListener(PaymentReceivedEvent event) throws InterruptedException {
        log.info("Received event on consumer: {}", event);

        if (processingDelayMs > 0) {
            log.info("Waiting {} ms before processing (cancellation window)...", processingDelayMs);
            Thread.sleep(processingDelayMs);
        }

        Optional<PaymentEntity> optPayment = paymentRepository.findById(event.id());

        if (optPayment.isEmpty()) {
            log.error("Payment not found for message id: {}", event.id());
            return;
        }

        PaymentEntity payment = optPayment.get();

        if (payment.getStatus() != PaymentStatus.PENDENTE) {
            log.warn(
                    "Payment {} is no longer in PENDING status (current status: {}). Processing aborted.",
                    payment.getId(),
                    payment.getStatus()
            );
            return;
        }

        payment.setStatus(PaymentStatus.APROVADO);

        try {
            PaymentIntent intent = paymentGatewayService.charge(payment);
            payment.setStripePaymentIntentId(intent.getId());
            payment.setStatus("succeeded".equals(intent.getStatus())
                    ? PaymentStatus.APROVADO
                    : PaymentStatus.RECUSADO);
        } catch (StripeException e) {
            log.error("Failed to process payment with Stripe. {}", e.getMessage());
            payment.setStatus(PaymentStatus.RECUSADO);
        }

        paymentRepository.save(payment);
        log.info("Payment successfully updated: {}", payment);
    }
}
