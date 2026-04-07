package org.example;
import java.util.Scanner;
public class Produto {
    private int id;
    private int estoque;
    private String nome;
    private double preco;
    private Scanner sc = new Scanner(System.in);
    public Produto(int id, String nome, double preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getPreco() {
       return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
    public int getEstoque() {
        return estoque;
    }
    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
    public void atualizar() {
        while(true) {
            System.out.println("Reduzir ou repor estoque?");
            System.out.println("1 - Reduzir");
            System.out.println("2 - Repor");
            System.out.println("3 - Alterar preço");
            int comando = sc.nextInt();
            if(comando == 1) {
                System.out.printf("Quantidade = ");
                int qtd = sc.nextInt();
                reduzirEstoque(qtd);
                break;
            } else if(comando == 2) {
                System.out.printf("Quantidade = ");
                int qtd = sc.nextInt();
                reporEstoque(qtd);
                break;
            } else if(comando == 3) {
                System.out.printf("Preço = ");
                preco = sc.nextDouble();
                break;
            } else {
                System.out.println("Comando inválido. Tente novamente.");
            }
        }
    }
    public void reduzirEstoque(int qtd){
        if(estoque - qtd < 0) 
            System.out.println("Estoque não pode ser reduzido a essa quantidade."); 
        else {
            estoque -= qtd;
            System.out.println("Estoque reduzido");
        }
    }
    public void reporEstoque(int qtd){
        estoque += qtd;
        System.out.println("Estoque reposto");
    }
    public boolean temEstoque(int qtd){
        return qtd >= 0;
    }
    public void exibirInfo() {
        System.out.println(nome);
        System.out.printf("ID = %d\n", id);
        System.out.printf("Preço = %.2f\n", preco);
        System.out.printf("Estoque = %d\n", estoque);
    }
}
