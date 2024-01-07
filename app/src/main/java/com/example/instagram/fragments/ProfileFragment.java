package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activity.EditarPerfilActivity;
import com.example.instagram.adapters.AdapterGridPerfil;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.example.instagram.model.Postagens;
import com.example.instagram.model.Usuario;
import com.example.instagram.tools.FirebaseInstance;
import com.example.instagram.tools.ImageLoaderConfig;
import com.example.instagram.tools.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentProfileBinding binding;
    private Usuario usuarioLogado;
    private ValueEventListener listener;
    private DatabaseReference databaseReference;
    private List<String> listPost;
    private AdapterGridPerfil adapterGridPerfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(getLayoutInflater());
        binding.progressBarPerfil.setVisibility(View.VISIBLE);
        binding.textEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));

            }
        });

        ImageLoaderConfig.carregarImageLoader(getActivity());
        recuperarPostagens();


        return binding.getRoot();


    }
    //carregar dados da tela com informações do usuário logado
    private void carregarDados(){
        if(!usuarioLogado.getFoto().equals("")){
            Glide.with(this).load(usuarioLogado.getFoto()).into(binding.imagePerfil);
        }else {
            binding.imagePerfil.setImageResource(R.drawable.padrao);
        }

        binding.textNomeUsuario.setText(usuarioLogado.getNome());
        binding.textPublicacoes.setText(String.valueOf(usuarioLogado.getPublicacoes()));
        binding.textSeguidores.setText(String.valueOf(usuarioLogado.getSeguidores()));
        binding.textSeguindo.setText(String.valueOf(usuarioLogado.getSeguindo()));

    }

    //carrega informações do usuario logado com base no firebase
    public void carregarUsuario(){

            databaseReference=FirebaseInstance.instanceDatabase().child("usuarios").child(UsuarioFirebase.getFirebaseUser().getUid());
            listener=databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usuarioLogado=snapshot.child("dadosUsuario").getValue(Usuario.class);
                    carregarDados();
                    binding.progressBarPerfil.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

          }

    @Override
    public void onStart() {
        super.onStart();
        carregarUsuario();

    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(listener);
    }


    public void recuperarPostagens(){
        listPost=new ArrayList<>();
        DatabaseReference post= FirebaseInstance.instanceDatabase().child("usuarios")
                .child(UsuarioFirebase.dadosUsuario().getId()).child("postagens");
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Postagens postagens=ds.getValue(Postagens.class);
                    listPost.add(postagens.getUrlPostagem());
                }
                carregarGridView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void carregarGridView(){
        adapterGridPerfil=new AdapterGridPerfil(getActivity(),R.layout.grid_postagens,listPost);
        binding.gridPerfil.setColumnWidth(getResources().getDisplayMetrics().widthPixels/3);
        binding.gridPerfil.setAdapter(adapterGridPerfil);
    }




}