package org.example;

import java.time.LocalDate;

public abstract class Usuario {
    protected int id;
    protected String nomeCompleto;
    protected String username;
    protected String senha;
    protected String tipo;
    protected String cpf;
    protected LocalDate dataDeNascimento;

    public Usuario(int id, String nomeCompleto, String cpf, LocalDate dataDeNascimento, String username, String senha) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataDeNascimento = dataDeNascimento;
        this.username = username;
        this.senha = senha;
    }
    public Usuario() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public boolean autenticarSenha(String senha) {
        return this.senha.equals(senha);
    }
    public String getTipo() {
        return tipo;
    }
    public void exibirInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nome completo: " + nomeCompleto);
        System.out.println("CPF: " + cpf);
        System.out.println("Data de nascimento: " + dataDeNascimento);
        System.out.println("Username: " + username);
    }
}
