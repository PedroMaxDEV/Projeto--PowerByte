# 🗄️ Banco de Dados — PowerByte

**Responsável:** Carlos Eduardo

> ⚠️ *Por motivos de força maior, Carlos Eduardo não estará presente na apresentação. Esta documentação registra sua contribuição ao projeto.*

---

## O problema original

Antes desta implementação, todos os dados existiam **apenas na memória RAM** — ao fechar o programa, tudo era perdido (usuários, produtos, pedidos).

## A solução: SQLite

Foi integrado o **SQLite via JDBC**: um banco de dados que funciona como um arquivo (`powerbyte.db`) criado automaticamente no disco, sem necessidade de instalar nenhum servidor.

---

## O que foi implementado

| Arquivo | O que faz |
|---|---|
| `DatabaseManager.java` *(novo)* | Abre a conexão, cria as tabelas e insere 8 produtos de exemplo na primeira execução |
| `UsuarioRepository.java` | Substituído de lista em memória para SQL real (INSERT, SELECT) |
| `ProdutoRepository.java` | Idem, com decremento de estoque no banco ao finalizar compra |
| `PedidoRepository.java` *(novo)* | Salva pedidos em transação (`COMMIT/ROLLBACK`), carrega histórico, gera relatório |
| `Main.java` | Inicializa o banco e fecha a conexão ao sair |
| `Criar.java` | Sincroniza contadores de ID com o banco; adiciona leitura segura de entradas |

## Funcionalidades de banco

- **Estoque real** — decrementado no banco ao finalizar compra; compra cancelada se insuficiente
- **CPF único** — constraint `UNIQUE` impede duplicatas no banco
- **Transações** — pedidos salvos com `COMMIT/ROLLBACK` (tudo ou nada)
- **Histórico** — pedidos carregados do banco ao fazer login
- **Relatório de vendas** — SQL com `SUM`, `COUNT`, `AVG`, `GROUP BY` para o Admin

---

## Como executar

```bash
# Compilar (Windows)
javac -cp "lib/sqlite-jdbc-3.36.0.3.jar" -d "target/classes" src/main/java/org/example/*.java

# Executar (Windows)
java -cp "target/classes;lib/sqlite-jdbc-3.36.0.3.jar" org.example.Main
```

O arquivo `powerbyte.db` é criado automaticamente na primeira execução.
