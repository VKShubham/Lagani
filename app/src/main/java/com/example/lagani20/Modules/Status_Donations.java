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
import com.google.firebase.database.ChildEventListener;
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
    ImageView logo;
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
        logo = findViewById(R.id.logo);

        Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
        logo.setImageBitmap(bitmap);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        recyclerViewForStatus = new RecyclerViewForStatus(Status_Donations.this, list);
        recyclerView.setAdapter(recyclerViewForStatus);
        db.getReference()
                .child("Donations")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                        Donations donations = snapshot.getValue(Donations.class);
                        if (("0".equals(donations.getStatus()) && user.getUid().equals(donations.getUserid())  || ("2".equals(donations.getStatus()) && user.getUid().equals(donations.getUserid()) || "1".equals(donations.getStatus())) && user.getUid().equals(donations.getUserid()))) {
                            list.add(donations);
                            recyclerViewForStatus.notifyItemInserted(list.size() - 1); // Notify adapter of new item
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                        // Handle child changed event
                        // You might need to update the item in your list and notify the adapter
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        // Handle child removed event
                        // You might need to remove the item from your list and notify the adapter
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                        // Handle child moved event
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled() method if needed
                    }
                });
    }
}