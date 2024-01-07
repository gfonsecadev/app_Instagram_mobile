package com.example.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMinhasPostagensBinding;
import com.example.instagram.model.Feed;
import com.example.instagram.tools.ZoomScale;

public class MinhasPostagensActivity extends AppCompatActivity {
    private ActivityMinhasPostagensBinding binding;
    private ZoomScale zoomScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMinhasPostagensBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        zoomScale=new ZoomScale(binding.layoutPost.imagePost,getApplicationContext());

        carregarIntent();


    }





    public void carregarIntent(){



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Feed feed = (Feed) bundle.getSerializable("feed");
            Glide.with(this).load(feed.getUriPostagem()).into(binding.layoutPost.imagePost);

            if (!feed.getFotoUsuario().equals("")) {
                Glide.with(this).load(feed.getFotoUsuario()).into(binding.layoutPost.imageFoto);
            } else binding.layoutPost.imageFoto.setImageResource(R.drawable.padrao);

            binding.layoutPost.textDescricao.setText(feed.getDescricaoPostagem());
            binding.layoutPost.textNomePost.setText(feed.getNomeUsuario());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zoomScale.zoom(event);
        return true;
    }
}