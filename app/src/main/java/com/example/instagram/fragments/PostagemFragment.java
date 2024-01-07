package com.example.instagram.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.instagram.R;
import com.example.instagram.activity.FiltroActivity;
import com.example.instagram.databinding.FragmentPostagemBinding;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostagemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostagemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostagemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostagemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostagemFragment newInstance(String param1, String param2) {
        PostagemFragment fragment = new PostagemFragment();
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

    private FragmentPostagemBinding binding;
    private Bitmap bitmap;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentPostagemBinding.inflate(getLayoutInflater());
        String [] permissoes={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
        permissao.launch(permissoes);



        return binding.getRoot();
    }

    //recuperar dados da camera
    ActivityResultLauncher camera=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if(result.getResultCode()== -1 && result.getData()!=null){
                bitmap= (Bitmap) result.getData().getExtras().get("data");
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,baos);
                byte[] bytes=baos.toByteArray();
                Intent intent1=new Intent(getActivity(),FiltroActivity.class);
                intent1.putExtra("camera",bytes);
                startActivity(intent1);

            }
        }
    });

    //recuperar dados da galeria
    ActivityResultLauncher galeria=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            bitmap=null;
            try {
                if(result.getResultCode()==-1 && result.getData()!=null){
                    bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver()
                            , result.getData().getData());
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,70,baos);
                    byte[] bytes=baos.toByteArray();
                    Intent intent2=new Intent(getActivity(),FiltroActivity.class);
                    intent2.putExtra("galeria",bytes);
                    startActivity(intent2);

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    });



    ActivityResultLauncher<String[]> permissao=registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            if(result.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE) && result.containsKey(Manifest.permission.CAMERA)){

                if(result.get(Manifest.permission.CAMERA).equals(true) && result.get(Manifest.permission.READ_EXTERNAL_STORAGE).equals(true)){
                    escolherImagem();
                }else {
                    aceitePermissoes();
                }
            }
        }
    });


    //acessar galeria ou camera após permissões aceitas
    public void escolherImagem(){
        binding.adicionarPostagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setTitle("Adicionar postagem");
                alert.setMessage("Escolha a fonte da postagem");
                alert.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if (intent.resolveActivity(getActivity().getPackageManager())!=null){
                            galeria.launch(intent);
                        }
                    }
                });


                alert.setNegativeButton("Câmera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent2.resolveActivity(getActivity().getPackageManager())!=null){
                            camera.launch(intent2);
                        }
                    }
                });

                Dialog dialog=alert.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bacground);
                dialog.show();


            }
        });
    }

    //mensagem em um Dialog para usuario ,pois não aceitou as permissões
    private  void aceitePermissoes(){
        binding.adicionarPostagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setTitle("Permissões foram negadas");
                alert.setMessage("É preciso aceitar todas as permissões para habilitar o botão para suas postagem");
                Dialog dialog=alert.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_permissao_negada_background);
                dialog.show();
            }
        });

    }
}