package org.example;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int estoque;
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
    public String toString() {
        return nome + "{ID=" + id + ", preço=" + preco + ", estoque=" + estoque + "}";
    }
}
