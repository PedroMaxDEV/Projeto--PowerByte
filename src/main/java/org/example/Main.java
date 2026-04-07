package org.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

/**
 * Main — Ponto de entrada do sistema PowerByte.
 *
 * Integrações com banco de dados:
 *  1. DatabaseManager abre/cria o powerbyte.db e semeia produtos iniciais
 *  2. Repositories recebem a conexão do banco via construtor
 *  3. Admin padrão criado apenas na primeira execução
 *  4. idPedido inicializado a partir do MAX(id) do banco (Bug Fix #1)
 *  5. Criar.inicializarContadores() sincroniza IDs de clientes com o banco (Bug Fix #1)
 *  6. Finalizar compra decrementa estoque no banco
 *  7. Pedidos salvos em transação atômica COMMIT/ROLLBACK
 *  8. Histórico de pedidos carregado do banco ao fazer login
 *  9. Relatório de vendas com SQL de agregação para o Admin
 * 10. Conexão com banco fechada corretamente ao sair
 */
public class Main {
    public static void main(String[] args) {
        int comando;
        String senha;
        String username;
        Scanner sc = new Scanner(System.in);
        Usuario usuario;
        Cliente cliente;
        Pedido pedido;
        Admin admin;

        // ── Banco de dados ────────────────────────────────────────────────────
        DatabaseManager db = new DatabaseManager();
        UsuarioRepository usuarios = new UsuarioRepository(db.getConexao());
        ProdutoRepository produtos = new ProdutoRepository(db.getConexao());
        PedidoRepository  pedidos  = new PedidoRepository(db.getConexao());

        // Bug Fix #1 — Inicializa idPedido a partir do MAX(id) do banco
        // Sem isso, cada reinicialização do programa começava em 0 e colidia com pedidos já existentes
        int idPedido = pedidos.proximoId();

        // Bug Fix #1 — Sincroniza contadores de ID de cliente/produto com o banco
        Criar.inicializarContadores(db.getConexao());

        // Admin padrão — criado apenas se não existir no banco
        if (usuarios.buscarPorUsername("Alvaro") == null) {
            usuarios.salvar(new Admin(0, "José Álvaro", "000.000.000-00", LocalDate.of(2000, 1, 1), "Alvaro", "senha"));
        }

        // ── Loop principal ────────────────────────────────────────────────────
        while (true) {
            Menus.inicio();
            comando = Criar.entrada();

            if (comando == 1) {
                // ── Login ─────────────────────────────────────────────────────
                System.out.printf("Username = ");
                username = sc.nextLine();
                System.out.printf("Senha = ");
                senha = sc.nextLine();
                usuario = usuarios.buscarPorUsername(username);

                if (usuario == null || !usuario.autenticarSenha(senha)) {
                    System.out.println("Username ou senha inválido(s).");
                } else {

                    if (usuario.tipo.equals("Admin")) {
                        // ── Sessão Admin ──────────────────────────────────────
                        admin = (Admin) usuario;
                        admin.setUsuarios(usuarios);
                        admin.setPedidos(pedidos);
                        admin.setProdutos(produtos);
                        System.out.printf("Bem-vindo, admin %s!%n", admin.getUsername());

                        while (true) {
                            Menus.admin();
                            comando = Criar.entrada();
                            if      (comando == 1) admin.cadastrarProduto();
                            else if (comando == 2) admin.listarProdutos();
                            else if (comando == 3) admin.removerProduto();
                            else if (comando == 4) admin.atualizarProduto();
                            else if (comando == 5) admin.listarUsuarios();
                            else if (comando == 6) admin.listarPedidos();
                            else if (comando == 7) admin.atualizarStatusPedido();
                            else if (comando == 8) admin.relatorioVendas();   
                            else if (comando == 9) admin.removerUsuario();
                            else if (comando == 0) break;
                            else System.out.println("Comando inválido. Tente novamente.");
                        }

                    } else {
                        // ── Sessão Cliente ────────────────────────────────────
                        cliente = (Cliente) usuario;
                        cliente.setProdutos(produtos);
                        System.out.printf("Bem-vindo, %s!%n", cliente.getUsername());

                        // #7 — Carrega histórico de pedidos do banco ao fazer login
                        ArrayList<Pedido> historico = pedidos.buscarTodosPorUsername(cliente.getUsername());
                        for (Pedido p : historico) {
                            p.setRepository(pedidos);
                            cliente.adicionarPedido(p);
                        }
                        if (!historico.isEmpty())
                            System.out.printf("  (%d pedido(s) carregado(s) do histórico)%n", historico.size());

                        while (true) {
                            Menus.cliente();
                            comando = Criar.entrada();
                            if      (comando == 1) cliente.listarProdutos();
                            else if (comando == 2) cliente.adicionarAoCarrinho();
                            else if (comando == 3) cliente.verCarrinho();
                            else if (comando == 4) cliente.removerDoCarrinho();
                            else if (comando == 5) {
                                // ── Finalizar compra ──────────────────────────
                                ArrayList<ItemPedido> itens = cliente.setupFinalizarCompra();
                                if (itens == null) {
                                    System.out.println("Carrinho está vazio!");
                                } else {
                                    pedido = new Pedido(idPedido++, cliente, itens);
                                    pedido.setRepository(pedidos);

                                    // #1 — Decrementa estoque de cada item no banco
                                    boolean estoqueOk = true;
                                    for (ItemPedido item : pedido.getItens()) {
                                        boolean ok = produtos.decrementarEstoque(
                                            item.getProduto().getId(),
                                            item.getQuantidade()
                                        );
                                        if (!ok) { estoqueOk = false; break; }
                                    }

                                    if (!estoqueOk) {
                                        System.out.println("Compra cancelada: estoque insuficiente para um ou mais itens.");
                                    } else {
                                        // #4 — Pedido salvo em transação (COMMIT/ROLLBACK)
                                        pedidos.salvar(pedido);
                                        cliente.adicionarPedido(pedido);
                                        System.out.println("Compra finalizada! Estoque e pedido atualizados no banco.");
                                    }
                                }
                            }
                            else if (comando == 6) cliente.verPedidos();
                            else if (comando == 0) break;
                            else System.out.println("Comando inválido. Tente novamente.");
                        }
                    }
                }

            } else if (comando == 2) {
                // ── Cadastro de novo cliente ───────────────────────────────────
                Cliente novoCliente = Criar.cliente();
                usuarios.salvar(novoCliente);
                System.out.println("Usuário cadastrado com sucesso!");

            } else if (comando == 0) {
                System.out.println("Encerrando programa...");
                db.fechar();
                System.out.println("Programa encerrado.");
                break;

            } else {
                System.out.println("Comando inválido. Tente novamente.");
            }
        }

        sc.close();
    }
}
