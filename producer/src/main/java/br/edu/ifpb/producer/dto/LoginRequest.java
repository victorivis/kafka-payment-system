package br.edu.ifpb.producer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Schema(description = "Email, usado como login", example = "dylan@email.com")
    @NotNull
    @Email
    private String email;

    @Schema(description = "Senha em texto puro", example = "interestelar123")
    @NotBlank
    private String password;
}
