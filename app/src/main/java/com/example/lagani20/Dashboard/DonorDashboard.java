package com.example.lagani20.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lagani20.Modules.ADDDonations;
import com.example.lagani20.R;
import com.example.lagani20.RegisterLogin.MainActivity;
import com.example.lagani20.RegisterLogin.UpdateProfile;
import com.google.firebase.auth.FirebaseAuth;

public class DonorDashboard extends AppCompatActivity {

    View logout;
    ImageButton donationbtn;
    View updateprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);

        logout = findViewById(R.id.rectangle_6);
        donationbtn = findViewById(R.id.rectangle_3);
        updateprofile = findViewById(R.id.rectangle_7);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(DonorDashboard.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DonorDashboard.this,MainActivity.class));
                overridePendingTransition(R.anim.push_up_in,R.anim.push_down_out);
            }
        });

        donationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DonorDashboard.this, ADDDonations.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DonorDashboard.this, UpdateProfile.class));
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
    }
}