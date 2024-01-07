package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UScript;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activity.SeguirActivity;
import com.example.instagram.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.HolderPesquisa> {
    private List<Usuario> listPesquisa;
    private Context context;

    public AdapterPesquisa(Context context,List<Usuario> list) {
        this.context = context;
        listPesquisa=list;
    }

    @NonNull
    @Override
    public HolderPesquisa onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_usuarios,parent,false);

        return new HolderPesquisa(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPesquisa holder, int position) {
        Usuario usuario=listPesquisa.get(position);
        holder.textNomeUsuario.setText(usuario.getNome());

        if(!usuario.getFoto().equals("")){
            Glide.with(context).load(usuario.getFoto()).into(holder.imageUsuario);
        }else {
            holder.imageUsuario.setImageResource(R.drawable.padrao);
        }

        holder.linearLayoutUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context.getApplicationContext(), SeguirActivity.class);
                intent.putExtra("usuarioProcurado",usuario);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPesquisa.size();
    }

    public class HolderPesquisa extends RecyclerView.ViewHolder {
        public CircleImageView imageUsuario;
        public TextView textNomeUsuario;
        public LinearLayout linearLayoutUsuario;
        public HolderPesquisa(@NonNull View itemView) {
            super(itemView);
            imageUsuario=itemView.findViewById(R.id.imageRecycler);
            textNomeUsuario=itemView.findViewById(R.id.textNomeRecycler);
            linearLayoutUsuario=itemView.findViewById(R.id.linearRecycler);

        }
    }

}
