package br.edu.ifpb.producer.controller;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.dto.PaymentResponse;
import br.edu.ifpb.producer.mapper.PaymentMapper;
import br.edu.ifpb.producer.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    public final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody PaymentRequest order) {
        PaymentResponse response = paymentService.sendMessageOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getStatus(@PathVariable UUID id) {
        return paymentService.findById(id)
                .map(PaymentMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PaymentResponse> getHistory() {
        return paymentService.findAll().stream()
                .map(PaymentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
