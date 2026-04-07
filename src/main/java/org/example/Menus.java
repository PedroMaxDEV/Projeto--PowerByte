package org.example;
public class Menus {
    public static void inicio() {
        System.out.println("1 - Entrar");
        System.out.println("2 - Criar conta");
        System.out.println("0 - Sair");
    }
    public static void admin() {
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Remover produto");
        System.out.println("4 - Atualizar produto");
        System.out.println("5 - Listar usuários");
        System.out.println("6 - Listar pedidos");
        System.out.println("7 - Atualizar status do pedido");
        System.out.println("8 - Relatório de vendas");
        System.out.println("9 - Remover usuário");
        System.out.println("0 - Sair");
    }
    public static void cliente() {
        System.out.println("1 - Listar produtos");
        System.out.println("2 - Adicionar produtos ao carrinho");
        System.out.println("3 - Ver carrinho");
        System.out.println("4 - Remover item do carrinho");
        System.out.println("5 - Finalizar compra");
        System.out.println("6 - Ver meus pedidos");
        System.out.println("0 - Sair");
    }
}