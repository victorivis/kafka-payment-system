package br.edu.ifpb.producer.service;

import br.edu.ifpb.producer.dto.UserRequest;
import br.edu.ifpb.producer.dto.UserResponse;
import br.edu.ifpb.producer.entity.PermissionEntity;
import br.edu.ifpb.producer.entity.UserEntity;
import br.edu.ifpb.producer.mapper.UserMapper;
import br.edu.ifpb.producer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getCredentials();
        return jwt.getClaim("email");
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity getCurrentUser() {
        return userRepository.findByEmail(getCurrentEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse register(UserRequest request) {
        UserEntity user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.addPermission(PermissionEntity.builder().name("ROLE_USER").build());

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    public UserResponse getCurrentUserResponse() {
        UserEntity userAutenticado = getCurrentUser();
        return UserMapper.toResponse(userAutenticado);
    }
}
