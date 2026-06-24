# Muinda Kubika — API

API REST do Muinda Kubika, construída com Spring Boot 4 e Java 21.

## Tecnologias

- **Runtime:** Java 21, Spring Boot 4.0.6
- **Base de dados:** PostgreSQL 15 (Flyway migrations)
- **Autenticação:** JWT (Auth0 java-jwt)
- **Armazenamento:** Cloudinary
- **Mensageria:** RabbitMQ (Spring AMQP)
- **Documentação:** Swagger UI (Springdoc OpenAPI)
- **Build:** Maven

## Estrutura do projecto

```
com.api.muinda_kubika
├── Config/              # Configurações (Cloudinary, RabbitMQ)
├── Controller/          # Endpoints REST
│   ├── Auth/            # Autenticação
│   ├── Files/           # Documentos, ficheiros, IA
│   ├── Instituicao/     # Instituições
│   ├── Localizacao/     # Países, províncias, municípios, bairros
│   ├── RolesPermissions/# Roles e permissões
│   └── User/            # Utilizadores e perfis
├── DTO/                 # Data Transfer Objects
├── Enums/               # Enumeradores do domínio
├── Exceptions/          # Excepções personalizadas
├── model/               # Entidades JPA
├── Repository/          # Repositórios JPA
├── security/            # JWT e configuração de segurança
├── Service/             # Lógica de negócio
└── Specification/       # Specifications JPA
```

## Como executar

### Pré-requisitos

- Java 21
- PostgreSQL 15
- RabbitMQ

### Localmente

```bash
# Configurar variáveis de ambiente (ou usar defaults)
export JWT_SECRET=minha-chave-secreta
export SPRING_RABBITMQ_URI=amqp://localhost:5672

# Executar
./mvnw spring-boot:run
```

### Com Docker

```bash
# A partir da raiz do projecto
docker-compose up -d muinda-kubika-api
```

## Testes

```bash
./mvnw test
```

## Documentação da API

Com a aplicação em execução, aceder a:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Migrações da base de dados

As migrações Flyway estão em `src/main/resources/db/migration/`.
