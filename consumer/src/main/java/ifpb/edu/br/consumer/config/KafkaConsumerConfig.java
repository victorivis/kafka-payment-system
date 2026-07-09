package ifpb.edu.br.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import ifpb.edu.br.consumer.record.PaymentReceivedEvent;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

        @Value(value = "${spring.kafka.bootstrap-servers}")
        private String bootstrapAddress;

        @Bean
        public ConsumerFactory<String, PaymentReceivedEvent> orderConsumerFactory() {
                Map<String, Object> props = new HashMap<>();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
                props.put(ConsumerConfig.GROUP_ID_CONFIG,"payment-processor-group");
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
                props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
                props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
                props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");
                props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, PaymentReceivedEvent.class.getName());
                return new DefaultKafkaConsumerFactory<>(props);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, PaymentReceivedEvent> orderKafkaListenerContainerFactory() {
                ConcurrentKafkaListenerContainerFactory<String, PaymentReceivedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(orderConsumerFactory());
                return factory;
        }
}
