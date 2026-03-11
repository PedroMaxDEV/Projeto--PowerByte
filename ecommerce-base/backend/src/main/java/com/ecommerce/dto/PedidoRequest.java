package com.ecommerce.dto;

import java.util.List;

public class PedidoRequest {
    private Long usuarioId;
    private List<PedidoItemRequest> itens;

    public PedidoRequest() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<PedidoItemRequest> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItemRequest> itens) {
        this.itens = itens;
    }
}