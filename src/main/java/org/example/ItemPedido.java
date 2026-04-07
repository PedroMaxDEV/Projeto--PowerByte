wpackage org.example;

/**
 * ItemPedido — Representa um produto e sua quantidade dentro de um pedido.
 * O preço unitário é fixado no momento da compra (histórico).
 */
public class ItemPedido {
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
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
