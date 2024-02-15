package com.example.lagani20.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lagani20.R;
import com.example.lagani20.RegisterLogin.MainActivity;
import com.example.lagani20.RegisterLogin.UpdateProfile;
import com.google.firebase.auth.FirebaseAuth;

public class RiderDashboard extends AppCompatActivity {

    View logout;
    View updateprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_dashboard);

        logout = findViewById(R.id.rectangle_6);
        updateprofile = findViewById(R.id.rectangle_7);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(RiderDashboard.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RiderDashboard.this, MainActivity.class));
                overridePendingTransition(R.anim.push_up_in,R.anim.push_down_out);
            }
        });

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RiderDashboard.this, UpdateProfile.class));
            }
        });
    }
}