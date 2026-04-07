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

| Funcionalidade | Descrição |
|---|---|
| 🔄 **Persistência total** | Usuários, produtos e pedidos salvos entre execuções |
| 📦 **Catálogo inicial** | 8 produtos de exemplo inseridos automaticamente |
| 🧾 **Estoque real** | Estoque decrementado no banco ao finalizar compra |
| 🔒 **CPF único** | Constraint `UNIQUE` impede cadastro duplicado por CPF |
| ⚡ **Transações** | Pedidos salvos com `COMMIT/ROLLBACK` — sem dados parciais |
| 📜 **Histórico** | Pedidos do cliente carregados do banco ao fazer login |
| 📊 **Relatório** | Admin acessa relatório de vendas com SQL de agregação |

> A integração com banco de dados foi desenvolvida por **Carlos Eduardo**.
> ⚠️ *Por motivos de força maior, Carlos Eduardo não estará presente na apresentação, porém sua contribuição está totalmente implementada e documentada em [`BANCO_DE_DADOS.md`](BANCO_DE_DADOS.md).*

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
- Relatório de vendas (receita total, ticket médio, top produtos)

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
│   ├── Pedido.java             # Entidade pedido
│   ├── Carrinho.java           # Carrinho de compras
│   ├── ItemCarrinho.java       # Item no carrinho
│   ├── ItemPedido.java         # Item em um pedido
│   ├── StatusPedido.java       # Enum de status
│   ├── UsuarioRepository.java  # CRUD de usuários
│   ├── ProdutoRepository.java  # CRUD de produtos + estoque
│   ├── PedidoRepository.java   # CRUD de pedidos + relatório
│   ├── Menus.java              # Menus do terminal
│   └── Criar.java              # Criação de entidades via terminal
├── lib/
│   └── sqlite-jdbc-3.36.0.3.jar
├── pom.xml
├── BANCO_DE_DADOS.md
└── README.md
```

---

*Projeto desenvolvido para a disciplina de Programação Orientada a Objetos.*
