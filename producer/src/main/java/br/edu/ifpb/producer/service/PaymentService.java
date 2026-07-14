package br.edu.ifpb.producer.service;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.entity.PaymentEntity;
import br.edu.ifpb.producer.mapper.PaymentMapper;

import br.edu.ifpb.producer.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, PaymentEntity> paymentKafkaTemplate;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    @SuppressWarnings("null")
    public void sendMessageOrder(PaymentRequest order) {
        PaymentEntity payment = paymentMapper.toEntity(order);
        payment = paymentRepository.save(payment);

        String key = payment.getId().toString();
        System.out.println("Sending Order: " + payment);
        paymentKafkaTemplate.send("payment-topic", key, payment);
    }

}
