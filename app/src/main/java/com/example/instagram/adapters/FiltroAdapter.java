package com.example.instagram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.activity.FiltroActivity;

import java.util.List;


public class FiltroAdapter extends RecyclerView.Adapter<FiltroAdapter.FiltroHolder>{
    private List<Bitmap> filtroList;
    private Context context;

    public FiltroAdapter(List<Bitmap> filtroList,Context context) {
        this.filtroList = filtroList;
        this.context=context;
    }

    @NonNull
    @Override
    public FiltroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_filtro,parent,false);


        return new FiltroHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltroHolder holder, int position) {
        Bitmap filtrosPersonalizados=filtroList.get(position);
        holder.imageFiltro.setImageBitmap(filtrosPersonalizados);

        holder.imageFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltroActivity filtroActivity= (FiltroActivity) context;
                filtroActivity.filtros(filtrosPersonalizados);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filtroList.size();
    }

    public class FiltroHolder extends RecyclerView.ViewHolder{

        public ImageView imageFiltro;
        public FiltroHolder(@NonNull View itemView) {
            super(itemView);

            imageFiltro=itemView.findViewById(R.id.imageRecyclerFiltro );

        }
    }
}
