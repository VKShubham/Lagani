package com.example.lagani20.Modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.lagani20.Adapter.RecyclerViewForHistory;
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

public class History2 extends AppCompatActivity {

    ImageView personlogo;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    RecyclerViewForHistory recyclerViewAdapter;
    ArrayList<Donations> list;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);

        personlogo = findViewById(R.id.logo);
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView = findViewById(R.id.history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();
        backbtn = findViewById(R.id.back_btn);
        // Image Load From An Storage

        Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
        personlogo.setImageBitmap(bitmap);

        //Back Button
        backbtn.setOnClickListener(view -> {
            onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // set an adapter to a RecyclerView
        firebaseDatabase.getReference("Donations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                String userid = user.getUid();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Donations donations = dataSnapshot.getValue(Donations.class);

                    if(donations != null && donations.getStatus() != null && donations.getUserid() != null) {
                        if(donations.getStatus().equals("2") && donations.getUserid2().equals(userid)){
                            list.add(donations);
                        }
                    }
                }
                recyclerViewAdapter = new RecyclerViewForHistory(History2.this,list);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database Error: " + error.getMessage());
            }
        });
    }
}