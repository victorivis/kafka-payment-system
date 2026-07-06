package br.edu.ifpb.producer.controller;

import br.edu.ifpb.producer.dto.PaymentRequest;
import br.edu.ifpb.producer.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    
    public final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
   
    @PostMapping
    public void createOrder(@RequestBody PaymentRequest order) {
        paymentService.sendMessageOrder(order);
    }
}
