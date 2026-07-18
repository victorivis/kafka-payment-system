package br.edu.ifpb.producer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentRequest(

        @Schema(description = "Descrição do pedido", example = "Assinatura mensal")
        String name,

        @Schema(description = "Valor do pagamento, na menor unidade da moeda (centavos)", example = "5000")
        Integer value
) {
}
