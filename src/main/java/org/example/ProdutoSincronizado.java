package org.example;

import java.util.Scanner;

/**
 * ProdutoSincronizado — Subclasse de Produto que sincroniza mudanças com o banco de dados.
 *
 * Quando o Admin chama produto.atualizar() (reduzir estoque, repor, alterar preço),
 * esta classe intercepta a operação e persiste as mudanças no SQLite automaticamente,
 * sem exigir nenhuma mudança na lógica já existente de Admin ou Produto.
 *
 * Padrão usado: Decorator/Subclasse de sincronização — o restante do código
 * continua usando Produto normalmente, sem saber que está falando com o banco.
 */
public class ProdutoSincronizado extends Produto {

    private final ProdutoRepository repository;
    private final Scanner sc = new Scanner(System.in);

    public ProdutoSincronizado(int id, String nome, double preco, int estoque, ProdutoRepository repository) {
        super(id, nome, preco, estoque);
        this.repository = repository;
    }

    /**
     * Sobrescreve o método atualizar() para que, após a interação com o usuário,
     * as mudanças sejam salvas automaticamente no banco de dados.
     */
    @Override
    public void atualizar() {
        while (true) {
            System.out.println("Reduzir ou repor estoque?");
            System.out.println("1 - Reduzir");
            System.out.println("2 - Repor");
            System.out.println("3 - Alterar preço");
            int comando = sc.nextInt();
            if (comando == 1) {
                System.out.printf("Quantidade = ");
                int qtd = sc.nextInt();
                reduzirEstoque(qtd);
                break;
            } else if (comando == 2) {
                System.out.printf("Quantidade = ");
                int qtd = sc.nextInt();
                reporEstoque(qtd);
                break;
            } else if (comando == 3) {
                System.out.printf("Preço = ");
                setPreco(sc.nextDouble());
                break;
            } else {
                System.out.println("Comando inválido. Tente novamente.");
            }
        }
        // Após qualquer alteração, persiste no banco
        repository.atualizar(this);
        System.out.println("[DB] Produto atualizado no banco de dados.");
    }
}
