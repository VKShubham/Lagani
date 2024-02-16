package com.example.lagani20.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        if (list != null) {

        }
    }
}