package br.edu.ifpb.producer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class UserRequest {

    @Schema(description = "Nome do usuário", example = "Dylan Thomas")
    @NotBlank
    private String name;

    @Schema(description = "Email, usado como login", example = "dylan@email.com")
    @NotNull @Email
    private String email;

    @Schema(description = "Senha em texto puro (armazenada com hash)", example = "interestelar123")
    @NotBlank
    private String password;
}
