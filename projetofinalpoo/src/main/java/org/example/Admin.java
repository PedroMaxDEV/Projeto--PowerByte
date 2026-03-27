package org.example;

import java.time.LocalDate;

public class Admin extends Usuario{
    public Admin(int id, String nomeCompleto, String cpf, LocalDate dataDeNascimento, String username, String senha) {
        super(id, nomeCompleto, cpf, dataDeNascimento, username, senha);
    }

    @Override
    public void exibirInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nome completo: " + nomeCompleto);
        System.out.println("CPF: " + cpf);
        System.out.println("Data de nascimento: " + dataDeNascimento);
        System.out.println("Username: " + username);

    }
    @Override
    public boolean autenticarSenha(String senha) {
        return true;
    }
    public void cadastrarProduto(){

    }
    public void removerProduto(int id){

    }
    public void atualizarProduto(){

    }
    public void listarCliente(){

    }
    public void listarPedidos(){

    }
    public void atualizarStatusPedido(){

    }

}
