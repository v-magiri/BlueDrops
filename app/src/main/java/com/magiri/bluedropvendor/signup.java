package com.magiri.bluedropvendor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class signup extends AppCompatActivity {
    private Button continueBtn;
    private TextView signinTxt;
    private EditText NameEditTxt,EmailEditTxt,phoneEditTxt,LocationEditTxt,PasswordEditTxt,repeatPassEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Binding the UI Components
        NameEditTxt=findViewById(R.id.BizNameEditTxt);
        EmailEditTxt=findViewById(R.id.EmailEditText);
        phoneEditTxt=findViewById(R.id.phoneEditTxt);
        LocationEditTxt=findViewById(R.id.location);
        PasswordEditTxt=findViewById(R.id.password);
        repeatPassEditTxt=findViewById(R.id.repeat_Password);

        continueBtn=findViewById(R.id.nextBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String BizName=NameEditTxt.getText().toString().trim();
        String email=EmailEditTxt.getText().toString().trim();
        String phoneNumber=phoneEditTxt.getText().toString().trim();
        String location=LocationEditTxt.getText().toString().trim();
        String password=PasswordEditTxt.getText().toString().trim();
        String repeatPass=repeatPassEditTxt.getText().toString().trim();

        //Input Validation
        if(TextUtils.isEmpty(BizName)){
            NameEditTxt.setError("Name can not Be Empty");
            NameEditTxt.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(email)){
            EmailEditTxt.setError("Name can not Be Empty");
            EmailEditTxt.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            phoneEditTxt.setError("Name can not Be Empty");
            phoneEditTxt.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(location)){
            LocationEditTxt.setError("Name can not Be Empty");
            LocationEditTxt.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            PasswordEditTxt.setError("Name can not Be Empty");
            PasswordEditTxt.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(repeatPass)){
            repeatPassEditTxt.setError("Name can not Be Empty");
            repeatPassEditTxt.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailEditTxt.setError("Invaliid Email");
            EmailEditTxt.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(phoneNumber).matches()){
            phoneEditTxt.setError("Incorrect Phonenumber");
            phoneEditTxt.requestFocus();
            return;
        }



    }
}