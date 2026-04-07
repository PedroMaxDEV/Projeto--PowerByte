# 🗄️ Banco de Dados no Projeto PowerByte

## O que é este projeto?

O PowerByte é um e-commerce acadêmico em Java que simula uma loja virtual via terminal: cadastro de usuários, login, produtos, carrinho e pedidos.

## O problema original

Antes desta implementação, todos os dados existiam **apenas na memória RAM** — ao fechar o programa, tudo era perdido.

## A solução: SQLite

Foi integrado o **SQLite**, um banco de dados que funciona como um arquivo (`powerbyte.db`) criado automaticamente no disco. Sem instalação de servidor, ideal para projetos acadêmicos.

A comunicação Java ↔ banco é feita via **JDBC**, usando o driver `sqlite-jdbc`.

## Tabelas criadas

| Tabela | O que armazena |
|---|---|
| `usuarios` | Admins e Clientes — campo `tipo` diferencia; `username` e `CPF` são únicos |
| `produtos` | Catálogo da loja (nome, preço, estoque) |
| `pedidos` | Cabeçalho de cada compra (cliente, data, status, total) |
| `itens_pedido` | Produtos de cada pedido com preço histórico |

## O que foi implementado

### Fundação do banco
- **`DatabaseManager.java`** *(novo)* — abre a conexão e cria as tabelas automaticamente
- **`UsuarioRepository`** — substituído de `ArrayList` para SQL real (INSERT, SELECT, DELETE)
- **`ProdutoRepository`** — idem, com ID gerado pelo banco (AUTOINCREMENT)
- **`ProdutoSincronizado.java`** *(novo)* — persiste mudanças de estoque/preço automaticamente
- **`PedidoRepository`** — salva pedidos em duas tabelas relacionadas
- **`Main.java`** — inicializa o banco, semeia dados e fecha conexão ao sair

### Melhorias de integridade e funcionalidade

| # | Melhoria | Como foi feita |
|---|---|---|
| **#1** | Estoque decrementado na compra | `ProdutoRepository.decrementarEstoque()` chamado no `Main` ao finalizar pedido |
| **#3** | CPF único por usuário | Constraint `UNIQUE` no campo `cpf` da tabela `usuarios` |
| **#4** | Transações ao salvar pedidos | `COMMIT / ROLLBACK` em `PedidoRepository.salvar()` — ou tudo é salvo ou nada |
| **#7** | Histórico de pedidos persistido | `PedidoRepository.buscarTodosPorUsername()` carrega pedidos do banco ao fazer login |
| **#8** | Relatório de vendas do Admin | SQL com `SUM`, `COUNT`, `AVG`, `GROUP BY` — exibe receita, ticket médio e top produtos |

## Participação do desenvolvedor

A integração foi possível porque o projeto já estava muito bem estruturado. O uso do **padrão Repository**, a hierarquia de classes (`Usuario` → `Admin`/`Cliente`) e a separação clara de responsabilidades. Adicionar o banco foi uma **extensão natural** — a lógica de negócio não precisou ser reescrita, apenas a camada de armazenamento foi substituída.

## Como executar

```bash
# Compilar
javac -cp "lib/sqlite-jdbc-3.36.0.3.jar" -d "target/classes" src/main/java/org/example/*.java

# Executar (Windows)
java -cp "target/classes;lib/sqlite-jdbc-3.36.0.3.jar" org.example.Main
```

O arquivo `powerbyte.db` é criado automaticamente na primeira execução com 8 produtos de exemplo já carregados.
