# 🗄️ Banco de Dados no Projeto PowerByte

> **Responsável por esta implementação:** Diego Euzébio
>
> ⚠️ *Por razões de força maior, não será possível realizar a apresentação presencial desta parte do trabalho. Este documento registra de forma completa e detalhada toda a implementação realizada.*

---

## O que é este projeto?

O PowerByte é um e-commerce acadêmico em Java que simula uma loja virtual via terminal: cadastro de usuários, login, produtos, carrinho e pedidos.

---

## O problema original

Antes desta implementação, todos os dados existiam **apenas na memória RAM** — ao fechar o programa, tudo era perdido:

- Produtos cadastrados? Sumiam.
- Usuários criados? Perdidos.
- Pedidos realizados? Nenhum histórico.

---

## A solução: SQLite via JDBC

Foi integrado o **SQLite**, um banco de dados que funciona como um único arquivo (`powerbyte.db`) criado automaticamente no disco. Sem instalação de servidor, sem configuração extra — ideal para projetos acadêmicos e portátil entre máquinas.

A comunicação Java ↔ banco é feita via **JDBC** (Java Database Connectivity), usando o driver `sqlite-jdbc`.

---

## Tabelas criadas

| Tabela | O que armazena |
|---|---|
| `usuarios` | Admins e Clientes — campo `tipo` diferencia; `username` e `CPF` são únicos |
| `produtos` | Catálogo da loja (nome, preço, estoque) |
| `pedidos` | Cabeçalho de cada compra (cliente, data, status, total) |
| `itens_pedido` | Produtos de cada pedido com preço histórico |

---

## O que foi implementado — detalhado

### 1. Fundação do banco de dados

#### `DatabaseManager.java` *(arquivo novo)*
Classe responsável por:
- Abrir (ou criar) o arquivo `powerbyte.db`
- Criar todas as tabelas automaticamente se não existirem
- Inserir 8 produtos de exemplo na primeira execução (seeder)
- Disponibilizar a conexão para os demais repositórios via `getConexao()`
- Fechar a conexão corretamente ao encerrar o programa

#### `UsuarioRepository.java` *(refatorado)*
Substituiu a lista `ArrayList<Usuario>` por operações SQL reais:
- `salvar()` — INSERT na tabela `usuarios`
- `buscarPorUsername()` — SELECT com WHERE
- `listar()` — SELECT ALL
- Constraint `UNIQUE` no CPF impede duplicatas no banco

#### `ProdutoRepository.java` *(refatorado)*
- Todas as operações de produto usando SQL
- ID gerado automaticamente pelo banco (AUTOINCREMENT)
- `decrementarEstoque()` — UPDATE direto no banco ao finalizar compra
- `buscarPorId()` — SELECT por ID

#### `PedidoRepository.java` *(arquivo novo)*
- `salvar()` — insere em `pedidos` E `itens_pedido` em **transação atômica**
- `buscarTodosPorUsername()` — carrega histórico completo de pedidos do cliente do banco
- `atualizarStatus()` — persiste mudança de status do pedido no banco
- `gerarRelatorio()` — relatório de vendas usando SQL com agregações
- `proximoId()` — consulta o MAX(id) do banco para continuar sequência

---

### 2. Melhorias de integridade e funcionalidade

| # | Melhoria | Arquivo | Como foi feita |
|---|---|---|---|
| **#1** | Estoque decrementado na compra | `ProdutoRepository`, `Main` | `decrementarEstoque()` chamado ao finalizar pedido; compra cancelada se estoque insuficiente |
| **#3** | CPF único por usuário | `UsuarioRepository`, DDL | Constraint `UNIQUE` no campo `cpf` — banco rejeita duplicatas |
| **#4** | Transações ao salvar pedidos | `PedidoRepository` | `setAutoCommit(false)` + `COMMIT / ROLLBACK` — ou tudo é salvo ou nada |
| **#7** | Histórico de pedidos persistido | `PedidoRepository`, `Main` | `buscarTodosPorUsername()` carrega pedidos do banco ao fazer login |
| **#8** | Relatório de vendas do Admin | `PedidoRepository`, `Admin`, `Menus` | SQL com `SUM`, `COUNT`, `AVG`, `GROUP BY` — receita total, ticket médio, top produtos |

---

### 3. Correções de bugs críticos

#### Bug Fix #1 — Colisão de IDs após reiniciar o programa
**Problema:** O programa iniciava os contadores de ID em `0` a cada execução. Se o banco já tinha usuários/produtos cadastrados, novos registros tentavam usar IDs já existentes, causando erros ou sobrescrita de dados.

**Solução:** `Criar.inicializarContadores(conexao)` — consulta `MAX(id)` em cada tabela ao iniciar e define o próximo ID corretamente.

#### Bug Fix #2 — Buffer do Scanner causava leitura vazia
**Problema:** Após ler um número inteiro com `sc.nextInt()`, o caractere de nova linha (`\n`) permanecia no buffer. O próximo `sc.nextLine()` (como no campo de username no login) lia esse `\n` como string vazia.

**Solução:** `Criar.entrada()` e `Criar.entradaInt()` chamam `sc.nextLine()` logo após `sc.nextInt()`, descartando o `\n` residual.

---

### 4. Integração e compatibilidade com o restante da equipe

A versão do repositório foi atualizada pela equipe durante o desenvolvimento. Foi necessário **mesclar as alterações locais com a nova versão** do repositório remoto:

- Incorporadas as melhorias da equipe: `Criar.entradaInt()` e `Criar.entradaDouble()` (leitura robusta com tratamento de erro), `Admin.java` refatorado para não usar `Scanner` próprio
- Mantidas todas as implementações de banco de dados sem conflito
- O banco é **totalmente transparente** para a lógica de negócio existente — nenhuma classe de domínio precisou ser modificada estruturalmente

---

## Estrutura de arquivos criados/modificados

```
projetofinalpoo/src/main/java/org/example/
├── DatabaseManager.java      ← NOVO: conexão, DDL, seeder
├── Main.java                 ← MODIFICADO: inicializa banco, fecha conexão
├── Criar.java                ← MODIFICADO: inicializarContadores(), entradaInt(), entradaDouble()
├── Admin.java                ← MODIFICADO: relatorioVendas(), usa Criar.entradaInt()
├── Pedido.java               ← MODIFICADO: setRepository(), persistência de status, toString()
├── ItemPedido.java           ← MODIFICADO: getters para acesso via repositório
├── UsuarioRepository.java    ← RECRIADO: ArrayList → SQL
├── ProdutoRepository.java    ← RECRIADO: ArrayList → SQL + decrementarEstoque()
└── PedidoRepository.java     ← NOVO: salvar, buscar, relatório, transações
```

---

## Como executar

```bash
# Compilar
javac -cp "lib/sqlite-jdbc-3.36.0.3.jar" -d "target/classes" src/main/java/org/example/*.java

# Executar (Windows)
java -cp "target/classes;lib/sqlite-jdbc-3.36.0.3.jar" org.example.Main

# Executar (Linux/Mac)
java -cp "target/classes:lib/sqlite-jdbc-3.36.0.3.jar" org.example.Main
```

O arquivo `powerbyte.db` é criado automaticamente na primeira execução com 8 produtos de exemplo já carregados.

---

*Implementação realizada por **Diego Euzébio** como parte da disciplina de Programação Orientada a Objetos.*
