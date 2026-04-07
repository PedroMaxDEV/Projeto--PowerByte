package org.example;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Cliente extends Usuario {
    private Carrinho carrinho = new Carrinho();
    private List<Pedido> pedidos;
    private ProdutoRepository produtos;
    private Scanner sc = new Scanner(System.in);
    public Cliente(int id, String nomeCompleto, String cpf, LocalDate dataDeNascimento, String username, String senha) {
       super(id,nomeCompleto, cpf, dataDeNascimento, username, senha);
       tipo = "Cliente";
       this.pedidos = new ArrayList<>();
    }
    public Cliente() {
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public void exibirInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nome completo: " + nomeCompleto);
        System.out.println("CPF: " + cpf);
        System.out.println("Data de nascimento: " + dataDeNascimento);
        System.out.println("Username: " + username);
    }
    public void setProdutos(ProdutoRepository produtos) {
        this.produtos = produtos;
    }
    public void listarProdutos() {
        if(produtos.vazio())
            System.out.println("A lista de produtos está vazia!");
        else 
            produtos.listar();
    }
    public void adicionarAoCarrinho() {
        System.out.printf("ID do produto = ");
        int idProduto = sc.nextInt();
        Produto produto = produtos.buscarPorId(idProduto);
        if(produto == null) 
            System.out.println("Produto não encontrado.");
        else 
        {
            System.out.printf("Quantidade = ");
            int quantidade = sc.nextInt();
            carrinho.adicionarItem(produto, quantidade);
        }
    }
    public void removerDoCarrinho() {
        System.out.printf("ID do produto = ");
        int idProduto = sc.nextInt();
        if(carrinho.removerItem(idProduto))
            System.out.println("Item removido com sucesso.");
        else 
            System.out.println("Item não encontrado.");
    }
    public void verCarrinho() {
        if(carrinho.estaVazio())
            System.out.println("Carrinho está vazio.");
        else
            carrinho.listarItens();
    }
    public ArrayList<ItemPedido> setupFinalizarCompra(){
        ArrayList<ItemPedido> itens = new ArrayList<>();
        if(!carrinho.estaVazio()) 
        {
            for(ItemCarrinho item : carrinho.getItens()) {
                Produto produto = item.getProduto();
                int quantidade = item.getQuantidade();
                itens.add(new ItemPedido(produto, quantidade));
            }
            return itens;
        }
        else  
            return null;
    }
    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }
    public void verPedidos(){
        for(Pedido pedido : pedidos) {
            System.out.println(pedido);
            System.out.println("=======================");
        }
    }
}
