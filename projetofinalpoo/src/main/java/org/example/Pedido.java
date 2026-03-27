package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private double total;
    private StatusPedido status;
    private LocalDate data;

    public Pedido(int id, Cliente cliente, List<ItemPedido> itens) {
        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>(itens);
        this.total = calcularTotal();
        this.status = StatusPedido.PENDENTE;
        this.data = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public double getTotal() {
        return total;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDate getData() {
        return data;
    }
    public double calcularTotal() {
        return 2.0;
    }
    public void autalizarStatus( StatusPedido novoStatus){

    }

}
