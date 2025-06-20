# 📦 E-commerce Backend (Spring Boot & PostgreSQL)

Este projeto é o coração da aplicação de e-commerce, uma **API RESTful robusta e segura** desenvolvida com Java e o ecossistema Spring. Ele foi arquitetado para suportar todas as operações de negócio de um e-commerce moderno, desde o gerenciamento de produtos até o processamento de pedidos e controle de acesso, com foco em segurança, escalabilidade e manutenibilidade.

## ✨ Funcionalidades Principais

-   **Gerenciamento de Produtos:**
    -   `CRUD` (Criar, Ler, Atualizar, Deletar) completo para itens de produtos.
    -   Endpoints públicos para listagem e visualização de produtos.
    -   `Ações restritas`: Operações de Criação, Atualização e Exclusão de produtos são rigidamente controladas e acessíveis apenas por usuários com perfil de `Administrador` (`ROLE_ADMIN`).
-   **Sistema de Autenticação e Autorização:**
    -   `Registro de Novos Usuários`: Permite a criação de contas de clientes (`ROLE_USER`) e administradores (`ROLE_ADMIN`).
    -   `Login Seguro`: Autenticação de usuários via `HTTP Basic Auth` (para demonstração de portfólio).
    -   `Criptografia de Senhas`: Armazenamento seguro de senhas utilizando o algoritmo `BCrypt` através do `Spring Security Crypto`.
    -   `Controle de Acesso Baseado em Papéis (RBAC)`: Implementação de autorização granular (`Spring Security`) para proteger endpoints, garantindo que apenas usuários com roles específicas (`ROLE_USER`, `ROLE_ADMIN`) possam acessá-los.
-   **Carrinho de Compras Dinâmico:**
    -   `Adição e Remoção de Itens`: Funcionalidade para adicionar produtos e gerenciar suas quantidades no carrinho de compras de um usuário logado.
    -   `Visualização do Carrinho`: Endpoint para consultar o conteúdo atual do carrinho, incluindo subtotais e valor total.
    -   `Gestão de Quantidade e Estoque`: Validações em tempo real para garantir a disponibilidade do produto antes de adicionar ao carrinho.
-   **Processamento de Pedidos:**
    -   `Checkout Seguro`: Conversão do conteúdo do carrinho em um pedido formal, com a criação de itens de pedido (garantindo a imutabilidade do preço no momento da compra).
    -   `Controle de Estoque`: `Decremento automático` do estoque do produto após a finalização bem-sucedida de um pedido.
    -   `Histórico de Pedidos`: Listagem de todos os pedidos realizados por um usuário autenticado.
-   **Robustez e Qualidade de Código:**
    -   `Validação de Entrada (Bean Validation)`: Utilização de anotações (`@Valid`, `@NotNull`, `@Size`, `@Min`) nos `DTOs` para validar os dados recebidos via requisições HTTP, melhorando a integridade dos dados e a segurança da API.
    -   `Tratamento Global de Exceções`: Implementação de um manipulador centralizado (`@ControllerAdvice`, `@ExceptionHandler`) para capturar e formatar respostas de erro (`JSON`) de forma consistente (`400 Bad Request`, `404 Not Found`, `500 Internal Server Error`), proporcionando uma melhor experiência para o cliente da API.
-   **Documentação Interativa da API:**
    -   `Swagger UI` (`OpenAPI 3.x`) integrado para uma documentação `interativa` e auto-gerada da API, facilitando a exploração e o teste dos endpoints.

## ⚙️ Tecnologias Detalhadas

-   **Java 17:** Linguagem principal para o desenvolvimento do backend, escolhida por sua robustez, performance e amplo suporte empresarial.
-   **Spring Boot 3.x:** Framework de desenvolvimento rápido de aplicações Java, simplificando a configuração e o deploy com seu ecossistema abrangente (autoconfiguração, starters).
-   **Spring Data JPA:** Facilita a implementação de camadas de acesso a dados, permitindo a interação com o banco de dados via objetos Java (entidades) e a criação automática de métodos de repositório (CRUD e consultas personalizadas).
-   **Hibernate 6.x:** Implementação de `JPA` padrão para o mapeamento objeto-relacional (ORM), gerenciando a persistência de dados entre objetos Java e o banco de dados.
-   **PostgreSQL:** Sistema de gerenciamento de banco de dados relacional (SGBD) de código aberto, robusto e escalável, utilizado para armazenar os dados da aplicação.
-   **Spring Security:** Framework de segurança fundamental para aplicações Spring, responsável por autenticação, autorização e proteção contra vulnerabilidades comuns.
-   **Project Lombok:** Biblioteca que reduz a verbosidade do código Java através de anotações (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`), gerando getters, setters, construtores e outros métodos automaticamente em tempo de compilação.
-   **Maven:** Ferramenta de automação de build e gerenciamento de dependências para projetos Java.
-   **Axios (para testes com o Front-end e Postman):** Cliente HTTP baseado em Promises, utilizado indiretamente para demonstrar o consumo da API.
-   **SpringDoc OpenAPI (Swagger UI):** Gera automaticamente a documentação da API no formato OpenAPI 3.x, disponibilizando uma interface web interativa (Swagger UI) para testar os endpoints.

## 🚀 Como Rodar o Projeto Localmente

1.  **Pré-requisitos:**
    -   **Java Development Kit (JDK) 17** ou superior instalado.
    -   **Apache Maven 3.x** instalado.
    -   **PostgreSQL** instalado e rodando em sua máquina.
    -   Um cliente PostgreSQL (ex: `pgAdmin`) para criar o banco de dados.

2.  **Configuração do Banco de Dados PostgreSQL:**
    -   Crie um banco de dados vazio com o nome `ecommerce_db`.
    -   Crie um usuário para o banco de dados (ex: `postgres`) e defina uma senha (ex: `2302`).
    -   Conceda todas as permissões (`GRANT ALL PRIVILEGES`) para este usuário no banco de dados `ecommerce_db`.

3.  **Configuração da Aplicação (application.properties):**
    -   Abra o arquivo `src/main/resources/application.properties`.
    -   Certifique-se de que as credenciais de conexão do banco de dados correspondem à sua configuração local do PostgreSQL:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
        spring.datasource.username=postgres
        spring.datasource.password=2302
        ```
    -   Verifique também as configurações de CORS se estiver usando um front-end local diferente de `localhost:5173`:
        ```properties
        myapp.frontend.url=http://localhost:5173
        ```
        *(Opcional: Para evitar conflitos de porta, você pode configurar `server.port=8080` ou outro, se necessário).*

4.  **Clonar o Repositório:**
    ```bash
    git clone [https://github.com/GustaaSouzaa/ecommerce-backend.git](https://github.com/GustaaSouzaa/ecommerce-backend.git)
    cd ecommerce-backend
    ```

5.  **Compilar e Empacotar o Projeto:**
    -   Abra o projeto no IntelliJ IDEA (ou sua IDE Java preferida).
    -   No terminal integrado da IDE ou em um terminal comum na raiz do projeto (`ecommerce-backend`), execute o comando Maven para compilar e gerar o JAR:
        ```bash
        mvn clean package
        ```
        (Isso criará o `ecommerce-backend-0.0.1-SNAPSHOT.jar` na pasta `target/`).

6.  **Rodar a Aplicação:**
    -   No IntelliJ IDEA, localize a classe principal `EcommerceBackendApplication.java`.
    -   Clique com o botão direito e selecione `Run 'EcommerceBackendApplication.main()'`.
    -   Aguarde a inicialização. O console deverá exibir `Tomcat started on port 8080 (http)`.

7.  **Acessar a API Localmente:**
    -   A API estará disponível em `http://localhost:8080`.
    -   A documentação interativa (`Swagger UI`) pode ser acessada em: `http://localhost:8080/swagger-ui.html`
    -   Utilize ferramentas como Postman ou o próprio Swagger UI para testar os endpoints (ex: `GET http://localhost:8080/api/products`).

## 🔑 Autenticação e Autorização (Back-end Detalhado)

-   **Registro de Usuário:** `POST /api/auth/register`
    -   **Headers:** `Content-Type: application/json`
    -   **Body:**
        ```json
        {
          "username": "novo_usuario",
          "password": "senha_segura",
          "role": "ROLE_USER" / ou "ROLE_ADMIN"
        }
        ```
    -   **Resposta:** `201 Created`

-   **Login de Usuário:** `POST /api/auth/login`
    -   **Headers:** `Content-Type: application/json`
    -   **Body:**
        ```json
        {
          "username": "usuario_existente",
          "password": "senha_existente"
        }
        ```
    -   **Autenticação:** Para acessar endpoints protegidos (`/api/cart/**`, `/api/orders/**`, `POST/PUT/DELETE /api/products`), utilize `HTTP Basic Auth` nas suas requisições. No Postman/Swagger UI, preencha o `username` e `password` do usuário logado na seção de `Authorization`.
    -   **Resposta:** `200 OK` (se credenciais válidas).

---

Made with ❤️ by [Seu Nome Completo]