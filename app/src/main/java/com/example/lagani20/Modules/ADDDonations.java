package com.example.lagani20.Modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ADDDonations extends AppCompatActivity {

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
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddonations);

        donationtype = findViewById(R.id.donation_type);
        donationweight = findViewById(R.id.weight);
        suggestedvehicle = findViewById(R.id.suggested_vechicle);
        pincode = findViewById(R.id.some_id);
        address = findViewById(R.id.address_text);
        contactno = findViewById(R.id.some_mobileno);
        submitbtn = findViewById(R.id.submit_btn);
        resturant = findViewById(R.id.res_name);
        firebaseStorage = FirebaseStorage.getInstance();
        logo = findViewById(R.id.ellipse_10);

        fauth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user  = fauth.getCurrentUser();
        BasicUtils utils = new BasicUtils();

        storageReference = firebaseStorage.getReference().child("images/" + user.getUid());

        storageReference.getFile(new File(getCacheDir(), "temp.jpg")).addOnSuccessListener(taskSnapshot -> {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
            logo.setImageBitmap(bitmap);
            //   bar.setVisibility(View.INVISIBLE);
        }).addOnFailureListener(e -> Log.e("Firebase", "Failed to download image: " + e.getMessage()));

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String donationtype_txt;
                String donationweight_txt;
                String suggestedvehicle_txt;
                String pincode_txt;
                String mobileno_txt;
                String address_txt;
                String resturant_txt;

                if(!getdonationtype(donationtype).equals("null") && !getdonationweight(donationweight).equals("null") && !getsuggestedvehicle(suggestedvehicle).equals("null"))
                {
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

                    }
                    else
                    {
                        adddetailstodatabase(donationtype_txt,donationweight_txt,suggestedvehicle_txt,pincode_txt,address_txt,mobileno_txt,user.getUid(),resturant_txt);
                    }
                }
                else
                {
                    Toast.makeText(ADDDonations.this, "Please Select all Options", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void adddetailstodatabase(String donationtype, String donationweight, String suggestedvehicle, String pincode, String mobileno, String address, String userid, String resturant)
    {
            final Donations donations = new Donations(donationtype,donationweight,suggestedvehicle,pincode,mobileno,address,userid,resturant);
            final String key = db.getReference().push().getKey();

            db.getReference("Donations")
                    .child(key)
                    .setValue(donations).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ADDDonations.this, "Donation Sent to Rider", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ADDDonations.this, DonorDashboard.class));
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            }
                            else {
                                Toast.makeText(ADDDonations.this, "Failed to Sent to the Rider", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }

    private String getdonationtype(RadioGroup group)
    {
        if(group.getCheckedRadioButtonId() == R.id.ellipse_11)
        {
            return "only people";
        }
        else if(group.getCheckedRadioButtonId() == R.id.ellipse_12)
        {
            return "only animal";
        }
        return "both";
    }

    private String getdonationweight(RadioGroup group)
    {
        if(group.getCheckedRadioButtonId() == R.id.ellipse_15)
        {
            return "1-10kg";
        }
        else if(group.getCheckedRadioButtonId() == R.id.ellipse_17)
        {
            return "10-20kg";
        }
        else if(group.getCheckedRadioButtonId() == R.id.ellipse_16)
        {
            return "20-30kg";
        }
        return "30-40kg";
    }

    private String getsuggestedvehicle(RadioGroup group)
    {
        if(group.getCheckedRadioButtonId() == R.id.ellipse_19)
        {
            return "Bike";
        }
        else if(group.getCheckedRadioButtonId() == R.id.ellipse_20)
        {
            return "Tempo";
        }
        else if(group.getCheckedRadioButtonId() == R.id.ellipse_21)
        {
            return "Car";
        }
        return "Van";
    }
}