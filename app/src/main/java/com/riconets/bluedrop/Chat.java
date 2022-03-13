package com.riconets.bluedrop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.Adapters.ChatAdapter;
import com.riconets.bluedrop.model.ChatModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    private ImageView backBtn,sendBtn;
    private EditText messageEditTxt;
    CircleImageView profilePic;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<com.riconets.bluedrop.model.ChatModel> messageList;
    private TextView SenderNameTxt;
    FirebaseAuth mAuth;
    String senderName,vendorUserName,message,CustomerUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        backBtn=findViewById(R.id.ChatBackBtn);
        sendBtn=findViewById(R.id.sendBtn);
        SenderNameTxt=findViewById(R.id.vendorName);
        messageEditTxt=findViewById(R.id.customerMessage);
        chatRecyclerView=findViewById(R.id.chatRecyclerView);
        messageList=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        Bundle chatExtras=getIntent().getExtras();
        if(chatExtras!=null){
            senderName=chatExtras.getString("VendorName");
            vendorUserName=chatExtras.getString("VendorUserName");
            CustomerUserName=chatExtras.getString("UserName");
        }
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter=new ChatAdapter(this,messageList,CustomerUserName);
        chatRecyclerView.setAdapter(chatAdapter);
        getMessages();
        //redirect to the vendor Details Page
        backBtn.setOnClickListener(v -> {
            finish();
        });
        sendBtn.setOnClickListener(view -> {
            message=messageEditTxt.getText().toString().trim();
            if(TextUtils.isEmpty(message)){
                Toast.makeText(getApplicationContext(),"Message can not be Empty",Toast.LENGTH_SHORT).show();
            }else{
                sendMessage();
            }
            messageEditTxt.setText("");
        });
    }

//    private void getCustomerUserName() {
//    }

    private void getMessages() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats").child(CustomerUserName).child(vendorUserName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatModel chat=dataSnapshot.getValue(ChatModel.class);
                    messageList.add(chat);
                }
                chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage() {
        DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy @HH:mm", Locale.US);
        String date=dateFormat.format(new Date());
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        ChatModel chatModel=new ChatModel(message,date,CustomerUserName,vendorUserName);
        databaseReference.child(CustomerUserName).child(vendorUserName).push().setValue(chatModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SenderNameTxt.setText(senderName);
    }
}