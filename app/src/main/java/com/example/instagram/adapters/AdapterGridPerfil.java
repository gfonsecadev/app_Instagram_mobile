package com.example.instagram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.instagram.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;
import java.util.List;

public class AdapterGridPerfil extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private List<String> list;

    public AdapterGridPerfil(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context=context;
        this.list=objects;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(resource,parent,false);
            holder.imageView=convertView.findViewById(R.id.imagemGridPerfil);
            holder.progressBar=convertView.findViewById(R.id.progressGridPerfil);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();


        }

        String uri=list.get(position);
        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.displayImage(uri, holder.imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.progressBar.setVisibility(View.GONE);

            }
        });




        return convertView;


    }

    public class ViewHolder{
        public ImageView imageView;
        public ProgressBar progressBar;
    }
}
