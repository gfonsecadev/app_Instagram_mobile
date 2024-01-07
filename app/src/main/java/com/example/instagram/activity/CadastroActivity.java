package com.example.instagram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityCadastroBinding;
import com.example.instagram.model.Usuario;
import com.example.instagram.tools.FirebaseInstance;
import com.example.instagram.tools.UsuarioFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

    }
    //método responsável por cadastrar usuários e validar os campos
    public void cadastrar(){
        String nome=binding.editNomeCadastro.getText().toString();
        String email=binding.editEmailCadastro.getText().toString();
        String senha=binding.editSenhaCadastro.getText().toString();

        if(!nome.equals("")){
            if(!email.equals("")){
                if(!senha.equals("")){
                    Usuario usuario=new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setFoto("");
                    usuario.setSenha(senha);
                    cadastroFirebase(usuario);


                }else {
                    binding.editSenhaCadastro.requestFocus();
                    binding.editSenhaCadastro.setError("Digite uma senha");

                }
            }else {
                binding.editEmailCadastro.requestFocus();
                binding.editEmailCadastro.setError("Digite um email");

            }
        }else {
            binding.editNomeCadastro.requestFocus();
            binding.editNomeCadastro.setError("Digite seu nome");

        }
    }
    //método para cadastro de usuário no firebaseAuth
    public void cadastroFirebase(Usuario usuario){
        FirebaseAuth firebaseAuth= FirebaseInstance.instanceFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    try {
                        usuario.setId(task.getResult().getUser().getUid());
                        usuario.salvar();
                        UsuarioFirebase.salvarProfile(usuario,binding.layoutProgressCadastro,null);
                        Toast.makeText(CadastroActivity.this,"Bem vindo: "+usuario.getNome(),Toast.LENGTH_LONG ).show();
                        startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    binding.progressCadastrar.setVisibility(View.GONE);
                    Toast toast=new Toast(getApplicationContext());
                    toast.setView(View.inflate(getApplicationContext(), R.layout.erro_salvar,null));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    String excessao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excessao = "Email não está cadastrado";

                    } catch (FirebaseAuthWeakPasswordException e) {
                        excessao = "Senha muito fraca, insira uma senha mais forte!";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excessao = "Este email não é válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excessao = "Este email já está em uso, digite outro!";

                    } catch (Exception e) {
                        excessao = e.getMessage();
                    }
                    Snackbar.make(binding.getRoot(), excessao, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}