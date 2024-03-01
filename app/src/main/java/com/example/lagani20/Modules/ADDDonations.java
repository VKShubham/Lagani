package com.example.lagani20.Modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lagani20.Dashboard.DonorDashboard;
import com.example.lagani20.R;
import com.example.lagani20.classes.BasicUtils;
import com.example.lagani20.classes.Donations;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;

public class ADDDonations extends AppCompatActivity implements OnMapReadyCallback {

    RadioGroup donationtype;
    RadioGroup donationweight;
    RadioGroup suggestedvehicle;
    EditText pincode;
    EditText address;
    EditText contactno;
    EditText resturant;
    View submitbtn;
    FirebaseAuth fauth;
    FirebaseUser user;
    FirebaseDatabase db;
    ImageView logo;
    ImageView backtn;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    private GoogleMap googleMap;
    public Double longitiude;
    public Double latitude;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddonations);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        donationtype = findViewById(R.id.donation_type);
        donationweight = findViewById(R.id.weight);
        suggestedvehicle = findViewById(R.id.suggested_vechicle);
        pincode = findViewById(R.id.some_id);
        address = findViewById(R.id.address_text);
        contactno = findViewById(R.id.some_mobileno);
        submitbtn = findViewById(R.id.submit_btn);
        resturant = findViewById(R.id.res_name);
        firebaseStorage = FirebaseStorage.getInstance();
        logo = findViewById(R.id.logo);
        backtn = findViewById(R.id.back_btn);

        fauth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user  = fauth.getCurrentUser();
        BasicUtils utils = new BasicUtils();

            Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
            logo.setImageBitmap(bitmap);

        backtn.setOnClickListener(view -> {
            onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        submitbtn.setOnClickListener(view -> {
            String donationtype_txt;
            String donationweight_txt;
            String suggestedvehicle_txt;
            String pincode_txt;
            String mobileno_txt;
            String address_txt;
            String resturant_txt;

            if (!getdonationtype(donationtype).equals("null") && !getdonationweight(donationweight).equals("null") && !getsuggestedvehicle(suggestedvehicle).equals("null")) {
                donationtype_txt = getdonationtype(donationtype);
                donationweight_txt = getdonationweight(donationweight);
                suggestedvehicle_txt = getsuggestedvehicle(suggestedvehicle);
                pincode_txt = pincode.getText().toString();
                mobileno_txt = contactno.getText().toString();
                address_txt = address.getText().toString();
                resturant_txt = resturant.getText().toString();
                if (pincode_txt.isEmpty()) {
                    Toast.makeText(ADDDonations.this, "Please Enter Pin Code", Toast.LENGTH_SHORT).show();
                } else if (mobileno_txt.length() != 10) { // Wrong Logic (Correct in simple way need further ref)
                    Toast.makeText(ADDDonations.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                } else if(address_txt.isEmpty()){
                    Toast.makeText(ADDDonations.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                } else if (resturant_txt.isEmpty()) {
                    // Handle case when restaurant name is empty
                } else {
                    adddetailstodatabase(donationtype_txt,donationweight_txt,suggestedvehicle_txt,pincode_txt,address_txt,mobileno_txt,user.getUid(),resturant_txt,longitiude,latitude);
                }
            } else {
                Toast.makeText(ADDDonations.this, "Please Select all Options", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String generateUniqueNumber() {
        long timestamp = System.currentTimeMillis();
        int lastFourDigits = (int) (timestamp % 10000);
        lastFourDigits = Math.abs(lastFourDigits);
        return String.format("%04d", lastFourDigits);
    }

    public static String getdate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = String.format("%02d-%02d-%04d", day, month, year);
        return dateString;
    }

    private void adddetailstodatabase(String donationtype, String donationweight, String suggestedvehicle, String pincode, String mobileno, String address, String userid, String resturant, double longitiude, double latitude) {
        final String key = db.getReference().push().getKey();
        final Donations donations = new Donations(generateUniqueNumber(),donationtype,donationweight,suggestedvehicle,pincode,mobileno,address,userid,resturant,"0",key,null, getdate(),longitiude,latitude);

        db.getReference("Donations")
                .child(key)
                .setValue(donations).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ADDDonations.this, "Donation Sent to Rider", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ADDDonations.this, DonorDashboard.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Toast.makeText(ADDDonations.this, "Failed to Sent to the Rider", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String getdonationtype(RadioGroup group) {
        if(group.getCheckedRadioButtonId() == R.id.ellipse_11) {
            return "only people";
        } else if(group.getCheckedRadioButtonId() == R.id.ellipse_12) {
            return "only animal";
        }
        return "both";
    }

    private String getdonationweight(RadioGroup group) {
        if(group.getCheckedRadioButtonId() == R.id.ellipse_15) {
            return "1-10kg";
        } else if(group.getCheckedRadioButtonId() == R.id.ellipse_17) {
            return "10-20kg";
        } else if(group.getCheckedRadioButtonId() == R.id.ellipse_16) {
            return "20-30kg";
        }
        return "30-40kg";
    }

    private String getsuggestedvehicle(RadioGroup group) {
        if(group.getCheckedRadioButtonId() == R.id.ellipse_19) {
            return "Bike";
        } else if(group.getCheckedRadioButtonId() == R.id.ellipse_20) {
            return "Tempo";
        } else if(group.getCheckedRadioButtonId() == R.id.ellipse_21) {
            return "Car";
        }
        return "Van";
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.googleMap.setMyLocationEnabled(true);

            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                                latitude = location.getLatitude();
                                longitiude = location.getLongitude();
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
