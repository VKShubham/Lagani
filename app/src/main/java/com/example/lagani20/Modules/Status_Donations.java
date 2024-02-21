package com.example.lagani20.Modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.lagani20.Adapter.RecyclerViewForStatus;
import com.example.lagani20.R;
import com.example.lagani20.classes.Donations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class Status_Donations extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView personlogo;
    ImageView backbtn;
    ArrayList<Donations> list;
    RecyclerViewForStatus recyclerViewForStatus;
    FirebaseDatabase db;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseStorage firebaseStorage;
    StorageReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_donations);

        backbtn = findViewById(R.id.back_btn);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        list  =new ArrayList<>();
        recyclerView = findViewById(R.id.status_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        personlogo = findViewById(R.id.logo);
        databaseReference = firebaseStorage.getReference().child("images" + user.getUid());

        databaseReference.getFile(new File(getCacheDir(), "temp.jpg")).addOnSuccessListener(taskSnapshot -> {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
            personlogo.setImageBitmap(bitmap);
            //   bar.setVisibility(View.INVISIBLE);
        }).addOnFailureListener(e -> Log.e("Firebase", "Failed to download image: " + e.getMessage()));

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        db.getReference()
                .child("Donations").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Donations donations = dataSnapshot.getValue(Donations.class);
                            if("2".equals(donations.getStatus()) || "1".equals(donations.getStatus()) && user.getUid().equals(donations.getUserid())){
                                list.add(donations);
                            }
                        }
                        recyclerViewForStatus = new RecyclerViewForStatus(Status_Donations.this, list);
                        recyclerView.setAdapter(recyclerViewForStatus);
                        recyclerViewForStatus.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled() method if needed
                    }
                });
    }
}