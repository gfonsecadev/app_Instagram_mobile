package com.example.instagram.tools;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class FirebaseInstance {
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;
    private static FirebaseAuth firebaseAuth;

    //carrega instância do RealTimeDatabase do Firebase
    public static DatabaseReference instanceDatabase(){
        if(databaseReference==null){
            databaseReference= FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    //carrega instância do FirebaseAuth
    public static FirebaseAuth instanceFirebaseAuth(){
        if(firebaseAuth==null){
            firebaseAuth= FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    //carrega instância do Storage do Firebase
    public static StorageReference instanceStorage(){
        if(storageReference==null){
            storageReference= FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }



}
