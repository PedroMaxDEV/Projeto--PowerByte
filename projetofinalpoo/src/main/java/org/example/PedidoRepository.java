package org.example;

import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private List<Pedido> pedidos;
    public PedidoRepository(){
        this.pedidos = new ArrayList<>();
    }
    public void salvar(Pedido pedido){
        pedidos.add(pedido);
    }
    public void listar(){
        for(int i = 0; i < pedidos.size(); i++)
            pedidos.get(i).exibirInfo();
    }   
    public boolean remover(int id){
        for(int i = 0; i < pedidos.size(); i++) {
            if(pedidos.get(i).getId() == id) {
                pedidos.remove(i);
                return true;
            }
        }
        return false;
    }
    public Pedido buscarPorId(int id){
        for(int i = 0; i < pedidos.size(); i++) {
            if(pedidos.get(i).getId() == id) 
                return pedidos.get(i);
        }
        return null;
    }
    public Pedido buscarPorUsername(String username){
        for(int i = 0; i < pedidos.size(); i++) {
            if(pedidos.get(i).getUsername().equals(username)) 
                return pedidos.get(i);
        }
        return null;
    }
    
}
