package com.example.instagram.model;

import com.example.instagram.tools.FirebaseInstance;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Feed implements Serializable {
    private String uriPostagem,descricaoPostagem,fotoUsuario,nomeUsuario;

    public Feed() {
    }

    public String getUriPostagem() {
        return uriPostagem;
    }


    public void salvarFeed(String idSeguidor){
        DatabaseReference databaseReference= FirebaseInstance.instanceDatabase()
                .child("usuarios").child(idSeguidor).child("feeds").push();

        databaseReference.setValue(this);
    }

    public void setUriPostagem(String uriPostagem) {
        this.uriPostagem = uriPostagem;
    }

    public String getDescricaoPostagem() {
        return descricaoPostagem;
    }

    public void setDescricaoPostagem(String descricaoPostagem) {
        this.descricaoPostagem = descricaoPostagem;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
