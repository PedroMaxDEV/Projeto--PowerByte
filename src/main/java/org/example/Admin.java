package org.example;
import java.time.LocalDate;
import java.util.Scanner;
public class Admin extends Usuario{
    private UsuarioRepository usuarios;
    private PedidoRepository pedidos;
    private ProdutoRepository produtos;
    private Scanner sc = new Scanner(System.in);
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
    @Override
    public void exibirInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nome completo: " + nomeCompleto);
        System.out.println("CPF: " + cpf);
        System.out.println("Data de nascimento: " + dataDeNascimento);
        System.out.println("Username: " + username);

    }
    public void cadastrarProduto() { 
        produtos.salvar(Criar.produto());
    }
    public void removerProduto(){
        System.out.printf("Id do produto = ");
        int id = sc.nextInt();
        if(produtos.remover(id))
            System.out.println("Produto removido com sucesso!");
        else 
            System.out.println("Produto não encontrado.");
    }
    public void atualizarProduto() {
        System.out.printf("Id do produto = ");
        int id = sc.nextInt();
        Produto produto = produtos.buscarPorId(id);
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
        int id = sc.nextInt();
        Pedido pedido = pedidos.buscarPorId(id);
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