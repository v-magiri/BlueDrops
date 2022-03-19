package com.riconets.bluedrop;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.model.VendorModel;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateVendor extends AppCompatActivity {
    private ImageView backBtn;
    private CircleImageView logoutBtn;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference,mRef;
    ArrayList<String> vendorNames;
    ArrayAdapter<String> vendorAdapter;
    Button updateVendorBtn;
    ArrayList<String> vendorIds;
    private TextView CurrentVendorTxt;
    private EditText vendorReviewTxt;
    private ImageView editVendor;
    TextInputLayout vendorsTextInputLayout;
    AutoCompleteTextView vendorAutoComplete;
    String VendorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendor);
        logoutBtn=findViewById(R.id.logoutBtn);
        mAuth=FirebaseAuth.getInstance();
        backBtn=findViewById(R.id.backBtn);
        vendorAutoComplete=findViewById(R.id.updateVendorAuto);
        backBtn.setOnClickListener(v -> finish());
        logoutBtn.setVisibility(View.GONE);
        updateVendorBtn=findViewById(R.id.changeVendorBtn);
        CurrentVendorTxt=findViewById(R.id.currentVendor);
        vendorNames=new ArrayList<>();
        vendorsTextInputLayout=findViewById(R.id.vendors);
        logoutBtn.setVisibility(View.GONE);
        vendorIds=new ArrayList<>();
        editVendor=findViewById(R.id.vendorEdit);
        editVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorsTextInputLayout.setVisibility(View.VISIBLE);
            }
        });
        vendorReviewTxt=findViewById(R.id.changeVendorTxt);

        databaseReference=FirebaseDatabase.getInstance().getReference("Customers");
        showCurrentVendor();
        mRef=FirebaseDatabase.getInstance().getReference("Vendors");
        getVendor();
        vendorAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,vendorNames);
        vendorAutoComplete.setAdapter(vendorAdapter);
        vendorAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String vendorName=vendorNames.get(position);
            VendorID=vendorIds.get(position);
            Log.d(TAG, "onCreate: "+VendorID);
        });
        updateVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vendorReview=vendorReviewTxt.getText().toString().trim();
                String UID=mAuth.getUid();
                databaseReference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!vendorReview.equals("")) {
                            String currentVendorID = snapshot.child("vendorID").getValue().toString();
                            String customerUserName = snapshot.child("userName").getValue().toString();
                            HashMap<String,String> map=new HashMap<>();
                            map.put(customerUserName,vendorReview);
                            mRef.child(currentVendorID).child("Review").setValue(map);
                        }
                        FirebaseDatabase.getInstance().getReference("Customers").child(UID).child("vendorID").setValue(VendorID);
                        Toast.makeText(getApplicationContext(),"Vendor Changed",Toast.LENGTH_LONG).show();
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void showCurrentVendor() {
        String UID=FirebaseAuth.getInstance().getUid();
        databaseReference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String VendorId=snapshot.child("vendorID").getValue().toString();
                FirebaseDatabase.getInstance().getReference("Vendors").child(VendorId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String VendorName=snapshot.child("name").getValue().toString();
                                CurrentVendorTxt.setText(VendorName);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "onCancelled: "+error.getMessage() );
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: "+error.getMessage() );
            }
        });
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