package com.example.instagram.tools;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.instagram.R;
import com.example.instagram.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.AbstractList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioFirebase {
    //retorna objeto FirebaseUser do usuario logado
    public static FirebaseUser getFirebaseUser(){
        FirebaseAuth firebaseAuth=FirebaseInstance.instanceFirebaseAuth();
        return firebaseAuth.getCurrentUser();
    }

    //salva dados do usuário no FirebaseAuth como nome e foto sem necessidade de salvar no Database
    public static void salvarProfile(Usuario usuario, LinearLayout progressBar, Activity context){
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser firebaseUser=getFirebaseUser();
        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(usuario.getNome()).setPhotoUri(Uri.parse(usuario.getFoto())).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    usuario.atualizarDadosUsuario();
                    Toast toast=new Toast(progressBar.getContext());
                    toast.setView(View.inflate(progressBar.getContext(),R.layout.salvo_sucesso,null));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    progressBar.setVisibility(View.INVISIBLE);
                    if(context!=null){
                    context.finish();}
                }else {
                    Toast toast=new Toast(progressBar.getContext());
                    toast.setView(View.inflate(progressBar.getContext(),R.layout.erro_salvar,null));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //retorna dados do usuário com base no FirebaseUser
    public static Usuario dadosUsuario(){
        Usuario usuario=new Usuario();
        usuario.setId(getFirebaseUser().getUid());
        usuario.setNome(getFirebaseUser().getDisplayName());
        usuario.setEmail(getFirebaseUser().getEmail());
        if (getFirebaseUser().getPhotoUrl()!=null){
            usuario.setFoto(getFirebaseUser().getPhotoUrl().toString());
        }else usuario.setFoto("");

        return usuario;
    }



}
