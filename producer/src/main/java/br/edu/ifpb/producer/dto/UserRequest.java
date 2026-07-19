package br.edu.ifpb.producer.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class UserRequest {

    @NotBlank
    private String name;

    @NotNull @Email
    private String email;

    @NotBlank
    private String password;
}
