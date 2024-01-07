package com.example.instagram.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.activity.FiltroActivity;
import com.example.instagram.adapters.FiltroAdapter;
import com.example.instagram.databinding.FragmentImagemFiltrosBinding;
import com.example.instagram.tools.SampleFilters;
import com.mukesh.image_processing.ImageProcessor;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImagemFiltrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagemFiltrosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImagemFiltrosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagemFiltrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagemFiltrosFragment newInstance(String param1, String param2) {
        ImagemFiltrosFragment fragment = new ImagemFiltrosFragment();
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


    private FragmentImagemFiltrosBinding binding;
    private Bitmap bitmapRecebido,bitmapefeito;
    private FiltroActivity filtroActivity;
    List<Bitmap> listFiltrosRecicler=new ArrayList<>();
    List<Filter> list=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImagemFiltrosBinding.inflate(getLayoutInflater());

        Bundle bundle=getArguments();
        byte [] image=bundle.getByteArray("galeria");
        bitmapRecebido = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(image,0,image.length),500,500);
        bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);


        filtros();
        filtroActivity= (FiltroActivity) getActivity();



        return binding.getRoot();

    }

    public void filtros(){
        binding.progressFiltro.setVisibility(View.VISIBLE);
        ImageProcessor imageProcessor=new ImageProcessor();
        list.add(SampleFilters.getAweStruckVibeFilter());
        list.add(SampleFilters.getBlueMessFilter());
        list.add(SampleFilters.getLimeStutterFilter());
        list.add(SampleFilters.getStarLitFilter());
        list.add(SampleFilters.getNightWhisperFilter());
        list.add(SampleFilters.sepia());
        list.add(SampleFilters.OVER());
        list.add(SampleFilters.x());
        list.add(SampleFilters.y());
        list.add(SampleFilters.z());
        list.add(SampleFilters.q());
        list.add(SampleFilters.gray());




        for (Filter filter :list) {
            bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
            listFiltrosRecicler.add(filter.processFilter(bitmapefeito));

        }

        listFiltrosRecicler.add(imageProcessor.doInvert(bitmapefeito));
        bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);
        listFiltrosRecicler.add(imageProcessor.applyReflection(bitmapefeito));
        bitmapefeito=bitmapRecebido.copy(bitmapRecebido.getConfig(),true);


        binding.progressFiltro.setVisibility(View.GONE);
        binding.recyclerFiltros.setVisibility(View.VISIBLE);
        FiltroAdapter adapter=new FiltroAdapter(listFiltrosRecicler, getActivity());
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        binding.recyclerFiltros.setLayoutManager(manager);
        binding.recyclerFiltros.setHasFixedSize(true);
        binding.recyclerFiltros.setAdapter(adapter);

    }

}