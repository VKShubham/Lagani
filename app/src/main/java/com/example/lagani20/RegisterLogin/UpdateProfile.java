package com.example.lagani20.RegisterLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lagani20.R;
import com.example.lagani20.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;



public class UpdateProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profilephoto;
    private EditText name;
    private ImageView back;
    private EditText mobileno;
    private EditText oldpassword;
    private TextView email;
    private EditText newpassword;
    private ImageButton updatebtn;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private FirebaseUser fuser;
    private FirebaseStorage firebaseStorage;
    private TextView uploadphpto;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private StorageReference ref;
    private Uri uri;
    ProgressBar bar;

    private void imagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            uploadImage(selectedImageUri);
        }
    }

    private void uploadImage(Uri selectedImageUri) {
        String filename = fuser.getUid();
        StorageReference ref = storageReference.child("images/" + filename);

        progressDialog.show();

        ref.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            ref.getFile(new File(getCacheDir(), "temp.jpg")).addOnSuccessListener(taskSnapshot1 -> {
                Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
                profilephoto.setImageBitmap(bitmap);
            });
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Photo Is Not Uploaded", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        profilephoto = findViewById(R.id.person_img);
        name = findViewById(R.id.lagni_team);
        mobileno = findViewById(R.id.mno);
        email = findViewById(R.id.email);
        oldpassword = findViewById(R.id.oldpass);
        newpassword = findViewById(R.id.newpass);
        updatebtn = findViewById(R.id.update_btn);
        uploadphpto = findViewById(R.id.upload_new_);
        back = findViewById(R.id.back_btn);

        bar = new ProgressBar(this);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        fuser = auth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");

        uploadphpto.setOnClickListener(view -> imagePicker());
        bar.setVisibility(View.VISIBLE);
        ref = storageReference.child("images/" + fuser.getUid());

        Bitmap bitmap = BitmapFactory.decodeFile(new File(getCacheDir(), "temp.jpg").getAbsolutePath());
        profilephoto.setImageBitmap(bitmap);

        db.getReference("Users").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name.setText(user.name);
                mobileno.setText(user.contact_no);
                email.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        updatebtn.setOnClickListener(view -> {
            String name_txt = name.getText().toString();
            String email_txt = email.getText().toString();
            String mobileno_txt = mobileno.getText().toString();
            String oldpassword_txt = oldpassword.getText().toString();
            String newpassword_txt = newpassword.getText().toString();

            if (TextUtils.isEmpty(oldpassword_txt) && TextUtils.isEmpty(newpassword_txt)) {
                db.getReference("Users").child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        if (currentUser != null) {
                            int userType = currentUser.userType;
                            if (!TextUtils.isEmpty(name_txt) && !TextUtils.isEmpty(mobileno_txt)) {
                                User userUpdate = new User(name_txt, email_txt, mobileno_txt, userType);
                                db.getReference("Users").child(fuser.getUid()).setValue(userUpdate).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UpdateProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateProfile.this, "Update Failed", Toast.LENGTH_LONG).show();
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
            } else {
                db.getReference("Users").child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        if (currentUser != null) {
                            int userType = currentUser.userType;
                            if (!TextUtils.isEmpty(name_txt) && !TextUtils.isEmpty(mobileno_txt)) {
                                User userUpdate = new User(name_txt, email_txt, mobileno_txt, userType);
                                db.getReference("Users").child(fuser.getUid()).setValue(userUpdate).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        auth.confirmPasswordReset(oldpassword_txt, newpassword_txt).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(UpdateProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(UpdateProfile.this, "Wrong Exist Password", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(UpdateProfile.this, "Update Failed", Toast.LENGTH_LONG).show();
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
        });
    }
}
