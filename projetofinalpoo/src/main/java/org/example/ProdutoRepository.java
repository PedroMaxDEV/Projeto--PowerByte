package org.example;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    private List<Produto> produtos;
    public ProdutoRepository() {
        this.produtos = new ArrayList<>();
    }
    public void salvar(Produto produto){
        produtos.add(produto);
    }
    public void listar(){
        for(int i = 0; i < produtos.size(); i++) 
            System.out.println(produtos.get(i).toString());      
    }
    public boolean remover(int id){
        for(int i = 0; i < produtos.size(); i++) {
            if(produtos.get(i).getId() == id) {
                produtos.remove(i);
                return true;
            }
        }
        return false;
    }
    public Produto buscarPorId(int id){
        for(int i = 0; i < produtos.size(); i++) {
            if(produtos.get(i).getId() == id) 
                return produtos.get(i);
        }
        return null;
    }
    public Produto buscarPorNome(String nome){
        for(int i = 0; i < produtos.size(); i++) {
            if(produtos.get(i).getNome().equals(nome))
                return produtos.get(i);
        }   
        return null;
    }
}
