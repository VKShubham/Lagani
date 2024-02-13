package com.example.lagani20.RegisterLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lagani20.Dashboard.DonorDashboard;
import com.example.lagani20.Dashboard.RiderDashboard;
import com.example.lagani20.R;
import com.example.lagani20.classes.BasicUtils;
import com.example.lagani20.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Boolean remme = false;
    TextView register;
    EditText email;
    EditText password;
    ImageView showpasswordbtn;
    ImageView checkbox;
    ImageView check;
    TextView forgetpassword;
    TextView login;
    ProgressDialog dialog;
    FirebaseAuth auth;
    FirebaseDatabase db;
    private boolean passwordvisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register =  findViewById(R.id.register);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        showpasswordbtn =  findViewById(R.id.showpass);
        checkbox =  findViewById(R.id.checkbox);
        check =  findViewById(R.id.group_1);
        forgetpassword =  findViewById(R.id.forget_pass);
        login =  findViewById(R.id.resetbtn);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        BasicUtils utils = new BasicUtils();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ProgressDialog progressDialog;

        if(user != null)
        {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            db.getReference().child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userobj = snapshot.getValue(User.class);
                    if(userobj.userType == 1)
                    {
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, DonorDashboard.class));
                        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                    }
                    else
                    {
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, RiderDashboard.class));
                        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uemail = email.getText().toString();
                String upassword = password.getText().toString();
                if(uemail.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Email cant't be blank",Toast.LENGTH_SHORT).show();
                }
                else if(upassword.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Password can't be blank",Toast.LENGTH_SHORT).show();
                }
                else if(!utils.isNetworkAvailable(getApplication()))
                {
                    Toast.makeText(MainActivity.this, "Network is Not Available", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Logging In..");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    loginuser(uemail,upassword);
                }
            }
        });

        // If User Click on The Register Button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Remember me CheckBoX Logic
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check.getVisibility() == View.INVISIBLE)
                {
                    check.setVisibility(View.VISIBLE);
                    remme = true;
                }
                else
                {
                    check.setVisibility(View.INVISIBLE);
                    remme = false;
                }
            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ForgetPassword.class));
                finish();
                overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            }
        });

        showpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordvisible)
                {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    passwordvisible = false;
                    showpasswordbtn.setImageResource(R.drawable.ic_password_toggle_off);
                }
                else
                {
                    password.setTransformationMethod(null);
                    passwordvisible = true;
                    showpasswordbtn.setImageResource(R.drawable.ic_password_toggle_on);
                }
                password.setSelection(password.getText().length());
            }
        });
    }

    private void loginuser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    // Getting User Type
                    db.getReference().child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userobj = snapshot.getValue(User.class);
                            if(userobj.userType == 1)
                            {
                                dialog.dismiss();
                                startActivity(new Intent(MainActivity.this, DonorDashboard.class));
                                overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                            }
                            else
                            {
                                dialog.dismiss();
                                startActivity(new Intent(MainActivity.this, RiderDashboard.class));
                                overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}