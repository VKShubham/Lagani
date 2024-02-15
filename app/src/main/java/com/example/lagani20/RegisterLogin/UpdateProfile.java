package com.example.lagani20.RegisterLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lagani20.R;
import com.example.lagani20.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    ImageView profilephoto;
    EditText name;
    EditText mobileno;
    EditText oldpassword;
    TextView email;
    EditText newpassword;
    ImageButton updatebtn;
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        profilephoto = findViewById(R.id.ellipse_10);
        name = findViewById(R.id.lagni_team);
        mobileno = findViewById(R.id.mno);
        email = findViewById(R.id.email);
        oldpassword = findViewById(R.id.oldpass);
        newpassword = findViewById(R.id.newpass);
        updatebtn = findViewById(R.id.update_btn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        fuser = auth.getCurrentUser();


        db.getReference("Users")
                .child(fuser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user =  snapshot.getValue(User.class);
                        name.setText(user.name);
                        mobileno.setText(user.contact_no);
                        email.setText(user.email);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        String name_txt = name.getText().toString();
        String email_txt = email.getText().toString();
        String mobileno_txt =mobileno.getText().toString();

        updatebtn.setOnClickListener(new View.OnClickListener() {

            String oldpassword_txt = oldpassword.getText().toString();
            String newpassword_txt = newpassword.getText().toString();
            @Override
            public void onClick(View view) {
                if(oldpassword_txt.isEmpty() && newpassword_txt.isEmpty())
                {
                    Log.d("Check","It will Goes In A PassWord Change");
                    final String name_txt = name.getText().toString();
                    final String mobileno_txt = mobileno.getText().toString();

                    db.getReference("Users")
                            .child(fuser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User currentUser = snapshot.getValue(User.class);
                                    if (currentUser != null) {
                                        int userType = currentUser.userType;
                                        if (!TextUtils.isEmpty(name_txt) && !TextUtils.isEmpty(mobileno_txt)) {
                                            User userUpdate = new User(name_txt, email_txt, mobileno_txt, userType);
                                            db.getReference("Users")
                                                    .child(fuser.getUid())
                                                    .setValue(userUpdate)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Check","Password update");
                                                                Toast.makeText(UpdateProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(UpdateProfile.this, "Update Failed", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(UpdateProfile.this, "Name and Mobile Number cannot be empty", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(UpdateProfile.this, "User data not available", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(UpdateProfile.this, "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    final String name_txt = name.getText().toString();
                    final String mobileno_txt = mobileno.getText().toString();
                    final String oldpassword_txt = oldpassword.getText().toString();
                    final String newpassword_txt = newpassword.getText().toString();

                    db.getReference("Users")
                            .child(fuser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User currentUser = snapshot.getValue(User.class);
                                    if (currentUser != null) {
                                        int userType = currentUser.userType;
                                        if (!TextUtils.isEmpty(name_txt) && !TextUtils.isEmpty(mobileno_txt)) {
                                            User userUpdate = new User(name_txt, email_txt, mobileno_txt, userType);
                                            db.getReference("Users")
                                                    .child(fuser.getUid())
                                                    .setValue(userUpdate)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                auth.confirmPasswordReset(oldpassword_txt,newpassword_txt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(UpdateProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                            }else {
                                                                                Toast.makeText(UpdateProfile.this, "Wrong Exist Password", Toast.LENGTH_LONG).show();
                                                                            }
                                                                    }
                                                                });
                                                            } else {
                                                                Toast.makeText(UpdateProfile.this, "Update Failed", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(UpdateProfile.this, "Name and Mobile Number cannot be empty", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(UpdateProfile.this, "User data not available", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(UpdateProfile.this, "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}