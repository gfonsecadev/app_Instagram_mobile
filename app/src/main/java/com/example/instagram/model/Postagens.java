package com.example.instagram.model;

import com.example.instagram.tools.FirebaseInstance;
import com.example.instagram.tools.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

public class Postagens {
    private String urlPostagem,descricaoPostagem;
    private int postagensAcumuladas;


    public Postagens() {
    }


    public void salvarPostagem(){
        DatabaseReference reference= FirebaseInstance.instanceDatabase().child("usuarios")
                .child(UsuarioFirebase.dadosUsuario().getId()).child("postagens").push();
        reference.setValue(this);

        DatabaseReference post= FirebaseInstance.instanceDatabase().child("usuarios")
                .child(UsuarioFirebase.dadosUsuario().getId()).child("dadosUsuario").child("publicacoes");
        post.setValue(getPostagensAcumuladas());

    }

    public String getUrlPostagem() {
        return urlPostagem;
    }

    public int getPostagensAcumuladas() {
        return postagensAcumuladas;
    }

    public void setPostagensAcumuladas(int postagensAcumuladas) {
        this.postagensAcumuladas = postagensAcumuladas;
    }

    public void setUrlPostagem(String urlPostagem) {
        this.urlPostagem = urlPostagem;
    }



    public String getDescricaoPostagem() {
        return descricaoPostagem;
    }

    public void setDescricaoPostagem(String descricaoPostagem) {
        this.descricaoPostagem = descricaoPostagem;
    }
}
