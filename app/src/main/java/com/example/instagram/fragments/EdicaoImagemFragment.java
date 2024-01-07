package com.example.instagram.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.instagram.activity.FiltroActivity;
import com.example.instagram.databinding.FragmentEdicaoImagemBinding;
import com.mukesh.image_processing.ImageProcessor;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EdicaoImagemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdicaoImagemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EdicaoImagemFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdicaoImagemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EdicaoImagemFragment newInstance(String param1, String param2) {
        EdicaoImagemFragment fragment = new EdicaoImagemFragment();
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

    private FragmentEdicaoImagemBinding binding;
    private Bitmap bitmapRecebido;
    private Bitmap bitmapefeito;
    private FiltroActivity filtroActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentEdicaoImagemBinding.inflate(getLayoutInflater());

        Bundle bundle=getArguments();
        byte [] image=bundle.getByteArray("galeria");
        bitmapRecebido = BitmapFactory.decodeByteArray(image,0,image.length);
        bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
        filtroActivity= (FiltroActivity) getActivity();

        efeitos();
        return binding.getRoot();
    }







    public void efeitos(){
        binding.seekRotacao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ImageProcessor imageProcessor=new ImageProcessor();
                filtroActivity.efeitos(imageProcessor.rotate(bitmapefeito,progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekSaturacao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Filter filter=new Filter();
                float f=progress/100f;
                filter.addSubFilter(new SaturationSubFilter(f));
                bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
                filtroActivity.efeitos(filter.processFilter(bitmapefeito));
                binding.seekSombra.setProgress(0);
                binding.seekContraste.setProgress(1);
                binding.seekBrilho.setProgress(0);
                binding.seekSopreposicaoVermelho.setProgress(0);
                binding.seekSopreposicaoVerde.setProgress(0);
                binding.seekRotacao.setProgress(0);
                binding.seekSopreposicaoAzul.setProgress(0);
                binding.seekCircular.setProgress(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekBrilho.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Filter filter=new Filter();
                filter.addSubFilter(new BrightnessSubFilter(progress));
                bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
                filtroActivity.efeitos(filter.processFilter(bitmapefeito));
                binding.seekSombra.setProgress(0);
                binding.seekContraste.setProgress(1);
                binding.seekSaturacao.setProgress(1);
                binding.seekSopreposicaoVermelho.setProgress(0);
                binding.seekSopreposicaoVerde.setProgress(0);
                binding.seekRotacao.setProgress(0);
                binding.seekSopreposicaoAzul.setProgress(0);
                binding.seekCircular.setProgress(0);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekContraste.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Filter filter=new Filter();
                float f=  (progress / 100.0f);;
                filter.addSubFilter(new ContrastSubFilter(f));
                Toast.makeText(getActivity(), String.valueOf(f),Toast.LENGTH_LONG).show();
                bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
                filtroActivity.efeitos(filter.processFilter(bitmapefeito));
                binding.seekSombra.setProgress(0);
                binding.seekSaturacao.setProgress(1);
                binding.seekBrilho.setProgress(0);
                binding.seekSopreposicaoVermelho.setProgress(0);
                binding.seekRotacao.setProgress(0);
                binding.seekSopreposicaoVerde.setProgress(0);
                binding.seekSopreposicaoAzul.setProgress(0);
                binding.seekCircular.setProgress(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekSombra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Filter filter=new Filter();
                filter.addSubFilter(new VignetteSubFilter(getActivity(),progress));
                bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
                filtroActivity.efeitos(filter.processFilter(bitmapefeito));
                binding.seekContraste.setProgress(1);
                binding.seekSaturacao.setProgress(1);
                binding.seekBrilho.setProgress(0);
                binding.seekSopreposicaoVermelho.setProgress(0);
                binding.seekRotacao.setProgress(0);
                binding.seekSopreposicaoVerde.setProgress(0);
                binding.seekSopreposicaoAzul.setProgress(0);
                binding.seekCircular.setProgress(0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekSopreposicaoVermelho.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Filter filter=new Filter();
                float f=progress/100f;
                filter.addSubFilter(new ColorOverlaySubFilter(100,f,0,0));
                bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
                filtroActivity.efeitos(filter.processFilter(bitmapefeito));
                binding.seekSombra.setProgress(0);
                binding.seekContraste.setProgress(1);
                binding.seekBrilho.setProgress(0);
                binding.seekSaturacao.setProgress(1);
                binding.seekRotacao.setProgress(0);
                binding.seekSopreposicaoVerde.setProgress(0);
                binding.seekSopreposicaoAzul.setProgress(0);
                binding.seekCircular.setProgress(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekSopreposicaoVerde.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Filter filter=new Filter();
                float f=progress/100f;
                filter.addSubFilter(new ColorOverlaySubFilter(100,0,f,0));
                bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
                filtroActivity.efeitos(filter.processFilter(bitmapefeito));
                binding.seekSombra.setProgress(0);
                binding.seekContraste.setProgress(1);
                binding.seekBrilho.setProgress(0);
                binding.seekSaturacao.setProgress(1);
                binding.seekRotacao.setProgress(0);
                binding.seekSopreposicaoVermelho.setProgress(0);
                binding.seekSopreposicaoAzul.setProgress(0);
                binding.seekCircular.setProgress(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekSopreposicaoAzul.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Filter filter=new Filter();
                float f=progress/100f;
                filter.addSubFilter(new ColorOverlaySubFilter(100,0,0,f));
                bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
                filtroActivity.efeitos(filter.processFilter(bitmapefeito));
                binding.seekSombra.setProgress(0);
                binding.seekContraste.setProgress(1);
                binding.seekRotacao.setProgress(0);
                binding.seekBrilho.setProgress(0);
                binding.seekSaturacao.setProgress(1);
                binding.seekSopreposicaoVermelho.setProgress(0);
                binding.seekSopreposicaoVerde.setProgress(0);
                binding.seekCircular.setProgress(0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekCircular.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                filtroActivity.efeitos(cantosArredondados(bitmapefeito,progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //responsável por arredondar um bitmap
    public Bitmap  cantosArredondados(Bitmap bitmap,int corners){
        int menor=Math.min(bitmap.getHeight(),bitmap.getWidth());
        Bitmap imagemModificada=Bitmap.createBitmap(menor,menor,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(imagemModificada);

        Paint paint=new Paint();
        //dar forma ao retângulo da borda
        Rect rect=new Rect(0,0,menor,menor);
        //impede que a imagem fique com aspecto serrilhado
        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        //seleciona a cor do paint aplicado para dar a forma
        paint.setColor(0xff424242);
        //desenha as bordas
        canvas.drawRoundRect(new RectF(rect),corners,corners,paint );


        //modo de tranferência da forma
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //desenha a forma
        canvas.drawBitmap(bitmap,rect,rect,paint);

        bitmapefeito=imagemModificada.copy(imagemModificada.getConfig(),true);



        return imagemModificada;

    }
}