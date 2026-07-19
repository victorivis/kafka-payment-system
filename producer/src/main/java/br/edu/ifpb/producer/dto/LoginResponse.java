package br.edu.ifpb.producer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT emitido após login bem-sucedido")
public record LoginResponse(
        @Schema(description = "Token a ser enviado no header Authorization como 'Bearer {token}'") String accessToken,
        @Schema(description = "Tempo de expiração em segundos", example = "600") Long expiresIn
) {
}