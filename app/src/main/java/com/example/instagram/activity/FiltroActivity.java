package com.example.instagram.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityFiltroBinding;
import com.example.instagram.fragments.EdicaoImagemFragment;
import com.example.instagram.fragments.ImagemFiltrosFragment;
import com.example.instagram.model.Feed;
import com.example.instagram.model.Postagens;
import com.example.instagram.model.Usuario;
import com.example.instagram.tools.DialogCarregamento;
import com.example.instagram.tools.FirebaseInstance;
import com.example.instagram.tools.UsuarioFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FiltroActivity extends AppCompatActivity {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    float x, y, dx, dy;
    private ActivityFiltroBinding binding;
    private Bitmap bitmaprecebido;
    private Bundle bundle;
    private float scale = 1f;
    private float scalex, scaley;
    private ScaleGestureDetector zoom;
    private Usuario usuarioLogado;
    private List<Usuario> listSeguidores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFiltroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        carregarLogado();

        setSupportActionBar(binding.toolbar.toollBarPrincipal);
        binding.toolbar.imageToolbar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Filtros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);

        recuperarIntent();


        selecionarEfeito();
        botaoClicadoFiltros();
        ImagemFiltrosFragment imagemFiltrosFragment = new ImagemFiltrosFragment();
        FragmentManager fr = getSupportFragmentManager();
        FragmentTransaction ft = fr.beginTransaction();
        imagemFiltrosFragment.setArguments(bundle);
        ft.replace(R.id.frameFragment, imagemFiltrosFragment);
        ft.commit();


        scalex = binding.imageFiltro.getScaleX();
        scaley = binding.imageFiltro.getScaleY();
        zoom = new ScaleGestureDetector(this, new zoom());


    }

    //recupera dados da recebidas do PostagemFragment
    private void recuperarIntent() {
        bitmaprecebido = null;
        bundle = getIntent().getExtras();
        if (bundle.containsKey("camera")) {
            byte[] bytes = bundle.getByteArray("camera");
            bitmaprecebido = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.imageFiltro.setImageBitmap(bitmaprecebido);

        } else if (bundle.containsKey("galeria")) {
            byte[] bytes = bundle.getByteArray("galeria");
            bitmaprecebido = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.imageFiltro.setImageBitmap(bitmaprecebido);
        }

    }

    //selecionar tipo de fragment
    public void selecionarEfeito() {

        binding.buttonOutrosEfeitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoClicadoOutros();
                EdicaoImagemFragment edicaoImagemFragment = new EdicaoImagemFragment();
                FragmentManager fr = getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                edicaoImagemFragment.setArguments(bundle);
                ft.replace(R.id.frameFragment, edicaoImagemFragment);
                ft.commit();
            }
        });

        binding.buttonFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoClicadoFiltros();
                ImagemFiltrosFragment imagemFiltrosFragment = new ImagemFiltrosFragment();
                FragmentManager fr = getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                imagemFiltrosFragment.setArguments(bundle);
                ft.replace(R.id.frameFragment, imagemFiltrosFragment);
                ft.commit();
            }
        });
    }

    public void efeitos(Bitmap bitmap) {
        binding.imageFiltro.setImageBitmap(bitmap);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void filtros(Bitmap bitmap) {
        binding.imageFiltro.setImageBitmap(bitmap);
    }

    public void botaoClicadoFiltros() {
        binding.buttonOutrosEfeitos.setBackground(getDrawable(R.drawable.background_botao_filtros_nao_clicado));
        binding.buttonFiltros.setBackground(getDrawable(R.drawable.bacground_botao_filtros_clicado));
    }

    public void botaoClicadoOutros() {
        binding.buttonFiltros.setBackground(getDrawable(R.drawable.background_botao_filtros_nao_clicado));
        binding.buttonOutrosEfeitos.setBackground(getDrawable(R.drawable.bacground_botao_filtros_clicado));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_filtros, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postMenu:
                salvarPostagemFirebase();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zoom.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            dx = event.getX() - x;
            dy = event.getY() - y;

            binding.imageFiltro.setX(binding.imageFiltro.getX() + dx);
            binding.imageFiltro.setY(binding.imageFiltro.getY() + dy);
            x = event.getX();
            y = event.getY();

        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            binding.imageFiltro.setScaleY(scaley);
            binding.imageFiltro.setScaleX(scalex);
            binding.imageFiltro.setX(27);
            binding.imageFiltro.setY(140);
            scale = 1.f;
        }


        return true;
    }

    public void salvarPostagemFirebase() {
        DialogCarregamento.inflarDialog("Salvando Postagem", this);

        String descricao = binding.descricao.getText().toString();
        String aleatorio = UUID.randomUUID().toString();
        StorageReference storageReference = FirebaseInstance.instanceStorage().child("imagens").child(UsuarioFirebase.getFirebaseUser().getEmail())
                .child("postagens").child("postagem_" + aleatorio + ".jpg");

        BitmapDrawable drawable = (BitmapDrawable) binding.imageFiltro.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] bytes = baos.toByteArray();
        UploadTask task = storageReference.putBytes(bytes);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Postagens postagens = new Postagens();
                            Feed feed = new Feed();
                            for (Usuario usuario : listSeguidores) {
                                feed.setDescricaoPostagem(descricao);
                                feed.setUriPostagem(task.getResult().toString());
                                feed.setNomeUsuario(usuarioLogado.getNome());
                                feed.setFotoUsuario(usuarioLogado.getFoto());
                                feed.salvarFeed(usuario.getId());
                            }

                            postagens.setUrlPostagem(task.getResult().toString());
                            postagens.setDescricaoPostagem(descricao);
                            postagens.setPostagensAcumuladas(usuarioLogado.getPublicacoes() + 1);
                            postagens.salvarPostagem();
                            carregarLogado();
                            DialogCarregamento.cancelarDialog();
                            Toast toast = new Toast(getApplicationContext());
                            toast.setView(View.inflate(getApplicationContext(), R.layout.salvo_sucesso, null));
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            finish();
                        }
                    });
                } else {

                    DialogCarregamento.cancelarDialog();
                    Toast toast = new Toast(getApplicationContext());
                    toast.setView(View.inflate(getApplicationContext(), R.layout.erro_salvar, null));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

    }

    public void carregarLogado() {

        DatabaseReference databaseReferenceLogado = FirebaseInstance.instanceDatabase().child("usuarios").child(UsuarioFirebase.dadosUsuario().getId());
        databaseReferenceLogado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioLogado = snapshot.child("dadosUsuario").getValue(Usuario.class);
                if (listSeguidores == null) {
                    listSeguidores = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.child("seguidores").getChildren()) {
                        Usuario usuario = ds.getValue(Usuario.class);
                        usuario.setId(ds.getKey());
                        listSeguidores.add(usuario);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class zoom extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));

            binding.imageFiltro.setScaleX(scale);
            binding.imageFiltro.setScaleY(scale);


            return true;
        }


    }


}