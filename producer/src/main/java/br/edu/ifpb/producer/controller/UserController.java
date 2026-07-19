package br.edu.ifpb.producer.controller;

import br.edu.ifpb.producer.dto.UserRequest;
import br.edu.ifpb.producer.dto.UserResponse;
import br.edu.ifpb.producer.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "Usuários", description = "Cadastro de usuário e consulta do próprio perfil")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cria a conta de usuário, associando uma forma de pagamento (token de teste do Stripe)"
                    + " que será usada pelo consumer para processar os pagamentos deste usuário."
    )
    @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (campo obrigatório ausente, email mal formado, forma de pagamento inexistente)")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    @PostMapping
    public UserResponse register(@Valid @RequestBody UserRequest request) {
        return userService.register(request);
    }

    @GetMapping("/me")
    public UserResponse search() {
        return userService.getCurrentUserResponse();
    }
}
