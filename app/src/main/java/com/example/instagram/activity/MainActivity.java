package com.example.instagram.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.fragments.HomeFragment;
import com.example.instagram.fragments.PostagemFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.example.instagram.fragments.SearchFragment;
import com.example.instagram.tools.FirebaseInstance;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toollBarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layoutViewPager, new HomeFragment()).commit();
        navigationBar();




    }





    //metódo para configurar barra navegação inferior
    public void navigationBar() {
        //metodo para inflar o menu e chamar os fragments
        binding.navigation.navigationBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selecionado = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selecionado = new HomeFragment();
                        break;
                    case R.id.pesquisa:
                        selecionado = new SearchFragment();
                        break;
                    case R.id.adicionar:
                        selecionado = new PostagemFragment();
                        break;
                    case R.id.perfil:
                        selecionado = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.layoutViewPager, selecionado).commit();

                return true;
            }
        });


    }


    //metódo para inflar menu na toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //metódo  para manipular os elementos do menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sair:
                deslogar();
        }


        return super.onOptionsItemSelected(item);

    }

    //metódo par deslogar usuário
    public void deslogar() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Logout de usuário");
        alert.setMessage("Tem certeza que deseja sair de sua conta?");
        alert.setNegativeButton("Não", null);
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth firebaseAuth = FirebaseInstance.instanceFirebaseAuth();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        Dialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_degrade_roxo);
        dialog.show();


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}