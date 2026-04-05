package org.example;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private List<Usuario> usuarios;
    public UsuarioRepository() {
        this.usuarios = new ArrayList<>();
    }
    public void salvar(Usuario usuario){
        usuarios.add(usuario);
    }
    public void listar(){
        for(int i = 0; i < usuarios.size(); i++) {
            if(usuarios.get(i).getTipo().equals("Cliente"))
                usuarios.get(i).exibirInfo();
        }
    }
    public boolean remover(int id){
        for(int i = 0; i < usuarios.size(); i++) {
            if(usuarios.get(i).getId() == id) {
                usuarios.remove(i);
                return true;
            }
        }
        return false;
    }
    public Usuario buscarPorId(int id) { 
        for(int i = 0; i < usuarios.size(); i++) {
            if(usuarios.get(i).getId() == id) 
                return usuarios.get(i);
        }
        return null;
    }
    public Usuario buscarPorUsernameESenha(String username, String senha){
        for(int i = 0; i < usuarios.size(); i++) {
            if(usuarios.get(i).getUsername().equals(username) && usuarios.get(i).getSenha().equals(senha))
                return usuarios.get(i);
        }
        return null;
    }
}
