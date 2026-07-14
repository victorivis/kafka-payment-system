# Sistema Distribuído de Processamento de Pagamentos

## Descrição

uma API REST de pagamentos integrada a um gateway externo, e um sistema distribuído baseado em mensageria assíncrona com Apache Kafka.

A comunicação por mensageria orientada a eventos permite desacoplar componentes de uma aplicação corporativa e escalar/processar cada etapa separadamente.

O sistema é dividido em dois serviços que se comunicam via Kafka:

- **producer**: expõe uma API REST para criar pagamentos. Ao receber uma requisição, salva o registro no banco (status `PENDENTE`) e publica um evento no Kafka.
- **consumer**: escuta o evento publicado, processa o pagamento e atualiza o status no banco (`APROVADO`, `RECUSADO` ou `CANCELADO`).

## Pré-requisitos

- Java 21+
- Docker e Docker Compose
- Uma ferramenta para chamadas HTTP (Postman, Insomnia, curl)

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

Em dois terminais separados, um dentro de cada pasta de módulo:

```bash
# terminal 1
cd producer
./mvnw spring-boot:run

# terminal 2
cd consumer
./mvnw spring-boot:run
```

- producer sobe na porta `8083`
- consumer sobe na porta `8085`

## 3. Endpoints

Criar um pagamento:

```
POST http://localhost:8083/payment
Content-Type: application/json
```

```json
{
    "name": "nome-evento",
    "value": 42
}
```

## 4. Debugar o banco rapidamente

Para ver o estado atual da tabela de pagamentos sem precisar de um cliente Postgres instalado:

```bash
docker exec -it postgres psql -U payments -d payments_db -c "SELECT * FROM payments;"
```