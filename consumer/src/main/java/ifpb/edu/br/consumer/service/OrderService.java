package ifpb.edu.br.consumer.service;

import ifpb.edu.br.consumer.record.PaymentReceivedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @KafkaListener(topics = "payment-topic", containerFactory = "orderKafkaListenerContainerFactory")
    public void orderListener(PaymentReceivedEvent event) {
        System.out.println("Received Message Consumer: " + event);
    }
}
