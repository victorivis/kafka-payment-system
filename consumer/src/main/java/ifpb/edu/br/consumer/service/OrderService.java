package ifpb.edu.br.consumer.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import ifpb.edu.br.consumer.entity.PaymentEntity;
import ifpb.edu.br.consumer.entity.PaymentStatus;
import ifpb.edu.br.consumer.record.PaymentReceivedEvent;
import ifpb.edu.br.consumer.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    PaymentRepository paymentRepository;
    PaymentGatewayService paymentGatewayService;

    @KafkaListener(topics = "payment-topic", containerFactory = "orderKafkaListenerContainerFactory")
    public void orderListener(PaymentReceivedEvent event) {
        System.out.println("Received Message Consumer: " + event);

        Optional<PaymentEntity> optPayment = paymentRepository.findById(event.id());

        if (optPayment.isEmpty()) {
            System.out.println("[Error]: Payment not found for message id: " + event.id());
            return;
        }

        PaymentEntity payment = optPayment.get();
        payment.setStatus(PaymentStatus.APROVADO);

        try {
            PaymentIntent intent = paymentGatewayService.charge(payment);
            payment.setStripePaymentIntentId(intent.getId());
            payment.setStatus("succeeded".equals(intent.getStatus())
                    ? PaymentStatus.APROVADO
                    : PaymentStatus.RECUSADO);
        } catch (StripeException e) {
            System.out.println("[Error] Falha ao processar pagamento na Stripe: " + e.getMessage());
            payment.setStatus(PaymentStatus.RECUSADO);
        }

        paymentRepository.save(payment);
        System.out.println("Payment updated: " + event);
    }
}
