package org.example;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Main {
    public static void main(String[] args) {
        int comando;
        int contadorId = 0;
        String cpf;
        String senha;
        String username;
        String nomeCompleto;
        String dataDeNascimento;
        Scanner sc = new Scanner(System.in);
        Usuario usuario;
        UsuarioRepository usuarios = new UsuarioRepository();
        usuarios.salvar(new Admin(0, "José Álvaro", "000.000.000-00", LocalDate.of(2000, 1, 1), "Álvaro", "senha"));
        while(true) {
            Menus.inicio();
            comando = sc.nextInt();
            if(comando == 1) {
                System.out.printf("Username = ");
                sc.nextLine();
                username = sc.nextLine();
                System.out.printf("Senha = ");
                senha = sc.nextLine();
                usuario = usuarios.buscarPorUsername(username);
                if(usuario == null) 
                    System.out.println("Usuário não encontrado.");
                else {
                    if(usuario.tipo.equals("Admin")) {
                        Menus.admin();
                    } else {
                        Menus.cliente();
                    }
                }
            } else if(comando == 1) {
                System.out.printf("Nome completo = ");
                nomeCompleto = sc.nextLine();
                System.out.printf("CPF = ");
                cpf = sc.nextLine();
                System.out.printf("Data de nascimento (aaaa/mm/dd) = ");
                dataDeNascimento = sc.nextLine();
                System.out.printf("Username = ");
                username = sc.nextLine();
                System.out.printf("Senha = ");
                senha = sc.nextLine();
                usuarios.salvar(new Cliente(contadorId++, nomeCompleto, cpf, LocalDate.parse(dataDeNascimento), username, senha));

            } else if(comando == 0) {
                System.out.println("Programa encerrado.");
                break;
            } else 
                System.out.println("Comando inválido. Tente novamente.");
        }
    }
}
