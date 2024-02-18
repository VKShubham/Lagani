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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lagani20.Adapter.RecyclerViewAdapter;
import com.example.lagani20.R;
import com.example.lagani20.RegisterLogin.MainActivity;
import com.example.lagani20.RegisterLogin.UpdateProfile;
import com.example.lagani20.classes.Donations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class RiderDashboard extends AppCompatActivity {

    View logout;
    View updateprofile;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Donations> list;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseDatabase db;
    ImageView personlogo;
    FirebaseStorage firebaseStorage;
    StorageReference databaseReference;
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
         //   bar.setVisibility(View.INVISIBLE);
        }).addOnFailureListener(e -> Log.e("Firebase", "Failed to download image: " + e.getMessage()));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(RiderDashboard.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RiderDashboard.this, MainActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
            }
        });


        db.getReference()
                .child("Donations").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Donations donations = dataSnapshot.getValue(Donations.class);
                            list.add(donations);
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(RiderDashboard.this, list);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled() method if needed
                    }
                });
    }
}