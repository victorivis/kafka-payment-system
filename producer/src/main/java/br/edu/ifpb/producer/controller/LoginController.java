package br.edu.ifpb.producer.controller;

import br.edu.ifpb.producer.dto.LoginRequest;
import br.edu.ifpb.producer.entity.PermissionEntity;
import br.edu.ifpb.producer.entity.UserEntity;
import br.edu.ifpb.producer.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ifpb.producer.dto.LoginResponse;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Operation(
            summary = "Autenticar usuário",
            description = "Valida email e senha e retorna um token JWT (válido por 10 minutos) "
                    + "a ser enviado no header Authorization como 'Bearer {token}' nas demais rotas."
    )
    @ApiResponse(responseCode = "200", description = "Login realizado, token emitido")
    @ApiResponse(responseCode = "401", description = "Email ou senha inválidos")
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<UserEntity> optUser = userService.findByEmail(loginRequest.getEmail());

        if (optUser.isEmpty() || !verifyLogin(loginRequest.getPassword(), optUser.get().getPassword())) {
            throw new BadCredentialsException("Wrong email or password!");
        }

        UserEntity user = optUser.get();
        List<String> permissoes = user.getPermissions().stream()
                .map(PermissionEntity::getName)
                .toList();
        long expiresIn = 600L;

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("seguranca-api")
                .subject(user.getName())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .issuedAt(Instant.now())
                .claim("email", user.getEmail())
                .claim("scope", permissoes)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(token, expiresIn));
    }

    private boolean verifyLogin(String password, String savedPassowrd) {
        return passwordEncoder.matches(password, savedPassowrd);
    }
}
