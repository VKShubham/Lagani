package com.example.lagani20.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lagani20.R;
import com.example.lagani20.RegisterLogin.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DonorDashboard extends AppCompatActivity {

    View logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);

        logout = findViewById(R.id.rectangle_6);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DonorDashboard.this,MainActivity.class));
                overridePendingTransition(R.anim.push_up_in,R.anim.push_down_out);
            }
        });
    }
}