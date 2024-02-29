package com.example.lagani20.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lagani20.Adapter.RecyclerViewAdapter;
import com.example.lagani20.Modules.Accepted_Donations;
import com.example.lagani20.R;
import com.example.lagani20.RegisterLogin.MainActivity;
import com.example.lagani20.classes.Donations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class RiderDashboard extends AppCompatActivity {

    View logout;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Donations> list;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseDatabase db;
    ImageView personlogo;
    FirebaseStorage firebaseStorage;
    StorageReference databaseReference;
    ImageView acceptedDonationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_dashboard);

        logout = findViewById(R.id.rectangle_6);
        recyclerView = findViewById(R.id.avildonations);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personlogo = findViewById(R.id.logo);
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseStorage.getReference().child("images" + user.getUid());

        databaseReference.getFile(new File(getCacheDir(), "temp.jpg")).addOnSuccessListener(taskSnapshot -> {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
            personlogo.setImageBitmap(bitmap);
        }).addOnFailureListener(e -> Log.e("Firebase", "Failed to download image: " + e.getMessage()));

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(RiderDashboard.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RiderDashboard.this, MainActivity.class));
            overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
        });

        acceptedDonationButton = findViewById(R.id.rectangle_3);
        acceptedDonationButton.setOnClickListener(view -> {
            startActivity(new Intent(RiderDashboard.this, Accepted_Donations.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(RiderDashboard.this, list);
        recyclerView.setAdapter(recyclerViewAdapter);
        db.getReference().child("Donations").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Donations donation = snapshot.getValue(Donations.class);
                if ("0".equals(donation.getStatus())) {
                    list.add(donation);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Handle child changed event
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle child removed event
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
