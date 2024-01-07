package com.example.instagram.model;

import com.example.instagram.tools.FirebaseInstance;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;

public class Usuario implements Serializable {
    private String id,nome,email,senha,foto;
    private int publicacoes,seguidores,seguindo;

    public Usuario() {
    }

    //salva um nó no firebase com dados do usuário cadastrado
    public void salvar(){
        DatabaseReference databaseReference= FirebaseInstance.instanceDatabase().child("usuarios").child(id).child("dadosUsuario");
        databaseReference.setValue(this);

    }

    //atualiza dados do usuário cadastrado sem necessidade de subscrever informações não modificadas
    public void atualizarDadosUsuario(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",getId());
        hashMap.put("nome",getNome());
        hashMap.put("email",getEmail());
        hashMap.put("foto",getFoto());

        DatabaseReference databaseReference=FirebaseInstance.instanceDatabase().child("usuarios").child(id).child("dadosUsuario");
        databaseReference.updateChildren(hashMap);
    }

    public int getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(int publicacoes) {
        this.publicacoes = publicacoes;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
