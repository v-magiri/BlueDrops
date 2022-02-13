package com.riconets.bluedrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VendorDetails extends AppCompatActivity {
    private Button messageBtn,CallBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);
        messageBtn=findViewById(R.id.messageBtn);
        CallBtn=findViewById(R.id.callBtn);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Chat.class));
            }
        });
    }
}