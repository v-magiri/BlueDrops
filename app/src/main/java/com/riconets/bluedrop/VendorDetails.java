package com.riconets.bluedrop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VendorDetails extends AppCompatActivity {
    private Button messageBtn,CallBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private CircleImageView vendorLogo;
    ProgressDialog progressDialog;
    private ImageView backBtn,logoutBtn;
    private TextView nameTxt,vendorDescTxt,LocationTxt;
    String VendorId,customerUserName,VendorName,VendorLogoUri,vendorDesc,Vendor_Address,vendorUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);
        messageBtn=findViewById(R.id.messageBtn);
        CallBtn=findViewById(R.id.callBtn);
        backBtn=findViewById(R.id.backBtn);
        progressDialog=new ProgressDialog(this);
        logoutBtn=findViewById(R.id.logoutBtn);
        vendorLogo=findViewById(R.id.vendorLogo);
        nameTxt=findViewById(R.id.vendorNameTxt);
        vendorDescTxt=findViewById(R.id.vendorDesc);
        mAuth=FirebaseAuth.getInstance();
        VendorId="2tc6TyLdpMZf0OVcIrOOq6wcR733";
        LocationTxt=findViewById(R.id.vendorLocation);
        progressDialog.setMessage("Loading Details");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        ShowVendorDetails();
        backBtn.setOnClickListener(v -> finish());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder logoutAlertDialog=new AlertDialog.Builder(VendorDetails.this);
                logoutAlertDialog.setTitle("Confirm Logout");
                logoutAlertDialog.setIcon(R.drawable.icon_bluedrops);
                logoutAlertDialog.setMessage("Are you sure you want to logout");
                logoutAlertDialog.setCancelable(false);
                logoutAlertDialog.setPositiveButton("Yes", (dialog, which) -> {
                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(),customer_login.class));
                });
                logoutAlertDialog.setNegativeButton("No", (dialog, which) ->
                        dialog.cancel());
                AlertDialog alertDialog = logoutAlertDialog.create();
                alertDialog.show();
            }
        });
        getCustomerUserName();
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(VendorName) || TextUtils.isEmpty(VendorId) || TextUtils.isEmpty(customerUserName)){
                    return;
                }else{
                    Intent intent=new Intent(getApplicationContext(),Chat.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("VendorName",VendorName);
                    bundle.putString("VendorUserName",vendorUserName);
                    bundle.putString("UserName",customerUserName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void getCustomerUserName() {
        DatabaseReference Mref=FirebaseDatabase.getInstance().getReference("Customers");
        String userId=mAuth.getUid();
        if(userId!=null){
            Mref.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    customerUserName=snapshot.child("userName").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void ShowVendorDetails() {
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Vendors");
        databaseReference.child(VendorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                VendorModel vendorModel=snapshot.getValue(VendorModel.class);
                VendorLogoUri=snapshot.child("profile_Pic").getValue().toString();
                VendorName=snapshot.child("name").getValue().toString();
                vendorDesc=snapshot.child("vendor_Desc").getValue().toString();
                Vendor_Address=snapshot.child("address").getValue().toString();
                vendorUserName=snapshot.child("userName").getValue().toString();
                nameTxt.setText(VendorName);
                vendorDescTxt.setText(vendorDesc);
                LocationTxt.setText(Vendor_Address);
                if(!VendorLogoUri.equals("")) {
                    Picasso.get().load(VendorLogoUri).into(vendorLogo);
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}