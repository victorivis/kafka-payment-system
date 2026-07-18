package br.edu.ifpb.producer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Pagamentos",
                version = "0.1.0",
                description = "API REST para cadastro de pedidos, geração de pagamento via Stripe, "
                        + "consulta de status e histórico de transações. O processamento do pagamento "
                        + "acontece de forma assíncrona: esta API publica um evento no Kafka e um "
                        + "serviço separado (consumer) processa a cobrança junto ao gateway."
        )
)
public class OpenApiConfig {
}
