package org.example;

import java.sql.*;
import java.util.ArrayList;

/**
 * PedidoRepository — Repositório de pedidos com persistência em SQLite.
 *
 * Opera sobre as tabelas 'pedidos' e 'itens_pedido' do powerbyte.db.
 *
 * Quando um pedido é salvo:
 *  1. Um registro é inserido em 'pedidos'
 *  2. Cada item do pedido é inserido em 'itens_pedido' referenciando o pedido
 *
 * Ao carregar pedidos, os itens são reconstruídos a partir do banco.
 * O preço unitário é armazenado historicamente — mesmo se o produto mudar de preço,
 * o pedido registra o valor pago na época da compra.
 */
public class PedidoRepository {

    private final Connection conexao;

    public PedidoRepository(Connection conexao) {
        this.conexao = conexao;
    }

    /**
     * Bug Fix #1 — Retorna o próximo ID disponível para pedidos.
     * Consulta MAX(id) no banco para evitar colisão de IDs após reiniciar o programa.
     */
    public int proximoId() {
        String sql = "SELECT COALESCE(MAX(id), -1) + 1 FROM pedidos";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao obter próximo ID de pedido: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Persiste um pedido completo (cabeçalho + itens) em uma única transação.
     * Se qualquer passo falhar, a operação é revertida por completo (ROLLBACK).
     */
    public void salvar(Pedido pedido) {
        String sqlPedido = """
            INSERT OR IGNORE INTO pedidos (id, username_cliente, data, status, total)
            VALUES (?, ?, ?, ?, ?)
            """;
        try {
            conexao.setAutoCommit(false); // início da transação

            try (PreparedStatement stmt = conexao.prepareStatement(sqlPedido)) {
                stmt.setInt(1, pedido.getId());
                stmt.setString(2, pedido.getUsername());
                stmt.setString(3, pedido.getData().toString());
                stmt.setString(4, pedido.getStatus().name());
                stmt.setDouble(5, pedido.getTotal());
                stmt.executeUpdate();
            }

            // Salva cada item do pedido dentro da mesma transação
            for (ItemPedido item : pedido.getItens()) {
                salvarItem(pedido.getId(), item);
            }

            conexao.commit(); // confirma tudo de uma vez

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao salvar pedido, revertendo...");
            try { conexao.rollback(); } catch (SQLException ex) { /* ignora */ }
            System.err.println("[DB] Rollback executado: " + e.getMessage());
        } finally {
            try { conexao.setAutoCommit(true); } catch (SQLException e) { /* ignora */ }
        }
    }

    /**
     * Salva um item individual na tabela itens_pedido.
     */
    private void salvarItem(int pedidoId, ItemPedido item) {
        String sql = """
            INSERT INTO itens_pedido (pedido_id, produto_id, produto_nome, preco_unitario, quantidade)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.setInt(2, item.getProduto().getId());
            stmt.setString(3, item.getProduto().getNome());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.setInt(5, item.getQuantidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao salvar item do pedido: " + e.getMessage());
        }
    }

    /**
     * Atualiza o status de um pedido no banco.
     */
    public void atualizarStatus(Pedido pedido) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, pedido.getStatus().name());
            stmt.setInt(2, pedido.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao atualizar status do pedido: " + e.getMessage());
        }
    }

    /**
     * Lista todos os pedidos com seus itens.
     */
    public void listar() {
        String sql = "SELECT * FROM pedidos";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            boolean algum = false;
            while (rs.next()) {
                algum = true;
                System.out.println("ID Pedido: "  + rs.getInt("id"));
                System.out.println("Cliente: "      + rs.getString("username_cliente"));
                System.out.println("Data: "         + rs.getString("data"));
                System.out.println("Status: "       + rs.getString("status"));
                System.out.println("Total: R$ "     + rs.getDouble("total"));
                listarItensDoPedido(rs.getInt("id"));
                System.out.println("=======================");
            }
            if (!algum) System.out.println("Nenhum pedido registrado.");
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar pedidos: " + e.getMessage());
        }
    }

    /**
     * Exibe os itens de um pedido específico.
     */
    private void listarItensDoPedido(int pedidoId) {
        String sql = "SELECT * FROM itens_pedido WHERE pedido_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.printf("  - %s (x%d) @ R$%.2f%n",
                    rs.getString("produto_nome"),
                    rs.getInt("quantidade"),
                    rs.getDouble("preco_unitario"));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar itens: " + e.getMessage());
        }
    }

    /**
     * Remove um pedido e seus itens pelo ID.
     */
    public boolean remover(int id) {
        try {
            // Remove os itens primeiro (integridade referencial)
            try (PreparedStatement stmt = conexao.prepareStatement("DELETE FROM itens_pedido WHERE pedido_id = ?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            // Remove o pedido
            try (PreparedStatement stmt = conexao.prepareStatement("DELETE FROM pedidos WHERE id = ?")) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao remover pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um pedido pelo ID e reconstrói o objeto Pedido com seus itens.
     */
    public Pedido buscarPorId(int id) {
        String sql = "SELECT * FROM pedidos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarPedido(rs);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar pedido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca o primeiro pedido de um cliente pelo username.
     */
    public Pedido buscarPorUsername(String username) {
        String sql = "SELECT * FROM pedidos WHERE username_cliente = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarPedido(rs);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar pedido por username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Monta um objeto Pedido a partir de um ResultSet do banco.
     * Reconstrói também seus itens consultando itens_pedido.
     */
    private Pedido montarPedido(ResultSet rs) throws SQLException {
        int pedidoId            = rs.getInt("id");
        String username         = rs.getString("username_cliente");
        StatusPedido status     = StatusPedido.valueOf(rs.getString("status"));

        // Reconstrói cliente mínimo para o pedido (apenas com username)
        Cliente clienteRef = new Cliente();
        clienteRef.setUsername(username);

        // Carrega os itens deste pedido
        ArrayList<ItemPedido> itens = carregarItens(pedidoId);

        // Cria o pedido reconstruído
        Pedido pedido = new Pedido(pedidoId, clienteRef, itens);
        pedido.atualizarStatus(status);
        return pedido;
    }

    /**
     * Carrega os itens de um pedido do banco e os reconstrói como objetos ItemPedido.
     */
    private ArrayList<ItemPedido> carregarItens(int pedidoId) {
        ArrayList<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT * FROM itens_pedido WHERE pedido_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int produtoId      = rs.getInt("produto_id");
                String prodNome    = rs.getString("produto_nome");
                double preco       = rs.getDouble("preco_unitario");
                int qtd            = rs.getInt("quantidade");
                // Produto reconstruído com dados históricos do pedido
                Produto prod = new Produto(produtoId, prodNome, preco, qtd);
                itens.add(new ItemPedido(prod, qtd));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao carregar itens do pedido: " + e.getMessage());
        }
        return itens;
    }

    /**
     * Busca todos os pedidos de um cliente pelo username.
     * Usado para carregar o histórico completo ao fazer login (#7).
     */
    public ArrayList<Pedido> buscarTodosPorUsername(String username) {
        ArrayList<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE username_cliente = ? ORDER BY id";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(montarPedido(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao carregar histórico de pedidos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Gera um relatório de vendas usando SQL de agregação (#8).
     * Exibe totais gerais, top 5 produtos e distribuição por status.
     */
    public void gerarRelatorio() {
        System.out.println("\n========== RELATÓRIO DE VENDAS ==========");

        // Totais gerais (excluindo cancelados)
        String sqlTotais = """
            SELECT
                COUNT(*)        AS total_pedidos,
                SUM(total)      AS receita_total,
                AVG(total)      AS ticket_medio
            FROM pedidos
            WHERE status != 'CANCELADO'
            """;
        try (Statement stmt = conexao.createStatement();
             ResultSet rs   = stmt.executeQuery(sqlTotais)) {
            System.out.printf("Pedidos realizados : %d%n",      rs.getInt("total_pedidos"));
            System.out.printf("Receita total      : R$ %.2f%n", rs.getDouble("receita_total"));
            System.out.printf("Ticket médio       : R$ %.2f%n", rs.getDouble("ticket_medio"));
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao gerar totais: " + e.getMessage());
        }

        // Top 5 produtos mais vendidos por quantidade
        System.out.println("\n--- Top 5 Produtos Mais Vendidos ---");
        String sqlTop = """
            SELECT
                produto_nome,
                SUM(quantidade)                  AS total_vendido,
                SUM(quantidade * preco_unitario) AS receita_produto
            FROM itens_pedido
            GROUP BY produto_nome
            ORDER BY total_vendido DESC
            LIMIT 5
            """;
        try (Statement stmt = conexao.createStatement();
             ResultSet rs   = stmt.executeQuery(sqlTop)) {
            int pos = 1;
            boolean algum = false;
            while (rs.next()) {
                algum = true;
                System.out.printf("  %d. %-28s | %3dx | R$ %.2f%n",
                    pos++,
                    rs.getString("produto_nome"),
                    rs.getInt("total_vendido"),
                    rs.getDouble("receita_produto"));
            }
            if (!algum) System.out.println("  Nenhuma venda registrada ainda.");
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao gerar ranking: " + e.getMessage());
        }

        // Distribuição por status
        System.out.println("\n--- Pedidos por Status ---");
        String sqlStatus = """
            SELECT status, COUNT(*) AS qtd
            FROM pedidos
            GROUP BY status
            ORDER BY qtd DESC
            """;
        try (Statement stmt = conexao.createStatement();
             ResultSet rs   = stmt.executeQuery(sqlStatus)) {
            while (rs.next()) {
                System.out.printf("  %-12s : %d pedido(s)%n",
                    rs.getString("status"), rs.getInt("qtd"));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao gerar por status: " + e.getMessage());
        }

        System.out.println("=========================================\n");
    }
}

