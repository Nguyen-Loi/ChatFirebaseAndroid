package com.nguyenloi.chatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Button btnLoginLogin,btnLoginSighUp;
    TextView tvLoginForgetPassword;
    TextInputEditText edtLoginEmail,edtLoginPasssword;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        btnLoginSighUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        tvLoginForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtLoginEmail.getText().toString();
                String password = edtLoginPasssword.getText().toString();

                if(!email.equals("")&&!password.equals("")){
                    signin(email,password);
                }else{
                    Toast.makeText(LoginActivity.this, "Please enter an email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setControl() {
        btnLoginLogin=findViewById(R.id.btnLoginLogin);
        btnLoginSighUp=findViewById(R.id.btnLoginSighUp);
        tvLoginForgetPassword=findViewById(R.id.tvLoginForgetPassword);
        edtLoginEmail=findViewById(R.id.edtLoginEmail);
        edtLoginPasssword=findViewById(R.id.edtLoginPasssword);

    }

    public  void signin(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Login is sucessfull", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login is unsucessfull", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}