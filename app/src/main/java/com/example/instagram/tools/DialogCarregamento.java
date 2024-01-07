package com.example.instagram.tools;

import android.app.AlertDialog;
import android.content.Context;

import com.example.instagram.R;

public class DialogCarregamento {
    private static AlertDialog alert;

    public static void inflarDialog(String titulo, Context context){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle(titulo);
        alertDialog.setView(R.layout.dialog_progress);
        alertDialog.setCancelable(false);
        alert=alertDialog.create();
        alert.show();


    }

    public static void cancelarDialog(){
        alert.cancel();
    }


}
