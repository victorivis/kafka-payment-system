package br.edu.ifpb.producer.service;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.entity.PaymentEntity;
import br.edu.ifpb.producer.mapper.PaymentMapper;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final KafkaTemplate<String, PaymentEntity> paymentKafkaTemplate;
    private final PaymentMapper paymentMapper;

    public PaymentService(KafkaTemplate<String, PaymentEntity> paymentKafkaTemplate, PaymentMapper paymentMapper) {
        this.paymentKafkaTemplate = paymentKafkaTemplate;
        this.paymentMapper = paymentMapper;
    }

    @SuppressWarnings("null")
    public void sendMessageOrder(PaymentRequest order) {
        PaymentEntity payment = paymentMapper.toEntity(order);
        String key = payment.getId().toString();
        System.out.println("Sending Order: " + payment);
        paymentKafkaTemplate.send("payment-topic", key, payment);
    }

}
