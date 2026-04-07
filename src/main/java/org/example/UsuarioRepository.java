package org.example;

import java.sql.*;
import java.time.LocalDate;

/**
 * UsuarioRepository — Repositório de usuários com persistência em SQLite.
 *
 * Todos os métodos executam SQL diretamente via JDBC.
 * Os dados são gravados na tabela 'usuarios' do arquivo powerbyte.db.
 *
 * Hierarquia suportada:
 *   - tipo = "Admin"   → instancia Admin
 *   - tipo = "Cliente" → instancia Cliente
 */
public class UsuarioRepository {

    private final Connection conexao;

    public UsuarioRepository(Connection conexao) {
        this.conexao = conexao;
    }

    /**
     * Persiste um novo usuário no banco.
     * Verifica unicidade de username E de CPF — ambos são UNIQUE no banco.
     * @return true se salvo com sucesso, false se username ou CPF já existir.
     */
    public boolean salvar(Usuario usuario) {
        String sql = """
            INSERT INTO usuarios
                (id, nome_completo, cpf, data_nascimento, username, senha, tipo)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getNomeCompleto());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getDataDeNascimento().toString());
            stmt.setString(5, usuario.getUsername());
            stmt.setString(6, usuario.getSenha());
            stmt.setString(7, usuario.getTipo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // SQLite retorna código 19 (SQLITE_CONSTRAINT) em violações de UNIQUE
            String msg = e.getMessage().toLowerCase();
            if (msg.contains("username")) {
                System.out.println("Erro: username '" + usuario.getUsername() + "' já está em uso.");
            } else if (msg.contains("cpf")) {
                System.out.println("Erro: CPF '" + usuario.getCpf() + "' já está cadastrado.");
            } else if (msg.contains("unique") || msg.contains("constraint")) {
                System.out.println("Erro: dados duplicados — verifique username e CPF.");
            }
            // Silencia para o admin padrão (que usa INSERT OR IGNORE equivalente via try/catch)
            return false;
        }
    }

    /**
     * Lista todos os usuários cadastrados no banco.
     */
    public void listar() {
        String sql = "SELECT * FROM usuarios";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            boolean algum = false;
            while (rs.next()) {
                algum = true;
                System.out.println("ID: "               + rs.getInt("id"));
                System.out.println("Nome completo: "     + rs.getString("nome_completo"));
                System.out.println("CPF: "               + rs.getString("cpf"));
                System.out.println("Data nascimento: "   + rs.getString("data_nascimento"));
                System.out.println("Username: "          + rs.getString("username"));
                System.out.println("Tipo: "              + rs.getString("tipo"));
                System.out.println("-----------------------");
            }
            if (!algum) System.out.println("Nenhum usuário cadastrado.");
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar usuários: " + e.getMessage());
        }
    }

    /**
     * Remove um usuário pelo ID.
     * @return true se foi removido, false se não encontrado.
     */
    public boolean remover(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao remover usuário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um usuário pelo ID.
     */
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarUsuario(rs);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca um usuário pelo username (utilizado no login).
     */
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarUsuario(rs);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar usuário por username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Monta o objeto Usuario (Admin ou Cliente) a partir de um ResultSet do banco.
     */
    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        int id                  = rs.getInt("id");
        String nomeCompleto     = rs.getString("nome_completo");
        String cpf              = rs.getString("cpf");
        LocalDate dataNasc      = LocalDate.parse(rs.getString("data_nascimento"));
        String username         = rs.getString("username");
        String senha            = rs.getString("senha");
        String tipo             = rs.getString("tipo");

        if ("Admin".equals(tipo)) {
            return new Admin(id, nomeCompleto, cpf, dataNasc, username, senha);
        } else {
            return new Cliente(id, nomeCompleto, cpf, dataNasc, username, senha);
        }
    }
}
