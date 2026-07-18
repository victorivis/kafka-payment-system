package br.edu.ifpb.producer.service;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.dto.PaymentResponse;
import br.edu.ifpb.producer.entity.PaymentEntity;
import br.edu.ifpb.producer.entity.PaymentStatus;
import br.edu.ifpb.producer.event.PaymentCreatedEvent;
import br.edu.ifpb.producer.mapper.PaymentMapper;

import br.edu.ifpb.producer.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, PaymentCreatedEvent> paymentKafkaTemplate;
    private final PaymentRepository paymentRepository;

    @SuppressWarnings("null")
    public PaymentResponse sendMessageOrder(PaymentRequest order) {
        PaymentEntity payment = PaymentMapper.toEntity(order);
        payment = paymentRepository.save(payment);

        String key = payment.getId().toString();
        System.out.println("Sending Order: " + payment);

        PaymentCreatedEvent event = PaymentMapper.toEvent(payment);
        paymentKafkaTemplate.send("payment-topic", key, event);
        return PaymentMapper.toResponse(payment);
    }

    public Optional<PaymentEntity> cancel(UUID id) {
        Optional<PaymentEntity> optPayment = paymentRepository.findById(id);

        if (optPayment.isEmpty()) {
            return Optional.empty();
        }

        PaymentEntity payment = optPayment.get();

        if (payment.getStatus() != PaymentStatus.PENDENTE) {
            return Optional.empty();
        }

        payment.setStatus(PaymentStatus.CANCELADO);
        return Optional.of(paymentRepository.save(payment));
    }

    public Optional<PaymentEntity> findById(UUID id) {
        return paymentRepository.findById(id);
    }

    public List<PaymentEntity> findAll() {
        return paymentRepository.findAll();
    }
}
