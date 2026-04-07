package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Criar {
    private static int contadorId = 1;       // iniciado em 1; ajustado pelo banco via inicializarContadores()
    private static int contadorProduto = 0;  // produtos usam AUTOINCREMENT — valor ignorado pelo banco
    private static Scanner sc = new Scanner(System.in);

    /**
     * Bug Fix #1 — Inicializa os contadores de ID a partir do banco.
     * Consulta o maior ID existente em cada tabela e continua a partir daí.
     * Deve ser chamado uma única vez, logo após os Repositories serem criados.
     */
    public static void inicializarContadores(Connection conexao) {
        try (Statement stmt = conexao.createStatement()) {
            // Próximo ID de usuário = MAX(id) atual + 1
            ResultSet rsU = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM usuarios");
            if (rsU.next()) contadorId = rsU.getInt(1);

            // contadorProduto não é necessário (banco usa AUTOINCREMENT), mas mantemos consistente
            ResultSet rsP = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM produtos");
            if (rsP.next()) contadorProduto = rsP.getInt(1);

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao inicializar contadores: " + e.getMessage());
        }
    }

    public static Produto produto() {
        System.out.printf("Nome = ");
        sc.nextLine();
        String nome = sc.nextLine();
        System.out.printf("Preço = ");
        double preco = entradaDouble();
        System.out.printf("Estoque = ");
        int estoque = 0;
        while (true) {
            try {
                estoque = entradaInt();
                if (estoque < 0)
                    throw new NumeroNegativoException("O estoque não pode ser negativo");
                break;
            } catch (NumeroNegativoException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        return new Produto(contadorProduto++, nome, preco, estoque);
    }

    public static Cliente cliente() {
        System.out.printf("Nome completo = ");
        sc.nextLine();
        String nomeCompleto = sc.nextLine();
        System.out.printf("CPF = ");
        String cpf = sc.nextLine();
        System.out.printf("Data de nascimento (aaaa-mm-dd) = ");
        String dataDeNascimento = sc.nextLine();
        System.out.printf("Username = ");
        String username = sc.nextLine();
        System.out.printf("Senha = ");
        String senha = sc.nextLine();
        Cliente cliente = new Cliente();
        while (true) {
            try {
                cliente = new Cliente(contadorId++, nomeCompleto, cpf, LocalDate.parse(dataDeNascimento), username, senha);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Formato errado de data de nascimento. Tente novamente!");
                System.out.printf("Data de nascimento (aaaa-mm-dd) = ");
                dataDeNascimento = sc.nextLine();
            }
        }
        return cliente;
    }

    /**
     * Bug Fix #2 — Lê um inteiro do terminal e consome o '\n' restante no buffer.
     * Sem o sc.nextLine() após nextInt(), o próximo sc.nextLine() (ex: username no login)
     * leria uma string vazia em vez do texto digitado pelo usuário.
     */
    public static int entrada() {
        while (true) {
            try {
                int val = sc.nextInt();
                sc.nextLine(); // consome o '\n' deixado pelo nextInt — Bug Fix #2
                return val;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.");
                sc.nextLine();
            }
        }
    }

    /** Alias de entrada() — mantém compatibilidade com chamadas do Admin e outros. */
    public static int entradaInt() {
        while (true) {
            try {
                int val = sc.nextInt();
                sc.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.");
                sc.nextLine();
            }
        }
    }

    /** Lê um double do terminal com tratamento de erro. */
    public static double entradaDouble() {
        while (true) {
            try {
                double val = sc.nextDouble();
                return val;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.");
                sc.nextLine();
            }
        }
    }
}