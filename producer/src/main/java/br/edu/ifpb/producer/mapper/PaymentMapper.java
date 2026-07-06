package br.edu.ifpb.producer.mapper;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.entity.PaymentEntity;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(PaymentRequest request) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setId(UUID.randomUUID());
        paymentEntity.setName(request.name());
        paymentEntity.setValue(request.value());
        return paymentEntity;
    }
}