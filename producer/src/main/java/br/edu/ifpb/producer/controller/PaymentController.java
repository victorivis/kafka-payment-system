package br.edu.ifpb.producer.controller;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.dto.PaymentResponse;
import br.edu.ifpb.producer.mapper.PaymentMapper;
import br.edu.ifpb.producer.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Cadastrar um pedido/pagamento",
            description = "Cria o registro com status PENDENTE e publica um evento no Kafka. "
                    + "O processamento junto ao gateway acontece de forma assíncrona pelo serviço consumer."
    )
    @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso")
    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody PaymentRequest order) {
        PaymentResponse response = paymentService.sendMessageOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Consultar o status de um pagamento pelo id")
    @ApiResponse(responseCode = "200", description = "Pagamento encontrado")
    @ApiResponse(responseCode = "403", description = "Pagamento não existe ou não pertence ao usuário autenticado")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getStatus(@PathVariable UUID id) {
        return paymentService.findById(id)
                .map(PaymentMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar o histórico de pagamentos realizados")
    @GetMapping
    public List<PaymentResponse> getHistory() {
        return paymentService.findCurrent().stream()
                .map(PaymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Cancelar um pagamento",
            description = "Só é permitido enquanto o pagamento ainda está PENDENTE, antes do consumer "
                    + "processá-lo junto ao gateway. Existe um delay para permitir que o cancelamento "
                    + "aconteça antes do processamento."
    )
    @ApiResponse(responseCode = "200", description = "Pagamento cancelado com sucesso")
    @ApiResponse(responseCode = "403", description = "Pagamento não existe ou não pertence ao usuário autenticado")
    @ApiResponse(responseCode = "409", description = "Pagamento já não está mais PENDENTE")
    @PatchMapping("/{id}")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable UUID id) {
        return paymentService.cancel(id)
                .map(PaymentMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
}
