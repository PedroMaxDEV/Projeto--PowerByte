package com.ecommerce.controller;

import com.ecommerce.dto.PedidoItemRequest;
import com.ecommerce.dto.PedidoRequest;
import com.ecommerce.model.ItemPedido;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Produto;
import com.ecommerce.model.Usuario;
import com.ecommerce.repository.PedidoRepository;
import com.ecommerce.repository.ProdutoRepository;
import com.ecommerce.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            UsuarioRepository usuarioRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody PedidoRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuarioOpt.get());
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("REALIZADO");

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PedidoItemRequest itemReq : request.getItens()) {
            Optional<Produto> produtoOpt = produtoRepository.findById(itemReq.getProdutoId());

            if (produtoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Produto não encontrado: " + itemReq.getProdutoId());
            }

            Produto produto = produtoOpt.get();

            if (produto.getEstoque() < itemReq.getQuantidade()) {
                return ResponseEntity.badRequest().body("Estoque insuficiente para: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - itemReq.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemReq.getQuantidade());
            item.setPreco(produto.getPreco());

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemReq.getQuantidade()));
            total = total.add(subtotal);

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPorUsuario(@PathVariable Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
}