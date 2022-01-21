package com.magiri.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

public class customer_login extends AppCompatActivity {
    private EditText userEmailEditTxt,passwordEditText;
    private Button loginBtn;
    private TextView createAccountTxt;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        userEmailEditTxt=findViewById(R.id.userID);
        passwordEditText=findViewById(R.id.password);
        loginBtn=findViewById(R.id.loginBtn);
        createAccountTxt=findViewById(R.id.createAccount);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();

        //redirect the usr to the registration  screen
        createAccountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),customer_registration.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String userEmail=userEmailEditTxt.getText().toString().trim();
        String Password=passwordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail)){
            userEmailEditTxt.setError("This Field Can not be Empty");
            userEmailEditTxt.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(Password)){
            passwordEditText.setError("Please Input Password");
            passwordEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            userEmailEditTxt.setError("Invalid Email");
            userEmailEditTxt.requestFocus();
            return;
        }
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Signing in");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userEmail,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FancyToast.makeText(getApplicationContext(),"Successfull Logged In", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    startActivity(new Intent(getApplicationContext(),CustomerHome.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                String reason=e.getMessage();
                FancyToast.makeText(getApplicationContext(),reason,FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

            }
        });
    }
}