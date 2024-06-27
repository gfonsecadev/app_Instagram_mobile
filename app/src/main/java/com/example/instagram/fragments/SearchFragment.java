package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.instagram.R;
import com.example.instagram.adapters.AdapterPesquisa;
import com.example.instagram.databinding.FragmentSearchBinding;
import com.example.instagram.model.Usuario;
import com.example.instagram.tools.FirebaseInstance;
import com.example.instagram.tools.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    private FragmentSearchBinding binding;
    private List<Usuario> listPesquisa=new ArrayList<>();
    private List<Usuario> listPesquisaAchados=new ArrayList<>();
    private DatabaseReference pesquisaRef;
    private AdapterPesquisa adapterPesquisa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSearchBinding.inflate(getLayoutInflater());
        carregarUsuarios();

        //Instãncia do adapter
        adapterPesquisa=new AdapterPesquisa(getActivity(),listPesquisaAchados);
        //instância do LayoutManager
        binding.recyclerPesquisa.setLayoutManager(new LinearLayoutManager(getActivity()));
        //para não mudar de tamanho
        binding.recyclerPesquisa.setHasFixedSize(true);
        //renderiza um divisor entre a lista
        binding.recyclerPesquisa.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        //setamos o adapter
        binding.recyclerPesquisa.setAdapter(adapterPesquisa);



        return binding.getRoot();
    }

    /*metódo responsável por verificar na lista carregada com todos os usuários
    se contém algum com base no digitado  no searchView*/
    private void pesquisar(){
        binding.searchPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0){
                       listPesquisaAchados.clear();
                       for (Usuario usuario:listPesquisa){
                           String nome=usuario.getNome().toUpperCase();
                           if(nome.contains(newText.toUpperCase())){
                               listPesquisaAchados.add(usuario);
                           }
                           adapterPesquisa.notifyDataSetChanged();
                       }
                }else if(newText.length()==0) {
                    listPesquisaAchados.clear();
                    adapterPesquisa.notifyDataSetChanged();
                }


                return true;
            }
        });


    }

    //carrega uma lista com todos os usuários do firebase
    private void carregarUsuarios(){
        pesquisaRef= FirebaseInstance.instanceDatabase().child("usuarios");
        pesquisaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPesquisa.clear();
                for (DataSnapshot db:snapshot.getChildren()){
                    Usuario usuario=db.child("dadosUsuario").getValue(Usuario.class);
                    if(!usuario.getEmail().equals(UsuarioFirebase.dadosUsuario().getEmail()))
                        listPesquisa.add(usuario);
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        pesquisar();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}