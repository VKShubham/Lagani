package com.example.lagani20.RegisterLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lagani20.R;
import com.example.lagani20.classes.BasicUtils;
import com.example.lagani20.classes.EmailValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText email;
    TextView resetbtn;
    BasicUtils utils = new BasicUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email = findViewById(R.id.email);
        resetbtn = findViewById(R.id.resetbtn);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                if(txt_email.isEmpty())
                {
                    Toast.makeText(ForgetPassword.this, "Email can't be blank", Toast.LENGTH_SHORT).show();
                }
                else if(!EmailValidator.isValidEmail(txt_email))
                {
                    Toast.makeText(ForgetPassword.this, "Email Is Not Valid", Toast.LENGTH_SHORT).show();
                }
                else if(!utils.isNetworkAvailable(getApplication()))
                {
                    Toast.makeText(ForgetPassword.this, "Network is Not Available", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgetPassword.this, "Email Sent to ".concat(txt_email), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(ForgetPassword.this, "Please Check Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}