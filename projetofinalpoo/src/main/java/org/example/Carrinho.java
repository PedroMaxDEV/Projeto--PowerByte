package org.example;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private List<ItemCarrinho> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }
    public void adicionarItem(Produto produto, int quantidade){

    }
    public void removerItem(int idProduto){

    }
    public double calcularTotal(){
        return 2.0;
    }
    public void listarItens(){
        for(ItemCarrinho item : itens){
            System.out.println(item);
            System.out.println("=======================");
        }
    }
    public void limpar(){

    }
    public boolean estaVazio(){
        return true;
    }

}
