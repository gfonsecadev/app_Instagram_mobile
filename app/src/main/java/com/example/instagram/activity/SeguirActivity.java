package com.example.instagram.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.adapters.AdapterGridPerfil;
import com.example.instagram.databinding.ActivitySeguirBinding;
import com.example.instagram.model.Postagens;
import com.example.instagram.model.Usuario;
import com.example.instagram.tools.FirebaseInstance;
import com.example.instagram.tools.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeguirActivity extends AppCompatActivity {
    private ActivitySeguirBinding binding;
    private Usuario usuarioRecebido;
    private Usuario usuarioRecebidoAtualizado;
    private Usuario usuarioLogado;
    private DatabaseReference databaseReferenceAmigo;
    private DatabaseReference databaseReferenceLogado;
    private List<String> listPublicacoes;
    private AdapterGridPerfil adapterGridPerfil;
    private ValueEventListener listenerAmigo;
    private ValueEventListener listenerLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeguirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        carregarIntent();
        recuperarDados();


    }


    //carregar intent recebida do SearchFragment
    public void carregarIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("usuarioProcurado")) {
            usuarioRecebido = (Usuario) bundle.getSerializable("usuarioProcurado");//para objetos usamos o serializable ou parcellable
        }

    }

    public void recuperarDados() {
        setSupportActionBar(binding.toolbarSeguir.toollBarPrincipal);
        binding.fragmentSeguir.textNomeUsuario.setText("");
        binding.toolbarSeguir.imageToolbar.setVisibility(View.GONE);
        getSupportActionBar().setTitle(usuarioRecebido.getNome());

        if (!usuarioRecebido.getFoto().equals("")) {
            Glide.with(this).load(usuarioRecebido.getFoto()).into(binding.fragmentSeguir.imagePerfil);
        } else {
            binding.fragmentSeguir.imagePerfil.setImageResource(R.drawable.padrao);
        }


    }

    //carrega informações do usuário recebido sempre atualizado e verifica se usuário logado é um seguidor
    public void carregarUsuario() {

        databaseReferenceAmigo = FirebaseInstance.instanceDatabase().child("usuarios").child(usuarioRecebido.getId());
        listenerAmigo = databaseReferenceAmigo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioRecebidoAtualizado = snapshot.child("dadosUsuario").getValue(Usuario.class);
                binding.fragmentSeguir.textPublicacoes.setText(String.valueOf(usuarioRecebidoAtualizado.getPublicacoes()));
                binding.fragmentSeguir.textSeguindo.setText(String.valueOf(usuarioRecebidoAtualizado.getSeguindo()));
                binding.fragmentSeguir.textSeguidores.setText(String.valueOf(usuarioRecebidoAtualizado.getSeguidores()));
                binding.fragmentSeguir.textViewsuasPublicacoes.setText("Publicações de " + usuarioRecebidoAtualizado.getNome());

                if (listPublicacoes == null) {
                    listPublicacoes = new ArrayList<>();
                    for (DataSnapshot db : snapshot.child("postagens").getChildren()) {
                        Postagens postagens = db.getValue(Postagens.class);
                        listPublicacoes.add(postagens.getUrlPostagem());
                    }
                    carregarGridView();
                }


                if (usuarioRecebidoAtualizado.getSeguidores() > 0) {
                    for (DataSnapshot ds : snapshot.child("seguidores").getChildren()) {
                        String chave = ds.getKey();
                        if (chave.equals(usuarioLogado.getId())) {
                            binding.fragmentSeguir.textEditarPerfil.setText("seguindo");
                            binding.fragmentSeguir.textEditarPerfil.setOnClickListener(null);
                            binding.fragmentSeguir.textEditarPerfil.setBackground(getDrawable(R.drawable.botao_sem_efeito_clique));
                        } else {
                            binding.fragmentSeguir.textEditarPerfil.setText("seguir");
                            binding.fragmentSeguir.textEditarPerfil.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seguirUsuario();
                                }
                            });
                        }

                    }
                } else {
                    binding.fragmentSeguir.textEditarPerfil.setText("seguir");
                    binding.fragmentSeguir.textEditarPerfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seguirUsuario();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //carrega informações do usuário logado sempre atualizadas
    public void carregarLogado() {

        databaseReferenceLogado = FirebaseInstance.instanceDatabase().child("usuarios").child(UsuarioFirebase.dadosUsuario().getId());
        listenerLogado = databaseReferenceLogado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioLogado = snapshot.child("dadosUsuario").getValue(Usuario.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        carregarLogado();
        carregarUsuario();


    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReferenceAmigo.removeEventListener(listenerAmigo);
        databaseReferenceLogado.removeEventListener(listenerLogado);
    }

    //metódo para seguir um usuário e atualizar informações no firebase
    private void seguirUsuario() {
        HashMap<String, Object> sequindo = new HashMap<>();
        sequindo.put("nome", usuarioRecebido.getNome());
        sequindo.put("foto", usuarioRecebido.getFoto());

        HashMap<String, Object> seguidor = new HashMap<>();
        seguidor.put("nome", usuarioLogado.getNome());
        seguidor.put("foto", usuarioLogado.getFoto());

        DatabaseReference logadoRef = FirebaseInstance.instanceDatabase().child("usuarios").child(usuarioLogado.getId()).child("seguindo").child(usuarioRecebido.getId());
        DatabaseReference amigoRef = FirebaseInstance.instanceDatabase().child("usuarios").child(usuarioRecebido.getId()).child("seguidores").child(usuarioLogado.getId());

        logadoRef.setValue(sequindo);
        amigoRef.setValue(seguidor);

        int totalSeguindo = usuarioLogado.getSeguindo() + 1;
        int totalSeguidor = usuarioRecebidoAtualizado.getSeguidores() + 1;

        logadoRef = FirebaseInstance.instanceDatabase().child("usuarios").child(usuarioLogado.getId()).child("dadosUsuario").child("seguindo");
        amigoRef = FirebaseInstance.instanceDatabase().child("usuarios").child(usuarioRecebido.getId()).child("dadosUsuario").child("seguidores");

        logadoRef.setValue(totalSeguindo);
        amigoRef.setValue(totalSeguidor);
        binding.fragmentSeguir.textEditarPerfil.setOnClickListener(null);

    }

    public void carregarGridView() {

        adapterGridPerfil = new AdapterGridPerfil(this, R.layout.grid_postagens, listPublicacoes);
        binding.fragmentSeguir.gridPerfil.setColumnWidth(getResources().getDisplayMetrics().widthPixels / 3);
        binding.fragmentSeguir.gridPerfil.setAdapter(adapterGridPerfil);
    }


}