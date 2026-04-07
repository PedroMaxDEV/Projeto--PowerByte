package org.example;
import java.time.LocalDate;
public class Admin extends Usuario{
    private UsuarioRepository usuarios;
    private PedidoRepository pedidos;
    private ProdutoRepository produtos;
    public Admin(int id, String nomeCompleto, String cpf, LocalDate dataDeNascimento, String username, String senha) {
        super(id, nomeCompleto, cpf, dataDeNascimento, username, senha);
        tipo = "Admin";
    }
    public void setUsuarios(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }
    public void setPedidos(PedidoRepository pedidos) {
        this.pedidos = pedidos;
    }
    public void setProdutos(ProdutoRepository produtos) {
        this.produtos = produtos;
    }
    public void cadastrarProduto() { 
        produtos.salvar(Criar.produto());
    }
    public void removerProduto(){
        System.out.printf("Id do produto = ");
        int idProduto = Criar.entradaInt();
        if(produtos.remover(idProduto))
            System.out.println("Produto removido com sucesso!");
        else 
            System.out.println("Produto não encontrado.");
    }
    public void removerUsuario() {
        System.out.printf("Id do usuário = ");
        int idUsuario = Criar.entradaInt();
        if(usuarios.remover(idUsuario))
            System.out.println("Usuário removido com sucesso!");
        else 
            System.out.println("Usuário não encontrado.");
    }
    public void atualizarProduto() {
        System.out.printf("Id do produto = ");
        int idProduto = Criar.entradaInt();
        Produto produto = produtos.buscarPorId(idProduto);
        if(produto != null)     
            produto.atualizar();
        else 
            System.out.println("Produto não encontrado.");
    }
    public void listarUsuarios() {
        usuarios.listar();
    }
    public void listarPedidos() {
        pedidos.listar();
    }
    public void listarProdutos() {
        if(produtos.vazio())
            System.out.println("A lista de produtos está vazia!");
        else 
            produtos.listar();
    }
    public void atualizarStatusPedido() {
        System.out.printf("Id do pedido = ");
        int idPedido = Criar.entradaInt();
        Pedido pedido = pedidos.buscarPorId(idPedido);
        if (pedido != null) {
            pedido.setRepository(pedidos);
            pedido.atualizar();
        } else
            System.out.println("Pedido não encontrado.");
    }
    public void relatorioVendas() {
        pedidos.gerarRelatorio();
    }
}