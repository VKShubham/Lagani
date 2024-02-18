package com.example.lagani20.Modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.lagani20.Adapter.RecyclerView2;
import com.example.lagani20.Adapter.RecyclerViewAdapter;
import com.example.lagani20.Dashboard.RiderDashboard;
import com.example.lagani20.R;
import com.example.lagani20.classes.Donations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Accepted_Donations extends AppCompatActivity {

    ArrayList<Donations> accpetdlist;
    FirebaseDatabase db;
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView2 recyclerView2;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_donations);

        recyclerView = findViewById(R.id.view_2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db.getReference()
                .child("Donations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userid = user.getUid();
                accpetdlist = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Donations donations = dataSnapshot.getValue(Donations.class);
                    if("1".equals(donations.getStatus()) && userid.equals(donations.getUserid())){
                        accpetdlist.add(donations);
                    }
                }
                recyclerView2 = new RecyclerView2(Accepted_Donations.this, accpetdlist);
                recyclerView.setAdapter(recyclerView2);
                recyclerView2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}