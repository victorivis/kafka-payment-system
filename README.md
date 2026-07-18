# Sistema Distribuído de Processamento de Pagamentos

## Descrição

Uma API REST de pagamentos integrada a um gateway externo (Stripe), estruturada como um sistema distribuído baseado em mensageria assíncrona com Apache Kafka.

A comunicação por mensageria orientada a eventos permite desacoplar componentes de uma aplicação corporativa e escalar/processar cada etapa separadamente.

O sistema é dividido em dois serviços que se comunicam via Kafka:

- **producer**: expõe a API REST para criar, consultar, listar e cancelar pagamentos. Ao receber uma requisição de criação, salva o registro no banco (status `PENDENTE`) e publica um evento no Kafka.
- **consumer**: escuta o evento publicado, aguarda uma janela de segurança (para permitir cancelamento), cobra o pagamento junto ao Stripe e atualiza o status no banco (`APROVADO`, `RECUSADO` ou `CANCELADO`).

### Fluxo de status do pagamento

```
PENDENTE ──► APROVADO   (cobrança aceita pelo Stripe)
   │  └────► RECUSADO   (cobrança rejeitada pelo Stripe ou falha na chamada)
   └───────► CANCELADO  (cancelado pelo cliente antes do processamento)
```

Um pagamento só pode ser cancelado enquanto ainda estiver `PENDENTE`. Uma vez que o consumer inicia o processamento junto ao Stripe, o cancelamento não é mais aceito.

## Pré-requisitos

- Java 21+
- Docker e Docker Compose
- Uma ferramenta para chamadas HTTP (Postman, Insomnia, curl)
- Uma chave de API de teste do Stripe (`STRIPE_SECRET_KEY`), necessária para o consumer processar as cobranças

## 1. Subir a infraestrutura (Kafka, Postgres, AKHQ)

Na raiz do projeto:

```bash
docker compose up
```

Isso sobe três serviços. Portas usadas no host:

| Serviço | Porta | Uso |
|---|---|---|
| Kafka | `9092` | listener interno (entre containers) |
| Kafka | `9094` | listener externo (usado pelo producer/consumer rodando fora do Docker) |
| AKHQ | `8087` | interface web para inspecionar tópicos e mensagens do Kafka (`http://localhost:8087`) |
| Postgres | `5432` | banco de dados `payments_db` |

Certifique-se de que essas portas estejam livres na sua máquina antes de subir.

## 2. Rodar o producer e o consumer

O consumer precisa de uma chave de API do Stripe (modo teste) exportada como variável de ambiente antes de subir:

```bash
export STRIPE_SECRET_KEY="<chave__sk_test_xxx>"
```

Ou em um ambiente windows
```powershell
setx STRIPE_SECRET_KEY "<chave__sk_test_xxx>"
```

Em dois terminais separados, um dentro de cada pasta de módulo:

```powershell
# terminal 1
cd producer
./mvnw spring-boot:run

# terminal 2
cd consumer
./mvnw spring-boot:run
```

- producer sobe na porta `8083`
- consumer sobe na porta `8085` (não expõe endpoints REST; apenas consome do Kafka)

O consumer aguarda `9000ms` (configurável em `consumer.processing-delay-ms`, no `application.yaml`) antes de processar cada pagamento, para dar tempo de o cliente cancelar via API antes da cobrança ser efetivada no Stripe.

## 3. Endpoints - Documentação interativa (Swagger)

Com o producer rodando, a documentação das rotas OpenAPI/Swagger fica disponível em:

```
http://localhost:8083/swagger-ui/index.html
```

## 4. Debugar o banco rapidamente

Para ver o estado atual da tabela de pagamentos sem precisar de um cliente Postgres instalado:

```bash
docker exec -it postgres psql -U payments -d payments_db -c "SELECT * FROM payments;"
```