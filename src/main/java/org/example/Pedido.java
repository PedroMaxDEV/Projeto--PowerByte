package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Pedido {
    private int id;
    private Cliente cliente;
    private ArrayList<ItemPedido> itens;
    private double total;
    private StatusPedido status;
    private LocalDate data;
    private Scanner sc = new Scanner(System.in);
    private PedidoRepository repository;

    public Pedido(int id, Cliente cliente, ArrayList<ItemPedido> itens) {
        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>(itens);
        this.total = calcularTotal();
        this.status = StatusPedido.PENDENTE;
        this.data = LocalDate.now();
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
    public String getUsername() {
        return cliente.getUsername();
    }
    public double calcularTotal() {
        double total = 0;
        for(int i = 0; i < itens.size(); i++) 
            total += itens.get(i).calcularSubtotal();
        return total;
    }
    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
    }

    public void setRepository(PedidoRepository repository) {
        this.repository = repository;
    }
    public void exibirInfo() {
        System.out.println("DADOS DO CLIENTE:");
        cliente.exibirInfo();
        System.out.println("==================");
        for(int i = 0; i < itens.size(); i++) {
            System.out.println("DADOS DO ITEM:");
            itens.get(i).exibirInfo();
            System.out.println("====================");
        }
        System.out.println("DADOS DO PEDIDO:");
        System.out.println("ID: " + id);
        System.out.println("Total: " + total);
        System.out.println("Status: " + status.getDescricao());
        System.out.println("Data: " + data);
    }
    public void atualizar() {
        System.out.printf("Status do pedido (PENDENTE, PAGO, ENVIADO, etc.) = ");
        String novo = sc.nextLine().toUpperCase().trim();
        try {
            status = StatusPedido.valueOf(novo);
            System.out.println("Status atualizado para: " + status.getDescricao());
            if (repository != null) 
                repository.atualizarStatus(this);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: Status inválido digitado.");
        }
    }

    @Override
    public String toString() {
        return "Pedido{ID=" + id +
               ", cliente='" + (cliente != null ? cliente.getUsername() : "?") + "'" +
               ", total=R$" + String.format("%.2f", total) +
               ", status='" + status.getDescricao() + "'" +
               ", data=" + data + "}";
    }
}
