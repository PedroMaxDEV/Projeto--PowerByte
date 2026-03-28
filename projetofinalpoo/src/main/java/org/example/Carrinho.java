package org.example;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private List<ItemCarrinho> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }
    public void adicionarItem(Produto produto, int quantidade){
        itens.add(new ItemCarrinho(produto, quantidade));
    }
    public void removerItem(int idProduto){
        boolean condition = true;
        for(int i = 0; i < itens.size(); i++) {
            if(itens.get(i).getProduto().getId() == idProduto) {
                condition = false;
                itens.remove(i);
                System.out.println("Item removido");
                break;
            }
        }
        if(condition) 
            System.out.println("Item não foi encontrado.");
    }
    public double calcularTotal(){
        double total = 0; 
        for(int i = 0; i < itens.size(); i++)
            total += itens.get(i).calcularSubtotal();
        return total;
    }
    public void listarItens(){
        for(ItemCarrinho item : itens){
            System.out.println(item);
            System.out.println("=======================");
        }
    }
    public void limpar(){
        while(itens.size() != 0)
            itens.remove(0);
    }
    public boolean estaVazio(){
        return itens.size() != 0;
    }

}
