package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseManager — Gerenciador central de conexão com o banco de dados SQLite.
 *
 * Responsabilidades:
 *  1. Abrir (e manter aberta) uma única conexão com o arquivo powerbyte.db
 *  2. Criar as tabelas do sistema na primeira execução (CREATE TABLE IF NOT EXISTS)
 *  3. Fornecer a conexão para os Repositories via getConexao()
 *  4. Fechar a conexão quando o programa encerra
 *
 * O arquivo powerbyte.db é criado automaticamente na pasta onde o programa é executado.
 */
public class DatabaseManager {

    // URL de conexão JDBC para SQLite — o arquivo será criado automaticamente
    private static final String URL = "jdbc:sqlite:powerbyte.db";

    // Conexão única compartilhada por todos os Repositories (Singleton de conexão)
    private Connection conexao;

    /**
     * Construtor: abre a conexão e inicializa as tabelas.
     */
    public DatabaseManager() {
        try {
            // Carrega o driver JDBC do SQLite explicitamente
            Class.forName("org.sqlite.JDBC");
            conexao = DriverManager.getConnection(URL);
            System.out.println("[DB] Banco de dados conectado: powerbyte.db");
            criarTabelas();
            semeadorDeProdutos();
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] Driver SQLite não encontrado: " + e.getMessage());
            throw new RuntimeException("Driver SQLite não encontrado. Verifique o classpath.", e);
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException("Falha crítica: não foi possível conectar ao banco de dados.", e);
        }
    }

    /**
     * Cria todas as tabelas necessárias caso ainda não existam.
     * O uso de IF NOT EXISTS garante que este método é seguro para rodar sempre.
     */
    private void criarTabelas() {
        try (Statement stmt = conexao.createStatement()) {

            // --- Tabela de Usuários ---
            // Armazena tanto Admins quanto Clientes. O campo 'tipo' diferencia os dois.
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id               INTEGER PRIMARY KEY,
                    nome_completo    TEXT    NOT NULL,
                    cpf              TEXT    UNIQUE NOT NULL,
                    data_nascimento  TEXT    NOT NULL,
                    username         TEXT    UNIQUE NOT NULL,
                    senha            TEXT    NOT NULL,
                    tipo             TEXT    NOT NULL
                )
                """);

            // --- Tabela de Produtos ---
            // Catálogo de produtos disponíveis na loja.
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS produtos (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome     TEXT    NOT NULL,
                    preco    REAL    NOT NULL,
                    estoque  INTEGER NOT NULL
                )
                """);

            // --- Tabela de Pedidos ---
            // Cada pedido pertence a um cliente (identificado pelo username).
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pedidos (
                    id               INTEGER PRIMARY KEY,
                    username_cliente  TEXT    NOT NULL,
                    data             TEXT    NOT NULL,
                    status           TEXT    NOT NULL,
                    total            REAL    NOT NULL
                )
                """);

            // --- Tabela de Itens do Pedido ---
            // Cada linha representa um produto dentro de um pedido.
            // Armazenamos nome e preço diretamente para manter o histórico mesmo se o produto for removido.
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS itens_pedido (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    pedido_id       INTEGER NOT NULL,
                    produto_id      INTEGER NOT NULL,
                    produto_nome    TEXT    NOT NULL,
                    preco_unitario  REAL    NOT NULL,
                    quantidade      INTEGER NOT NULL,
                    FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
                )
                """);

            System.out.println("[DB] Tabelas verificadas/criadas com sucesso.");

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao criar tabelas: " + e.getMessage());
        }
    }

    /**
     * Insere produtos de exemplo na primeira execução (quando o catálogo está vazio).
     * Usa INSERT OR IGNORE para não duplicar caso o produto já exista.
     */
    private void semeadorDeProdutos() {
        String verificar = "SELECT COUNT(*) FROM produtos";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs   = stmt.executeQuery(verificar)) {

            if (rs.getInt(1) > 0) return; // catálogo já tem produtos — não faz nada

            String sql = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
            Object[][] exemplos = {
                {"Notebook Lenovo IdeaPad",      2899.99, 8 },
                {"Mouse Gamer Redragon",           149.90, 30},
                {"Teclado Mecânico HyperX",        349.90, 20},
                {"Monitor LG 24\" Full HD",       1199.90, 12},
                {"SSD Kingston 480GB",              259.90, 25},
                {"Headset Logitech G332",           299.90, 18},
                {"Webcam Full HD 1080p",            189.90, 15},
                {"Mousepad XL RGB",                 79.90, 40},
            };

            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                for (Object[] p : exemplos) {
                    ps.setString(1, (String)  p[0]);
                    ps.setDouble(2, (Double)  p[1]);
                    ps.setInt   (3, (Integer) p[2]);
                    ps.executeUpdate();
                }
            }
            System.out.println("[DB] Catálogo inicial carregado com " + exemplos.length + " produtos.");

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao carregar produtos iniciais: " + e.getMessage());
        }
    }

    /**
     * Retorna a conexão ativa para uso pelos Repositories.
     */
    public Connection getConexao() {
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados.
     * Deve ser chamado ao encerrar o programa.
     */
    public void fechar() {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("[DB] Conexão encerrada.");
            } catch (SQLException e) {
                System.err.println("[DB] Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
