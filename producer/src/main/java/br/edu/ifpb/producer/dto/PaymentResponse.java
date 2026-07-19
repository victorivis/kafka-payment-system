package br.edu.ifpb.producer.dto;

import br.edu.ifpb.producer.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record PaymentResponse(
        @Schema(description = "Id do pagamento") UUID id,
        @Schema(description = "Descrição do pedido", example = "Assinatura mensal") String name,
        @Schema(description = "Valor em centavos", example = "5000") Integer value,
        @Schema(description = "Status atual do pagamento") PaymentStatus status
) {
}