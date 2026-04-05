package org.example;
import java.time.LocalDate;
import java.util.Scanner;
public class Criar {
    private static int contadorId = 0;
    private static int contadorProduto = 0;
    private static Scanner sc = new Scanner(System.in);
    public static Produto produto() {
        System.out.printf("Nome = ");
        sc.nextLine();
        String nome = sc.nextLine();
        System.out.printf("Preço = ");
        double preco = sc.nextDouble();
        System.out.printf("Estoque = ");
        int estoque = sc.nextInt();
        sc.close();
        return new Produto(contadorProduto++, nome, preco, estoque);
    }
    public static Cliente cliente() {
        System.out.printf("Nome completo = ");
        String nomeCompleto = sc.nextLine();
        System.out.printf("CPF = ");
        String cpf = sc.nextLine();
        System.out.printf("Data de nascimento (aaaa--mm--dd) = ");
        String dataDeNascimento = sc.nextLine();
        System.out.printf("Username = ");
        String username = sc.nextLine();
        System.out.printf("Senha = ");
        String senha = sc.nextLine();
        sc.close();
        return new Cliente(contadorId++, nomeCompleto, cpf, LocalDate.parse(dataDeNascimento), username, senha);
    }
}