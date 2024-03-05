package com.example.lagani20.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lagani20.Adapter.RecyclerViewAdapter;
import com.example.lagani20.Modules.Accepted_Donations;
import com.example.lagani20.R;
import com.example.lagani20.RegisterLogin.MainActivity;
import com.example.lagani20.RegisterLogin.UpdateProfile;
import com.example.lagani20.classes.Donations;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
    private static final int EARTH_RADIUS = 6371;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
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
    double currentLatitude;
    double currentLongitude;

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

        Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
        personlogo.setImageBitmap(bitmap);

        personlogo.setOnClickListener(view -> {
            startActivity(new Intent(RiderDashboard.this, UpdateProfile.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

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
                if ("0".equals(donation.getStatus()) && isWithinRadius(currentLatitude, currentLongitude, donation.getLatitude(), donation.getLongitude(), 10)) {
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

        requestLocationPermissions();
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestLocationUpdates() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(this, "Location can't get it", Toast.LENGTH_LONG).show();
                });
    }

    public boolean isWithinRadius(double currentLat, double currentLon, double targetLat, double targetLon, double radius) {
        double distance = calculateDistance(currentLat, currentLon, targetLat, targetLon);
        return distance <= radius;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
