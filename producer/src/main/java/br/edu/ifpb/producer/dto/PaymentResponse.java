package br.edu.ifpb.producer.dto;

import br.edu.ifpb.producer.entity.PaymentEntity;
import br.edu.ifpb.producer.entity.PaymentStatus;

import java.util.UUID;

public record PaymentResponse(UUID id, String name, Integer value, PaymentStatus status) {
}