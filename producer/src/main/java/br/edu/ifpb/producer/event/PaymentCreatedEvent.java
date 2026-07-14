package br.edu.ifpb.producer.event;

import java.util.UUID;

public record PaymentCreatedEvent(
        UUID id,
        String name,
        Integer value
) {}