package com.example.instagram.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.instagram.R;
import com.example.instagram.activity.FiltroActivity;
import com.example.instagram.databinding.FragmentPostagemBinding;

import java.io.ByteArrayOutputStream;
import java.text.BreakIterator;
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
    private FragmentPostagemBinding binding;
    private Bitmap bitmap;
    //recuperar dados da camera
    ActivityResultLauncher camera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == -1 && result.getData() != null) {
                bitmap = (Bitmap) result.getData().getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] bytes = baos.toByteArray();
                Intent intent1 = new Intent(getActivity(), FiltroActivity.class);
                intent1.putExtra("camera", bytes);
                startActivity(intent1);

            }
        }
    });

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );

    //recuperar dados da galeria
    ActivityResultLauncher galeria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                                                               new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            bitmap = null;
            try {
                //SE BEM SUCEDIDO E COM RETORNO DE DADOS.
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getData().getData());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //não é correto passarmos arquivos gramdes para as intents
                    //Elas carregam no máxim 1mb
                    //Mas isto é somente para fins educativos
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] bytes = baos.toByteArray();
                    Intent intent2 = new Intent(getActivity(), FiltroActivity.class);
                    intent2.putExtra("galeria", bytes);
                    startActivity(intent2);
                    getActivity().finish();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    });






    ActivityResultLauncher<String[]> permissao = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                                                                           new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            //inicializo uma variavel
            Boolean denied = false;
            //percorro o Map
            for (Boolean permission : result.values()) {
                if (permission == false) {
                    //se permissão estiver negada atualizo a variavel criada.
                    denied = true;
                }
            }

            //Se tiver alguma permissão negada
            if (denied) {
                //peço autorização
                aceitePermissoes();
            } else {
                //libero a aplicação
                escolherImagem();
            }
        }
    });

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostagemBinding.inflate(getLayoutInflater());
        String[] permissoes = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        permissao.launch(permissoes);


        binding.adicionarPostagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        return binding.getRoot();
    }

    //acessar galeria ou camera após permissões aceitas
    public void escolherImagem() {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Adicionar postagem");
                alert.setMessage("Escolha a fonte da postagem");
                alert.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //acessar a galeria
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //se tivermos uma seleção de algum arquivo.
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            //recuperamos o arquivo.
                            galeria.launch(intent);
                        }
                    }
                });


                alert.setNegativeButton("Câmera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent2.resolveActivity(getActivity().getPackageManager()) != null) {
                            camera.launch(intent2);
                        }
                    }
                });

                Dialog dialog = alert.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bacground);
                dialog.show();

    }



    //mensagem em um Dialog para usuario ,pois não aceitou as permissões
    private void aceitePermissoes() {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Permissões foram negadas");
                alert.setMessage("Para prosseguirmos, precisamos de algumas permissões para fazer postagens");

                alert.setPositiveButton("aceitar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });

        Dialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_permissao_negada_background);

        dialog.show();

            }


    }
