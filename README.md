<div align="center">

# 💬 Qualé – Chat APP

![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)
![Flutter](https://img.shields.io/badge/Flutter-3.7.0-blue)

**"Qualé"** o aplicativo de chat que é pura brasilidade! 😎

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Arquitetura](#-arquitetura)
- [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Documentação da API](#-documentação-da-api)
- [Autores](#-autores)
- [Licença](#-licença)

---

## 📖 Sobre o Projeto

**Qualé** é um aplicativo de chat desenvolvido para conectar pessoas de maneira simples e divertida. Com uma interface amigável e recursos intuitivos, o Qualé permite que os usuários se comuniquem em tempo real, criando grupos, compartilhando mensagens e muito mais. (Semelhante ao WhatsApp, Telegram, etc.)

O **App Qualé** está sendo desenvolvido como projeto pessoal para aprimorar habilidades em desenvolvimento mobile e backend, utilizando tecnologias modernas para garantir uma experiência fluida e segura.

---

## ✨ Funcionalidades
- **Autenticação de Usuários:** Registro e login seguro com validação de credenciais.
- **Lista de Contatos:** Adição e gerenciamento de contatos para facilitar a comunicação.
- **Mensagens em Tempo Real:** Envio e recebimento instantâneo de mensagens de texto
- **Grupos de Chat:** Criação e participação em grupos para conversas em grupo.
- **Notificações Push:** Alertas em tempo real para novas mensagens e atividades.
- **Perfil de Usuário:** Personalização do perfil com foto e status.


---

## 🏗️ Arquitetura

O projeto segue uma arquitetura de **microserviços** com frontend e backend separados e comunicação via API RESTful utilizando JSON.

A aplicação utiliza persistência poliglota com banco de dados relacional (PostgresSQL) para dados de usuários e autenticação, e banco NoSQL (MongoDB) para armazenamento de mensagens e histórico de chats.

```
Frontend (Flutter) <--> API REST (Spring Boot) <--> Banco de Dados (PostgresSQL + MongoDB)
```

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 4.0** - Framework principal
    - Spring Web (REST APIs)
    - Spring Data JPA (Persistência)
    - Spring Security (Autenticação/Autorização)
    - Spring Validation (Validações)
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **PostgresSQL** - Banco de dados relacional
- **MongoDB** - Banco de dados NoSQL
- **Hibernate** - ORM para JPA
- **Lombok** - Redução de código boilerplate
- **SpringDoc OpenAPI** - Documentação automática da API
- **Mapstruct** - Mapeamento entre DTOs e entidades
- **JUnit & Mockito** - Testes unitários e de integração
- **Maven** - Gerenciamento de dependências

### Frontend
- **Flutter 3.7.0** - Framework de desenvolvimento mobile
- **Dart** - Linguagem de programação

### DevOps
- **Docker & Docker Compose** - Containerização
- **Git** - Controle de versão

---

## 📦 Pré-requisitos

### Opção 1: Execução com Docker (Recomendado)
- [Docker Desktop](https://www.docker.com/products/docker-desktop) instalado e em execução
- 4GB de RAM disponível
- 5GB de espaço em disco

### Opção 2: Execução Local
- **Java 21** ou superior - [Download JDK](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.9+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Flutter 3.7.0** - [Download Flutter](https://flutter.dev/docs/get-started/install)
- **PostgresSQL** - [Download Postgres](https://www.postgresql.org/download/)
- **MongoDB** - [Download MongoDB](https://www.mongodb.com/try/download/community)
- Git - [Download Git](https://git-scm.com/)

---

## 🚀 Instalação e Execução

### Opção 1: Com Docker (Recomendado)

#### 1. Clone o repositório
```bash
git clone https://github.com/Lu4head/Quale.git
cd Quale
```

#### 2. Inicie a aplicação

**Windows:**
```bash
quale-run.bat
```

#### 3. Acesse a aplicação
- **Backend:** http://localhost:8080
- **Documentação da API (Swagger):** http://localhost:8080/docs

#### 4. Para parar a aplicação
Pressione `Ctrl + C` no terminal ou:

**Windows:**
```bash
docker-compose down
```

### Opção 2: Execução Local (Desenvolvimento)

#### Backend

```bash
# Navegue até a pasta do backend
cd backend

# Compile e execute
mvn spring-boot:run

# Ou compile o JAR
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

#### Frontend

```bash
# Navegue até a pasta do frontend
cd frontend

```

---

## 📁 Estrutura do Projeto

```
Quale/
├── 📂 backend/                    # API REST em Spring Boot
│   ├── 📂 src/
│   │   ├── 📂 main/
│   │   │   ├── 📂 java/br/com/quale/
│   │   │   │   ├── 📂 config/       # Configurações (Security, CORS, etc)
│   │   │   │   ├── 📂 controller/   # Endpoints REST
│   │   │   │   ├── 📂 mappers/      # Mapstruct Mappers
│   │   │   │   ├── 📂 model/        # Entidades JPA
│   │   │   │   ├── 📂 repository/   # Repositórios JPA
│   │   │   │   ├── 📂 service/      # Lógica de negócio
│   │   │   │   ├── 📂 dto/          # Data Transfer Objects
│   │   │   │   ├── 📂 exception/    # Tratamento de exceções
│   │   │   │   └── 📂 validations/  # Validações customizadas
│   │   │   └── 📂 resources/
│   │   │       └── application.yml # Configurações da aplicação
│   │   └── 📂 test/               # Testes unitários e integração
│   ├── 📂 uploads/                # Arquivos enviados pelos usuários
│   ├── Dockerfile                 # Container Docker
│   └── pom.xml                    # Dependências Maven
│
├── 📂 frontend/                   # Aplicativo Flutter
│   
│
├── docker-compose.yml             # Orquestração dos containers
├── quale-run.bat                  # Script de execução (Windows)
└── README.md                      # Este arquivo
```

---

## 📚 Documentação da API

A documentação completa da API está disponível via **Swagger UI** quando a aplicação está rodando:

🔗 **http://localhost:8080/docs**



## 👥 Autores

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/Lu4head">
        <sub><b>Luan Emanuel R. Argentato</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Joviarruda">
        <sub><b>Joao Vitor B. Arruda</b></sub>
      </a>
    </td>
  </tr>
</table>

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT.

---

<div align="center">


[⬆ Voltar ao topo](#-qualé--chat-app)

</div>
