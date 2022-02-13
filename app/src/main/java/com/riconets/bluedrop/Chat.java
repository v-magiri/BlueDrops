package com.riconets.bluedrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    private ImageView backBtn,sendBtn;
    private EditText messageEditTxt;
    CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        backBtn=findViewById(R.id.ChatBackBtn);
        sendBtn=findViewById(R.id.sendBtn);
        messageEditTxt=findViewById(R.id.customerMessage);

        //redirect to the vendor Details Page
        backBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),VendorDetails.class)));
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=messageEditTxt.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(getApplicationContext(),"Message can not be Empty",Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage();
                }
                messageEditTxt.setText("");
            }
        });
    }

    private void sendMessage() {

    }
}