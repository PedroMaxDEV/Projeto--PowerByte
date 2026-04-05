package org.example;
import java.util.Scanner;
import java.time.LocalDate;
public class Main {
    public static void main(String[] args) {
        int comando;
        int id;
        String username;
        String senha;
        Scanner sc = new Scanner(System.in);
        Produto produto;
        Usuario usuario;
        Pedido pedido;
        UsuarioRepository usuarios = new UsuarioRepository();
        ProdutoRepository produtos = new ProdutoRepository();
        PedidoRepository pedidos = new PedidoRepository();
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
                usuario = usuarios.buscarPorUsernameESenha(username, senha);
                if(usuario == null) 
                    System.out.println("Usuário não encontrado.");
                else {
                    if(usuario.tipo.equals("Admin")) {
                        while(true) {
                            Menus.admin();
                            comando = sc.nextInt();
                            if(comando == 1) {
                                produtos.salvar(Criar.produto());
                            } else if(comando == 2) {
                                produtos.listar();
                            } else if(comando == 3) {
                                System.out.printf("Id do produto = ");
                                id = sc.nextInt();
                                if(produtos.remover(id))
                                    System.out.println("Produto removido com sucesso!");
                                else 
                                    System.out.println("Produto não encontrado.");
                            } else if(comando == 4) {
                                System.out.printf("Id do produto = ");
                                id = sc.nextInt();
                                produto = produtos.buscarPorId(id);
                                if(produto != null)     
                                    produto.atualizar();
                                else 
                                    System.out.println("Produto não encontrado.");
                            } else if(comando == 5) {
                                usuarios.listar();
                            } else if(comando == 6) {
                                pedidos.listar();
                            } else if(comando == 7) {
                                System.out.printf("Id do pedido = ");
                                id = sc.nextInt();
                                pedido = pedidos.buscarPorId(id);
                                if(pedido != null)   
                                    pedido.atualizar();
                                else 
                                    System.out.println("Pedido não encontrado.");
                            } else if(comando == 0) {
                                break;
                            }
                        }
                    } else {
                        Menus.cliente();
                    }
                }
            } else if(comando == 2) {
                usuarios.salvar(Criar.cliente());
                System.out.println("Usuário cadastrado com sucesso!");
            } else if(comando == 0) {
                System.out.println("Programa encerrado.");
                break;
            } else 
                System.out.println("Comando inválido. Tente novamente.");
        }
        sc.close();
    }
}
