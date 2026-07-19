package br.edu.ifpb.producer.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
