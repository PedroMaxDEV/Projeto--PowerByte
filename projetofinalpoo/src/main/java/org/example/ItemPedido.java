package org.example;

public class ItemPedido {
    private Produto produto;
    private int quantidade;
    private double precoUnitario;
    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        precoUnitario = produto.getPreco();
    }
    public double calcularSubtotal() {
        return quantidade * precoUnitario;
    }
    public void exibirInfo() {
        System.out.println(produto.toString());
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Preço unitário: " + precoUnitario);
    }
}
