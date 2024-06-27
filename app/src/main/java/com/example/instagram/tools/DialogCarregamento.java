package com.example.instagram.tools;

import android.app.AlertDialog;
import android.content.Context;

import com.example.instagram.R;

//exemplo de uma classe que inicializa um Dialog
public class DialogCarregamento {
    //Crie um objeto do tipo AlertDialog
    private static AlertDialog alert;

    public static void inflarDialog(String titulo, Context context){
        //crie um objeto construtor de AlertDialog e o custumize
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle(titulo);
        alertDialog.setView(R.layout.dialog_progress);
        alertDialog.setCancelable(false);
        //após e somente passar para o objeto criado primeiramente e o mostrar ou cancelar
        alert=alertDialog.create();
        alert.show();

    }
    public static void cancelarDialog(){

        alert.cancel();
    }


}
