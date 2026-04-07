package org.example;

public enum StatusPedido {
    PENDENTE("AGUARDANDO PAGAMENTO"),
    PAGO("PAGO"),
    ENVIADO("ENVIADO"),
    ENTREGUE("ENTREGUE"),
    CANCELADO("CANCELADO");
    private final String descricao;
    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }

}
