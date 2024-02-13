package com.example.lagani20.RegisterLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lagani20.R;
import com.example.lagani20.classes.BasicUtils;
import com.example.lagani20.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password;
    EditText confirmpassword;
    TextView register;
    EditText contactno;
    TextView loginbtn;
    ProgressDialog dialog;

    FirebaseAuth auth;
    FirebaseDatabase db;
    BasicUtils utils = new BasicUtils();

    @Override
    public void onBackPressed() {
         super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RadioGroup radioGroup = findViewById(R.id.usertype);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        confirmpassword = findViewById(R.id.confirm_password);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerbtn);
        contactno = findViewById(R.id.contact_number);
        loginbtn = findViewById(R.id.login);
        register = findViewById(R.id.registerbtn);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmpassword = confirmpassword.getText().toString();
                String txt_contactno = contactno.getText().toString();


                if(txt_name.isEmpty()){
                    Toast.makeText(Register.this,"Name is Invalid!",Toast.LENGTH_SHORT).show();
                }else if(!utils.isPhoneNoValid(txt_contactno)){ // need to revise it
                    Toast.makeText(Register.this,"Phone Number is Invalid!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txt_email)){
                    Toast.makeText(Register.this,"Email can't be blank!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txt_password) && !txt_password.equals(txt_confirmpassword)){
                    Toast.makeText(Register.this,"Password can't be blank and Should be Same!",Toast.LENGTH_SHORT).show();
                }else if(txt_password.length()<6){
                    Toast.makeText(Register.this,"Password too short!", Toast.LENGTH_SHORT).show();
                }
                else if(utils.isNetworkAvailable(getApplication()))
                {
                    dialog = new ProgressDialog(Register.this);
                    dialog.setMessage("Registering..");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();
                    registeruser(txt_name,txt_email,txt_password,txt_contactno,usertype(radioGroup.getCheckedRadioButtonId()));
                }
                else
                {
                    Toast.makeText(Register.this, "Network is Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private int usertype(int checkedId) {
        if(checkedId == R.id.rider)
        {
            return 2;
        }
        return 1;
    }


    // Register User In Database Function
    private void registeruser(String name, String email, String password, String contactno, int usertype)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            final User userobj = new User(name,email,contactno,usertype);
                            db.getReference("Users")
                                    .child(auth.getCurrentUser().getUid())
                                    .setValue(userobj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                dialog.dismiss();
                                                Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Register.this, MainActivity.class));
                                                finish();
                                            }
                                            else
                                            {
                                                dialog.dismiss();
                                                Toast.makeText(Register.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            dialog.dismiss();
                            Toast.makeText(Register.this,"Registration Failed",Toast.LENGTH_SHORT);
                        }

                    }
                });
    }
}