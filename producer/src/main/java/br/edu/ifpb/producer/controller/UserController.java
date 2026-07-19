package br.edu.ifpb.producer.controller;

import br.edu.ifpb.producer.dto.UserRequest;
import br.edu.ifpb.producer.dto.UserResponse;
import br.edu.ifpb.producer.service.UserService;
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
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse register(@Valid @RequestBody UserRequest request) {
        return userService.register(request);
    }

    @GetMapping("/me")
    public UserResponse search() {
        return userService.getCurrentUserResponse();
    }
}
