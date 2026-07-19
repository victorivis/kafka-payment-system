package br.edu.ifpb.producer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Dados públicos do usuário, sem senha")
public class UserResponse {

    @Schema(example = "Maria Silva")
    private String name;

    @Schema(example = "maria@email.com")
    private String email;

    @Schema(description = "Permissões para uso futuro limitando o acesso de usuários para certas rotas",
            example = "[\"ROLE_USER\"]")
    private List<String> permissions;
}
