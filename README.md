# 🚀 Tech Insight Dashboard

Backend REST desenvolvido em **Java e Spring Boot** para centralizar dados e insights relacionados ao mercado de tecnologia.

O projeto foi construído com foco em **desenvolvimento Backend**, integração com APIs externas, persistência de dados, autenticação, segurança, containerização e preparação de busca baseada em dados.

---

## 📌 Sobre o projeto

O **Tech Insight Dashboard** é uma aplicação Backend desenvolvida para trabalhar com informações relacionadas ao mercado de tecnologia.

A aplicação utiliza uma arquitetura baseada no ecossistema Spring e integra diferentes componentes para lidar com:

* APIs REST
* Persistência de dados
* PostgreSQL
* Autenticação e segurança
* JWT
* Integração com APIs externas
* OpenFeign
* Docker
* Docker Compose

A aplicação também possui integração configurável com:

**Adzuna API**: permitindo trabalhar com dados provenientes de plataformas de vagas.

**Github API**: permitindo o usuário accessar repositórios e tópicos sobre diversas tecnologias.

---

## 🎯 Objetivos do projeto

O projeto foi desenvolvido com os seguintes objetivos:

* Praticar desenvolvimento Backend com Java moderno;
* Trabalhar com Spring Boot em uma aplicação real;
* Desenvolver uma API REST estruturada;
* Implementar autenticação e segurança utilizando Spring Security e JWT;
* Trabalhar com persistência utilizando Spring Data JPA;
* Integrar serviços externos utilizando OpenFeign;
* Trabalhar com PostgreSQL;
* Containerizar a aplicação utilizando Docker;
* Utilizar Docker Compose para orquestrar aplicação e banco de dados;
* Desenvolver uma base preparada para análise e geração de insights sobre dados de tecnologia.

---

# 🛠️ Tecnologias utilizadas

## Backend

| Tecnologia        | Utilização                                            |
| ----------------- | ----------------------------------------------------- |
| Java 21           | Linguagem principal                                   |
| Spring Boot       | Framework Backend                                     |
| Spring Web MVC    | Desenvolvimento da API REST                           |
| Spring Data JPA   | Persistência de dados                                 |
| Hibernate         | ORM                                                   |
| Spring Security   | Segurança e autenticação                              |
| JWT               | Autenticação baseada em tokens                        |
| Spring Validation | Validação de dados                                    |
| OpenFeign         | Comunicação com APIs externas                         |
| Spring AI         | Recursos relacionados a IA                            |
| Lombok            | Redução de código boilerplate                         |

## Banco de dados

* PostgreSQL 16
* H2 para ambiente de execução/testes
* JPA / Hibernate

## Infraestrutura

* Docker
* Docker Compose
* Maven
* Multi-Stage Docker Build

## Integrações

* Adzuna API
* OpenFeign

---

# 🏗️ Arquitetura

O projeto utiliza o ecossistema Spring para separar responsabilidades entre diferentes camadas da aplicação.

Fluxo conceitual:

```text
                    ┌─────────────────────┐
                    │       Client        │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    REST API /       │
                    │    Controllers      │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Services        │
                    │ Business Logic      │
                    └──────────┬──────────┘
                               │
                ┌──────────────┼
                │              │                                          
                ▼              ▼              
          ┌──────────┐   ┌───────────┐  
          │   JPA    │   │ OpenFeign │  
          │Repository│   │ External  │  
          └────┬─────┘   │   APIs    │  
               │         └───────────┘
               ▼
        ┌──────────────┐
        │  PostgreSQL  │
        └──────────────┘
```

---

# 🔐 Segurança

A aplicação utiliza **Spring Security** para implementar a camada de segurança.

A autenticação utiliza **JWT (JSON Web Token)**, permitindo que clientes autenticados realizem requisições utilizando tokens.

Principais componentes utilizados:

* Spring Security
* JWT
* Auth0 Java JWT
* Secret configurável por variável de ambiente

O projeto utiliza a biblioteca `java-jwt` para trabalhar com tokens JWT.

### Fluxo conceitual

```text
Cliente
   │
   │ Login
   ▼
API
   │
   │ Autenticação
   ▼
Spring Security
   │
   │ JWT
   ▼
Token
   │
   │ Authorization: Bearer <token>
   ▼
Endpoints protegidos
```

---

# 🗄️ Banco de dados

O projeto utiliza **PostgreSQL 16** como banco de dados principal.

A configuração é realizada através de variáveis de ambiente, permitindo separar credenciais e configurações do código-fonte.

O Docker Compose configura automaticamente o serviço PostgreSQL e utiliza um volume persistente para os dados.

```yaml
db:
  image: postgres:16-alpine
```

---

# 🔄 Integração com APIs externas

O projeto utiliza **Spring Cloud OpenFeign** para facilitar a comunicação com serviços externos.

Uma das integrações previstas na configuração do projeto é a **Adzuna API**, uma plataforma de dados de vagas de emprego.

<img width="1538" height="861" alt="image" src="https://github.com/user-attachments/assets/11d256e1-e657-41a6-bd37-9e18b6b82c8e" />


As credenciais são configuradas através das variáveis:

```env
ADZUNA_APP_ID=
ADZUNA_APP_KEY=
```
Dessa forma, as credenciais não precisam ser armazenadas diretamente no código-fonte.

--- 

Outra integração no projeto é a API do **Github**, permitindo o usuário tanto pesquisar os melhores repositórios de uma determinada tecnologia/tema.

<img width="1537" height="838" alt="Captura de tela de 2026-10-07 21-35-18" src="https://github.com/user-attachments/assets/e2e1dbdf-1aff-4e0e-a62a-c4a7db71114b" />

Quanto pesquisar sobre tópicos de uma determinada linguagem

<img width="1537" height="838" alt="Captura de tela de 2026-10-07 21-32-20" src="https://github.com/user-attachments/assets/abd2d1f1-08b3-4b3e-8cf7-72f6d1b92c32" />


---


# 🐳 Docker

A aplicação possui um **Dockerfile Multi-Stage**.

A primeira etapa utiliza Maven + JDK para compilar a aplicação:

```dockerfile
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn clean package
```

Depois, uma segunda imagem é utilizada apenas para executar a aplicação:

```dockerfile
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

Essa abordagem separa o ambiente de **build** do ambiente de **runtime**, evitando carregar Maven e outras ferramentas de compilação para a imagem final.

---

# 🐳 Docker Compose

O projeto possui um `compose.yaml` responsável por executar a aplicação junto com o PostgreSQL.

```text
┌─────────────────────────────┐
│        Docker Compose       │
│                             │
│  ┌─────────────┐            │
│  │ PostgreSQL  │            │
│  │    16       │            │
│  └──────┬──────┘            │
│         │                   │
│         │ JDBC              │
│         ▼                   │
│  ┌─────────────┐            │
│  │ Spring Boot │            │
│  │     API     │            │
│  └─────────────┘            │
│                             │
└─────────────────────────────┘
```

O serviço da aplicação depende do PostgreSQL estar saudável antes de iniciar.

---

# ⚙️ Pré-requisitos

Antes de executar o projeto localmente, tenha instalado:

* Java 21
* Maven
* Docker
* Docker Compose

Para execução utilizando Docker, não é necessário instalar PostgreSQL separadamente.

---

# 🚀 Como executar

## 1. Clone o repositório

```bash
git clone https://github.com/GabrielBenford/Tech_Insight_Dashboard.git
```

Entre no diretório:

```bash
cd Tech_Insight_Dashboard
```

---

## 2. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto.

Exemplo:

```env
POSTGRES_DB=techinsight
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

JWT_SECRET=sua_chave_jwt

ADZUNA_APP_ID=seu_app_id
ADZUNA_APP_KEY=sua_app_key

APP_PORT=8080
```
---

# 🐳 Executando com Docker Compose

Com o `.env` configurado:

```bash
docker compose up --build
```

O Docker Compose irá:

1. Criar o container PostgreSQL;
2. Criar o banco `techinsight`;
3. Executar o healthcheck do PostgreSQL;
4. Construir a aplicação Spring Boot;
5. Iniciar a API;
6. Conectar a aplicação ao banco através da rede interna do Docker.

A API ficará disponível em:

```text
http://localhost:8080
```

O `compose.yaml` expõe a porta 8080 por padrão.

Para executar em background:

```bash
docker compose up -d --build
```

Para visualizar os logs:

```bash
docker compose logs -f
```

Para encerrar:

```bash
docker compose down
```

---

# ☕ Executando localmente

Também é possível executar a aplicação diretamente através do Maven.

Compile o projeto:

```bash
./mvnw clean package
```

Execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação será executada na porta:

```text
8080
```

---

# 🧪 Testes

O projeto possui dependências de teste integradas ao ecossistema Spring Boot, incluindo suporte para:

* Spring Boot Test
* Testes de JPA
* Testes de Security
* Testes Web MVC

Para executar os testes:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

---

# 🔧 Configuração

As principais configurações da aplicação podem ser controladas através de variáveis de ambiente.

| Variável         | Descrição                         |
| ---------------- | --------------------------------- |
| `POSTGRES_DB`    | Nome do banco PostgreSQL          |
| `DB_USERNAME`    | Usuário do PostgreSQL             |
| `DB_PASSWORD`    | Senha do PostgreSQL               |
| `JWT_SECRET`     | Chave utilizada para JWT          |
| `ADZUNA_APP_ID`  | Identificador da aplicação Adzuna |
| `ADZUNA_APP_KEY` | Chave da aplicação Adzuna         |
| `APP_PORT`       | Porta externa da aplicação        |

O `compose.yaml` utiliza essas variáveis para configurar tanto o banco quanto a aplicação.

---

# 📁 Estrutura do projeto

```text
Tech_Insight_Dashboard/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   └── test/
│
├── .dockerignore
├── .gitattributes
├── .gitignore
├── Dockerfile
├── compose.yaml
├── mvnw
├── mvnw.cmd
└── pom.xml
```
---

# 👨‍💻 Autor

Desenvolvido por Gabriel Benford

LinkedIn:
```bash
https://www.linkedin.com/in/gabriel-loureiro-benford/
```

GitHub:
```bash
https://github.com/GabrielBenford
```
