package br.edu.ifpb.producer.service;

import br.edu.ifpb.producer.record.PaymentRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



import java.util.Random;

@Service
public class PaymentService {

    private final KafkaTemplate<String, PaymentRecord> paymentKafkaTemplate;

    public PaymentService(KafkaTemplate<String, PaymentRecord> paymentKafkaTemplate) {
        this.paymentKafkaTemplate = paymentKafkaTemplate;
    }

    @SuppressWarnings("null")
    public void sendMessageOrder(PaymentRecord order) {
        int partition = 0;
        String key = null;
        System.out.println("Sent message to partition: " + partition);
        System.out.println("Sending Order: " + order.name());
        paymentKafkaTemplate.send("payment-topic", partition, key, order);
    }

}
