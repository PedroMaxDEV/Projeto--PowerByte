package org.example;

import java.sql.*;

/**
 * ProdutoRepository — Repositório de produtos com persistência em SQLite.
 *
 * Opera sobre a tabela 'produtos' do arquivo powerbyte.db.
 * Todo CRUD (salvar, listar, buscar, remover) é feito via SQL/JDBC.
 *
 * Nota sobre IDs: O banco usa AUTOINCREMENT, mas como a classe Produto
 * recebe o ID no construtor, fazemos INSERT e depois recuperamos o ID gerado.
 */
public class ProdutoRepository {

    private final Connection conexao;

    public ProdutoRepository(Connection conexao) {
        this.conexao = conexao;
    }

    /**
     * Persiste um novo produto no banco.
     * O ID é gerado pelo banco (AUTOINCREMENT). O objeto produto é atualizado com o ID gerado.
     */
    public void salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.executeUpdate();

            // Atualiza o ID do objeto com o ID gerado pelo banco
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                produto.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao salvar produto: " + e.getMessage());
        }
    }

    /**
     * Lista todos os produtos cadastrados.
     */
    public void listar() {
        String sql = "SELECT * FROM produtos";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                montarProduto(rs).exibirInfo();
                System.out.println("=======================");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar produtos: " + e.getMessage());
        }
    }

    /**
     * Remove um produto pelo ID.
     * @return true se removido, false se não encontrado.
     */
    public boolean remover(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao remover produto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca produto por ID.
     * Retorna um ProdutoSQLite que sincroniza estoque e preço com o banco ao ser modificado.
     */
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarProdutoSincronizado(rs);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar produto por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca produto por nome.
     */
    public Produto buscarPorNome(String nome) {
        String sql = "SELECT * FROM produtos WHERE nome = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarProdutoSincronizado(rs);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar produto por nome: " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica se não há produtos cadastrados.
     */
    public boolean vazio() {
        String sql = "SELECT COUNT(*) FROM produtos";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1) == 0;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao verificar produtos: " + e.getMessage());
            return true;
        }
    }

    /**
     * Decrementa o estoque de um produto após uma compra.
     * Verifica se há estoque suficiente antes de operar.
     * @return true se decrementado com sucesso, false se estoque insuficiente.
     */
    public boolean decrementarEstoque(int produtoId, int quantidade) {
        // Lê o estoque atual do banco
        String sqlSelect = "SELECT estoque FROM produtos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlSelect)) {
            stmt.setInt(1, produtoId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return false;

            int estoqueAtual = rs.getInt("estoque");
            if (estoqueAtual < quantidade) {
                System.out.printf("[ESTOQUE] Produto ID=%d: estoque insuficiente (%d disponíveis, %d solicitados).%n",
                    produtoId, estoqueAtual, quantidade);
                return false;
            }

            // Decrementa no banco
            String sqlUpdate = "UPDATE produtos SET estoque = estoque - ? WHERE id = ?";
            try (PreparedStatement upd = conexao.prepareStatement(sqlUpdate)) {
                upd.setInt(1, quantidade);
                upd.setInt(2, produtoId);
                upd.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao decrementar estoque: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza estoque e preço de um produto no banco.
     * Chamado após modificações feitas via produto.atualizar().
     */
    public void atualizar(Produto produto) {
        String sql = "UPDATE produtos SET preco = ?, estoque = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDouble(1, produto.getPreco());
            stmt.setInt(2, produto.getEstoque());
            stmt.setInt(3, produto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao atualizar produto: " + e.getMessage());
        }
    }

    // Monta um Produto simples (apenas leitura)
    private Produto montarProduto(ResultSet rs) throws SQLException {
        return new Produto(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getDouble("preco"),
            rs.getInt("estoque")
        );
    }

    // Monta um ProdutoSincronizado que persiste mudanças de volta ao banco
    private Produto montarProdutoSincronizado(ResultSet rs) throws SQLException {
        int id         = rs.getInt("id");
        String nome    = rs.getString("nome");
        double preco   = rs.getDouble("preco");
        int estoque    = rs.getInt("estoque");
        return new ProdutoSincronizado(id, nome, preco, estoque, this);
    }
}
