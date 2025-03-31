# FinancialApp

**FinancialApp** é uma aplicação Spring Boot para gerenciamento financeiro básico, permitindo que usuários cadastrem suas informações, vinculem cartões de crédito e realizem autenticação segura com JWT. O sistema oferece funcionalidades como recuperação de senha, bloqueio temporário de contas após tentativas falhas e logout seguro, com foco em segurança de dados sensíveis.

## Funcionalidades

- **Gerenciamento de Usuários:**
  - Cadastro, atualização e consulta de usuários.
  - Preferências de notificação (`email`, `sms`, `push`).
- **Vinculação de Cartões de Crédito:**
  - Adição e consulta de cartões por usuário (máximo de 5 por usuário).
  - Criptografia AES-256 para números de cartão.
- **Autenticação e Segurança:**
  - Login com JWT (token de acesso e refresh token).
  - Logout com blacklist de tokens.
  - Bloqueio temporário de 15 minutos após 3 tentativas falhas de login.
  - Recuperação de senha via e-mail ou SMS (simulado).
- **Documentação:**
  - API documentada com Swagger (`/swagger-ui.html`).

## Tecnologias Utilizadas

- **Backend:** Java 17, Spring Boot 3.x
- **Segurança:** Spring Security, JWT (jjwt), BCrypt, AES-256
- **Banco de Dados:** H2 (em memória, para desenvolvimento)
- **Documentação:** OpenAPI 3 (Swagger)
- **Build:** Maven

## Pré-requisitos

- Java 17 ou superior
- Maven 3.8.x ou superior
- Postman (opcional, para testes manuais)

## Instalação

1. **Clone o Repositório:**
   ```bash
   git clone https://github.com/seu-usuario/financialapp.git
   cd financialapp
