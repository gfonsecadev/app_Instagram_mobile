package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityLoginBinding;
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
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //parar execução de tarefa atual por 1segundo e meio para exibição da splashScreem
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_LoginCadastro);
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        verificarUsuarioLogado();
        binding.textCadastreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goIntentCadastrar();
            }
        });

        binding.buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });

    }


    public void goIntentCadastrar(){
        startActivity(new Intent(this, CadastroActivity.class));
    }
    //metódo para logar e validar campos
    public void entrar(){
        String email=binding.editEmail.getText().toString();
        String senha=binding.editSenha.getText().toString();

            if(!email.equals("")){
                if(!senha.equals("")){
                    Usuario usuario=new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    loginFirebase(usuario);


                }else {
                    binding.editSenha.requestFocus();
                    binding.editSenha.setError("Digite uma senha");

                }
            }else {
                binding.editEmail.requestFocus();
                binding.editEmail.setError("Digite um email");

            }
        }

    //metódo para logar no firebaseUser
    public void loginFirebase(Usuario usuario){
        binding.progress.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth= FirebaseInstance.instanceFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Bem vindo: "
                            + UsuarioFirebase.getFirebaseUser().getDisplayName(),Toast.LENGTH_LONG ).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                }else  {
                    binding.progress.setVisibility(View.GONE);
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

    //metódo que verifica se o usuário atual está logado
    public void verificarUsuarioLogado(){
        FirebaseAuth firebaseAuth=FirebaseInstance.instanceFirebaseAuth();
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

    }
}