package com.example.lagani20.Modules;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.lagani20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class History extends AppCompatActivity {


    ImageView personlogo;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        personlogo = findViewById(R.id.logo);
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageReference = firebaseStorage.getReference().child("images" + user.getUid());

        // Image Load From An Storage

        storageReference.getFile(new File(getCacheDir(), "temp.jpg")).addOnSuccessListener(taskSnapshot -> {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
            personlogo.setImageBitmap(bitmap);
        }).addOnFailureListener(e -> Log.e("Firebase", "Failed to download image: " + e.getMessage()));


    }
}