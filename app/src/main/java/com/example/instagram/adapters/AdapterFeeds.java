package com.example.instagram.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.method.Touch;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activity.MainActivity;
import com.example.instagram.activity.MinhasPostagensActivity;
import com.example.instagram.databinding.ActivityFiltroBinding;
import com.example.instagram.model.Feed;
import com.example.instagram.tools.ImageSquad;
import com.example.instagram.tools.ZoomScale;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeeds extends RecyclerView.Adapter<AdapterFeeds.HolderFeed> {
    private List<Feed> feedList;
    private Activity context;

    public AdapterFeeds(List<Feed> feedList, Activity context) {
        this.feedList = feedList;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderFeed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_postagens,parent,false);

        return new HolderFeed(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFeed holder, int position) {
        Feed feed = feedList.get(position);

        if (!feed.getFotoUsuario().equals("")) {
            Glide.with(context).load(feed.getFotoUsuario()).into(holder.imageFoto);
        } else holder.imageFoto.setImageResource(R.drawable.padrao);

        Glide.with(context).load(feed.getUriPostagem()).into(holder.imagePost);
        holder.textDescricao.setText(feed.getDescricaoPostagem());
        holder.textNome.setText(feed.getNomeUsuario());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MinhasPostagensActivity.class);
                intent.putExtra("feed",feed);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class HolderFeed extends RecyclerView.ViewHolder{
        ImageSquad imagePost;
        CircleImageView imageFoto;
        TextView textNome;
        TextView textDescricao;
        LinearLayout linearLayout;

        public HolderFeed(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.imagePost);
            imageFoto = itemView.findViewById(R.id.imageFoto);
            textNome = itemView.findViewById(R.id.textNomePost);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            linearLayout=itemView.findViewById(R.id.layoutFeed);



        }
    }



}
