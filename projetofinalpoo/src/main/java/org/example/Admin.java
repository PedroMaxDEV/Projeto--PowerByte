package org.example;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
public class Admin extends Usuario{
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    public Admin(int id, String nomeCompleto, String cpf, LocalDate dataDeNascimento, String username, String senha) {
        super(id, nomeCompleto, cpf, dataDeNascimento, username, senha);
        tipo = "Admin";
    }
    @Override
    public void exibirInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nome completo: " + nomeCompleto);
        System.out.println("CPF: " + cpf);
        System.out.println("Data de nascimento: " + dataDeNascimento);
        System.out.println("Username: " + username);

    }
    public void cadastrarProduto(Produto produto){
        produtos.add(produto);
    }
    public void cadastrarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }
    public void removerProduto(int id){
        boolean condition = true;
        for(int i = 0; i < produtos.size(); i++) {
            if(produtos.get(i).getId() == id) {
                condition = false;
                produtos.remove(i);
                System.out.println("Produto removido.");
                break;
            }   
        }
        if(condition) 
            System.out.println("Produto não foi encontrado.");
    }
    public void removerPedido(int id) {
        boolean condition = true;
        for(int i = 0; i < pedidos.size(); i++) {
            if(pedidos.get(i).getId() == id) {
                condition = false;
                pedidos.remove(i);
                System.out.println("Pedido removido.");
                break;
            }   
        }
        if(condition) 
            System.out.println("Pedido não foi encontrado.");
    }
    public void atualizarProduto(){
        int comando = 0;
        int id;
        int quantia;
        boolean condition = true;
        while(true) {
            System.out.println("1. Repor estoque de produto");
            System.out.println("2. Reduzir estoque de produto");
            comando = scanner.nextInt();
            if(comando == 1 || comando == 2) {
                break;
            } else {
                System.out.println("Comando inválido. Tente novamente");
            }
        }
        System.out.printf("ID do produto = ");
        id = scanner.nextInt();
        System.out.printf("Quantidade a ser reposta/reduzida = ");
        quantia = scanner.nextInt();
        for(int i = 0; i < produtos.size(); i++) {
            if(produtos.get(i).getId() == id) {
                condition = false;
                if(comando == 1)    
                    produtos.get(i).reporEstoque(quantia);
                else 
                    produtos.get(i).reduzirEstoque(quantia);
                break;
            } 
        }
        if(condition) 
            System.out.println("Produto não foi encontrado.");
    }
    public void listarClientes() {
        for(int i = 0; i < pedidos.size(); i++) 
            pedidos.get(i).getCliente().exibirInfo();
    }
    public void listarPedidos() {
        for(int i = 0; i < pedidos.size(); i++) 
            pedidos.get(i).exibirInfo();
    }
    public void atualizarStatusPedido() {
        int id;
        String descricao;
        boolean condition = true;
        StatusPedido novoStatus;
        System.out.printf("ID do pedido = ");
        id = scanner.nextInt();
        System.out.printf("Status atualizado do pedido = ");   
        scanner.nextLine();
        descricao = scanner.nextLine();
        novoStatus = StatusPedido.valueOf(descricao);
        for(int i = 0; i < pedidos.size(); i++) {
            if(pedidos.get(i).getId() == id) {
                condition = false;
                pedidos.get(i).atualizarStatus(novoStatus);
                break;
            }
        }
        if(condition)
            System.out.println("Pedido não foi encontrado.");
    }
}