# PowerByte — E-Commerce Acadêmico em Java

> Projeto acadêmico de Programação Orientada a Objetos (POO) que simula um sistema de e-commerce via terminal.

---

## 📋 Sobre o Projeto

O **PowerByte** é um sistema de loja virtual desenvolvido em Java com foco nos conceitos de POO:
- **Herança** — `Usuario` como classe base para `Admin` e `Cliente`
- **Encapsulamento** — atributos protegidos, acesso via getters/setters
- **Polimorfismo** — comportamentos distintos entre Admin e Cliente
- **Padrão Repository** — camada dedicada de acesso a dados para cada entidade
- **Tratamento de exceções** — entradas inválidas e erros de formato

---

## 🗄️ Banco de Dados: SQLite integrado

O sistema usa **SQLite via JDBC** para persistência real de dados. Um arquivo `powerbyte.db` é criado automaticamente na primeira execução, mantendo todos os dados entre sessões.

### Funcionalidades implementadas

| Funcionalidade | Descrição |
|---|---|
| 🔄 **Persistência total** | Usuários, produtos e pedidos salvos entre execuções |
| 📦 **Catálogo inicial** | 8 produtos de exemplo inseridos automaticamente |
| 🧾 **Estoque real** | Estoque decrementado no banco ao finalizar compra |
| 🔒 **CPF único** | Constraint `UNIQUE` impede cadastro duplicado por CPF |
| ⚡ **Transações** | Pedidos salvos com `COMMIT/ROLLBACK` — sem dados parciais |
| 📜 **Histórico** | Pedidos do cliente carregados do banco ao fazer login |
| 📊 **Relatório** | Admin acessa relatório de vendas com SQL de agregação |

---

## 🚀 Como Executar

### Pré-requisitos
- JDK 17 ou superior
- Arquivo `lib/sqlite-jdbc-3.36.0.3.jar` (incluído no projeto)

### Compilar e rodar

```bash
# Compilar
javac -cp "lib/sqlite-jdbc-3.36.0.3.jar" -d "target/classes" src/main/java/org/example/*.java

# Executar (Windows)
java -cp "target/classes;lib/sqlite-jdbc-3.36.0.3.jar" org.example.Main

# Executar (Linux/Mac)
java -cp "target/classes:lib/sqlite-jdbc-3.36.0.3.jar" org.example.Main
```

Na primeira execução, `powerbyte.db` é criado automaticamente com 8 produtos de exemplo.

---

## 👤 Acesso ao Sistema

### Admin padrão
| Campo | Valor |
|---|---|
| Username | `Alvaro` |
| Senha | `senha` |

### Funcionalidades por perfil

**Admin:**
- Cadastrar, listar, atualizar e remover produtos
- Listar usuários e pedidos
- Atualizar status de pedidos
- **Relatório de vendas** (receita total, ticket médio, top produtos)

**Cliente:**
- Criar conta (CPF único validado pelo banco)
- Listar produtos e adicionar ao carrinho
- Finalizar compra (valida e decrementa estoque)
- Visualizar histórico de pedidos (persistido entre sessões)

---

## 📁 Estrutura do Projeto

```
projetofinalpoo/
├── src/main/java/org/example/
│   ├── Main.java               # Ponto de entrada
│   ├── DatabaseManager.java    # Conexão SQLite + tabelas + seeder
│   ├── Usuario.java            # Classe abstrata base
│   ├── Admin.java              # Perfil administrador
│   ├── Cliente.java            # Perfil cliente
│   ├── Produto.java            # Entidade produto
│   ├── ProdutoSincronizado.java# Produto com persistência automática
│   ├── Pedido.java             # Entidade pedido
│   ├── Carrinho.java           # Carrinho de compras
│   ├── ItemCarrinho.java       # Item no carrinho
│   ├── ItemPedido.java         # Item em um pedido
│   ├── StatusPedido.java       # Enum de status
│   ├── UsuarioRepository.java  # CRUD de usuários (CPF único)
│   ├── ProdutoRepository.java  # CRUD de produtos + decremento de estoque
│   ├── PedidoRepository.java   # CRUD de pedidos + transações + relatório
│   ├── Menus.java              # Menus do terminal
│   └── Criar.java              # Criação de entidades via terminal
├── lib/
│   └── sqlite-jdbc-3.36.0.3.jar
├── pom.xml
├── BANCO_DE_DADOS.md           # Documentação do banco de dados
└── README.md
```

---

## 👥 Contribuidores

### Equipe do Projeto

| Desenvolvedor | Contribuição Principal |
|---|---|
| **Pedro Max** | Estrutura base do projeto — classes de domínio (`Usuario`, `Admin`, `Cliente`, `Produto`, `Pedido`), sistema de menus, lógica de carrinho e fluxo de compra |
| **Diego Euzébio** | **Integração com banco de dados SQLite** — camada de persistência completa com `DatabaseManager`, todos os `Repository`, transações SQL, histórico de pedidos, relatório de vendas e correções de bugs |

### Contribuição detalhada — Diego Euzébio

> ⚠️ Nota: Por razões de força maior, **não será possível realizar a apresentação presencial** desta parte do trabalho. Toda a implementação está documentada neste README e em [`BANCO_DE_DADOS.md`](BANCO_DE_DADOS.md).

A responsabilidade de **Diego Euzébio** foi integrar persistência real ao sistema, transformando-o de um programa que perdia todos os dados ao fechar para uma aplicação com banco de dados funcional. Isso envolveu:

- Criação do **`DatabaseManager.java`** — gerenciamento de conexão, criação de tabelas e inserção de dados iniciais
- Refatoração de **`UsuarioRepository`**, **`ProdutoRepository`** e **`PedidoRepository`** — substituindo `ArrayList` por SQL real
- Implementação de **transações atômicas** (`COMMIT/ROLLBACK`) ao salvar pedidos
- Validação de **CPF único** via constraint `UNIQUE` no banco
- Controle de **estoque** com decremento no banco ao finalizar compra
- Carregamento do **histórico de pedidos** do cliente ao fazer login
- **Relatório de vendas** para o Admin com agregações SQL (`SUM`, `COUNT`, `AVG`, `GROUP BY`)
- Correção de dois bugs críticos: **inicialização de contadores de ID** a partir do banco e **consumo do buffer do Scanner** após `nextInt()`
- Mesclagem das alterações com a versão atualizada do repositório remoto

---

## 📄 Documentação Adicional

Veja [`BANCO_DE_DADOS.md`](BANCO_DE_DADOS.md) para uma explicação detalhada do banco de dados, tabelas e melhorias implementadas.

---

*Projeto desenvolvido para a disciplina de Programação Orientada a Objetos.*
