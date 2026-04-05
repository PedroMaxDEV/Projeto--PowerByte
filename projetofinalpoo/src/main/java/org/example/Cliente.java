package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Cliente extends Usuario{
    private Carrinho carrinho;
    protected List<Pedido> pedidos;
    public Cliente(int id, String nomeCompleto, String cpf, LocalDate dataDeNascimento, String username, String senha) {
       super(id,nomeCompleto, cpf, dataDeNascimento, username, senha);
       tipo = "Cliente";
       this.pedidos = new ArrayList<>();
    }
    @Override
    public void exibirInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nome completo: " + nomeCompleto);
        System.out.println("CPF: " + cpf);
        System.out.println("Data de nascimento: " + dataDeNascimento);
        System.out.println("Username: " + username);
    }
    public void adicionarAoCarrinho(Produto produto, int quantidade){
        carrinho.adicionarItem(produto, quantidade);
    }
    public void removerDoCarrinho(int idProduto){
        carrinho.removerItem(idProduto);
    }
    public Pedido finalizarCompra(){
        return null;
    }
    public void listarPedido(){
        for(Pedido pedido: pedidos){
            System.out.println(pedido);
            System.out.println("=======================");
        }
    }
}
