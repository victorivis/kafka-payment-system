package br.edu.ifpb.producer.mapper;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.entity.PaymentEntity;

import br.edu.ifpb.producer.entity.PaymentStatus;
import br.edu.ifpb.producer.event.PaymentCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMapper {

    public static PaymentEntity toEntity(PaymentRequest request) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setName(request.name());
        paymentEntity.setValue(request.value());
        paymentEntity.setStatus(PaymentStatus.PENDENTE);
        return paymentEntity;
    }

    public static PaymentCreatedEvent toEvent(PaymentEntity entity) {
        return new PaymentCreatedEvent(
                entity.getId(),
                entity.getName(),
                entity.getValue()
        );
    }
}