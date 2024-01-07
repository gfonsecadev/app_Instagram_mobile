package com.example.instagram.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;

import com.example.instagram.databinding.ActivityEditarPerfilBinding;
import com.example.instagram.model.Usuario;
import com.example.instagram.tools.FirebaseInstance;
import com.example.instagram.tools.UsuarioFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditarPerfilActivity extends AppCompatActivity {
    private ActivityEditarPerfilBinding binding;
    private Usuario usuario;
    private  Uri uriGlobal;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usuario=UsuarioFirebase.dadosUsuario();
        uriGlobal= Uri.parse(usuario.getFoto());
        recuperarDados();
        binding.toolbarPerfilEditar.setTitle("");
        setSupportActionBar(binding.toolbarPerfilEditar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);

        //chamado de metodos para salvar edição
        binding.buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome=binding.editNomeUsuario.getText().toString();

                if(usuario.getNome().equals(nome) && usuario.getFoto().equals(uriGlobal.toString())){
                    Toast toast=new Toast(getApplicationContext());
                    toast.setView(View.inflate(getApplicationContext(),R.layout.nenhuma_alteracao_feita,null));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                }else {
                    usuario.setNome(nome);
                    usuario.setFoto(String.valueOf(uriGlobal));
                    UsuarioFirebase.salvarProfile(usuario,binding.layoutProgress,EditarPerfilActivity.this);

                }


            }
        });
        //metódo para escolher foto da galeria
        binding.escolherFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFoto();
            }
        });
    }
    //metódo subscrito para o botão voltar da toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    //metódo para recuperar dados amigo
    public void recuperarDados(){


        if(UsuarioFirebase.getFirebaseUser().getPhotoUrl()!=null){
            Glide.with(this).load(UsuarioFirebase.getFirebaseUser().getPhotoUrl()).into(binding.imagePerfilEditar);
        }else binding.imagePerfilEditar.setImageResource(R.drawable.padrao);

        binding.editNomeUsuario.setText(usuario.getNome());
        binding.editEmailUsuario.setText(usuario.getEmail());
        binding.editEmailUsuario.setFocusable(false);
    }

    //metódo para abrir intent da galeria
    public void escolherFoto(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager())!=null){
            galeria.launch(intent);
        }
    }

    //recuerar dados da intent
    ActivityResultLauncher galeria=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK && result.getData()!=null){
                uriGlobal=result.getData().getData();
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriGlobal);

                    salvarFotoPerfilStorage(uriGlobal,binding.layoutProgress);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        //metódo para salvar foto no storage do firebase
        public  void salvarFotoPerfilStorage(Uri uri, LinearLayout progressBar){
            progressBar.setVisibility(View.VISIBLE);


            StorageReference storageReference= FirebaseInstance.instanceStorage().child("imagens").child(UsuarioFirebase.getFirebaseUser().getEmail())
                    .child("perfil").child("perfil.jpg");

            UploadTask uploadTask=storageReference.putFile(uri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    binding.buttonSalvar.setVisibility(View.GONE);
                    binding.escolherFoto.setVisibility(View.GONE);
                    binding.editNomeUsuario.setFocusable(false);
                    binding.progressEditarPerfilHorizontal.setVisibility(View.VISIBLE);
                    binding.progressEditarPerfilHorizontal.setProgress((int) snapshot.getBytesTransferred());
                    binding.progressEditarPerfilHorizontal.setMax((int) snapshot.getTotalByteCount());
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                uriGlobal=task.getResult();
                                progressBar.setVisibility(View.GONE);
                                binding.imagePerfilEditar.setImageBitmap(bitmap);
                                binding.progressEditarPerfilHorizontal.setVisibility(View.GONE);
                                binding.editNomeUsuario.setFocusableInTouchMode(true);
                                binding.buttonSalvar.setVisibility(View.VISIBLE);
                                binding.escolherFoto.setVisibility(View.VISIBLE);
                            }else {
                                progressBar.setVisibility(View.GONE);
                                binding.editNomeUsuario.setFocusableInTouchMode(true);
                                binding.buttonSalvar.setVisibility(View.VISIBLE);
                                binding.escolherFoto.setVisibility(View.VISIBLE);
                                Toast toast=new Toast(progressBar.getContext());
                                toast.setView(View.inflate(progressBar.getContext(),R.layout.erro_salvar,null));
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                binding.imagePerfilEditar.setImageResource(R.drawable.padrao);
                            }

                        }
                    });
                }
            });

        }


    });



}