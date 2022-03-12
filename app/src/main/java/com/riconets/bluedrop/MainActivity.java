package com.riconets.bluedrop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference,mRef;
    String VendorID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Roles");
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userId =mAuth.getUid();
//        getVendorId();
        if(userId!=null){
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userType=snapshot.child("UserType").getValue().toString();
                    if(userType.equals("Customer")) {
//                        startActivity(new Intent(getApplicationContext(), CustomerHome.class));
                        Intent intent=new Intent(getApplicationContext(),CustomerHome.class);
                        intent.putExtra("VendorId",VendorID);
                        startActivity(intent);
                        finish();
                    }else{
                        startActivity(new Intent(getApplicationContext(), customer_login.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            startActivity(new Intent(getApplicationContext(),customer_registration.class));
        }
    }
//    private void getVendorId() {
//        mRef= FirebaseDatabase.getInstance().getReference("Customers");
//        String UID=mAuth.getUid();
//        if(UID!=null) {
//            mRef.child(UID).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    VendorID = snapshot.child("vendorID").getValue().toString();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//    }
}