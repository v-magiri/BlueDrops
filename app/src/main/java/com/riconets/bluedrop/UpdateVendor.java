package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.model.VendorModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateVendor extends AppCompatActivity {
    private ImageView backBtn;
    private CircleImageView logoutBtn;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference,mRef;
    ArrayList<String> vendorNames;
    ArrayAdapter<String> vendorAdapter;
    ArrayList<String> vendorIds;
    AutoCompleteTextView vendorAutoComplete;
    String VendorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendor);
        logoutBtn=findViewById(R.id.logoutBtn);
        mAuth=FirebaseAuth.getInstance();
        backBtn=findViewById(R.id.backBtn);
        vendorAutoComplete=findViewById(R.id.updateVendorAutoC);
        backBtn.setOnClickListener(v -> finish());
        logoutBtn.setVisibility(View.GONE);
        vendorNames=new ArrayList<>();
        logoutBtn.setVisibility(View.GONE);
        vendorIds=new ArrayList<>();

        databaseReference=FirebaseDatabase.getInstance().getReference("Customers");
        mRef=FirebaseDatabase.getInstance().getReference("Vendors");
        getVendor();
        vendorAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,vendorNames);
        vendorAutoComplete.setAdapter(vendorAdapter);
        vendorAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            VendorID=vendorIds.get(position);
        });
//        logoutBtn.setOnClickListener(view -> {
//            AlertDialog.Builder logoutAlertDialog=new AlertDialog.Builder(UpdateVendor.this);
//            logoutAlertDialog.setTitle("Confirm Logout");
//            logoutAlertDialog.setIcon(R.drawable.icon_bluedrops);
//            logoutAlertDialog.setMessage("Are you sure you want to logout");
//            logoutAlertDialog.setCancelable(false);
//            logoutAlertDialog.setPositiveButton("Yes", (dialog, which) -> {
//                mAuth.signOut();
//                startActivity(new Intent(getApplicationContext(),customer_login.class));
//            });
//            logoutAlertDialog.setNegativeButton("No", (dialog, which) ->
//                    dialog.cancel());
//            AlertDialog alertDialog = logoutAlertDialog.create();
//            alertDialog.show();
//        });

    }

    private void getVendor() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    VendorModel vendorModel=dataSnapshot.getValue(VendorModel.class);
                    vendorNames.add(vendorModel.getName());
                    vendorIds.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}