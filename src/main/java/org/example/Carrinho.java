package org.example;
import java.util.ArrayList;
public class Carrinho {
    private ArrayList<ItemCarrinho> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }
    public ArrayList<ItemCarrinho> getItens() {
        return itens;
    }
    public void adicionarItem(Produto produto, int quantidade){
        itens.add(new ItemCarrinho(produto, quantidade));
    }
    public boolean removerItem(int idProduto){
        for(int i = 0; i < itens.size(); i++) {
            if(itens.get(i).getProduto().getId() == idProduto) {
                itens.remove(i);
                return true;
            }
        }
        return false;
    }
    public double calcularTotal(){
        double total = 0; 
        for(int i = 0; i < itens.size(); i++)
            total += itens.get(i).calcularSubtotal();
        return total;
    }
    public void listarItens(){
        for(ItemCarrinho item : itens){
            item.exibirInfo();
            System.out.println("=======================");
        }
    }
    public void limpar(){
        while(itens.size() != 0)
            itens.remove(0);
    }
    public boolean estaVazio(){
        return itens.size() == 0;
    }

}
