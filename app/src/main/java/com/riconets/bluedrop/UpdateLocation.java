package com.riconets.bluedrop;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.essam.simpleplacepicker.MapActivity;
import com.essam.simpleplacepicker.utils.SimplePlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.model.VendorModel;

import java.util.ArrayList;

public class UpdateLocation extends AppCompatActivity {
    private ImageView backBtn,logoutBtn;
    AutoCompleteTextView vendorAutoComplete;
    private TextView updatedAddressTxt;
    String updatedLatitude,updatedLongitude,updatedAddress,VendorID;
    private ImageView placePicker;
    ArrayList<String> vendorNames;
    ArrayAdapter<String> vendorAdapter;
    ArrayList<String> vendorIds;
    String Country;
    String Language;
    String []mSupportedAreas={""};
    private Button UpdateBtn;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
        logoutBtn=findViewById(R.id.logoutBtn);
        logoutBtn.setVisibility(View.GONE);
        placePicker=findViewById(R.id.placePicker);
        UpdateBtn=findViewById(R.id.updateLocationBtn);
        updatedAddressTxt=findViewById(R.id.updatedAddress);
        vendorAutoComplete=findViewById(R.id.vendorAutoComplete);
        vendorNames=new ArrayList<>();
        mRef=FirebaseDatabase.getInstance().getReference("Customers");
        vendorIds=new ArrayList<>();
        getVendor();
        vendorAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,vendorNames);
        vendorAutoComplete.setAdapter(vendorAdapter);
        vendorAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            VendorID=vendorIds.get(position);
            Log.d(TAG, "onCreate: "+VendorID);
        });
        placePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMapPermission(UpdateLocation.this,1, Manifest.permission.ACCESS_FINE_LOCATION)){
                    selectlocation();
                }
            }
        });
        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UID= FirebaseAuth.getInstance().getUid();
                if(updatedAddress!=null&&updatedLatitude!=null&&updatedLongitude!=null) {
                    mRef.child(UID).child("Location").setValue(updatedAddress);
                }
            }
        });
    }
    public static boolean checkMapPermission(Activity activity, int requestCode, String permissionName) {
        if (ContextCompat.checkSelfPermission(activity,
                permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionName},
                    requestCode);
        } else {
            return true;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectlocation();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null) ShowSelectedAddress(data);
        }

    }
    private void getVendor() {
        FirebaseDatabase.getInstance().getReference("Vendors").addListenerForSingleValueEvent(new ValueEventListener() {
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
    private void selectlocation() {
        Country="Kenya";
        Language="English";
        String ApiKey="AIzaSyDbiqrB6Ar5n1EMhqdvk_cH0Uq2_u51EmA";
        showMap(ApiKey,Country,Language,mSupportedAreas);
    }
    private void ShowSelectedAddress(Intent data) {
        updatedAddress=data.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS);
        updatedAddressTxt.setText(updatedAddress);
        updatedLatitude=String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,-1));
        updatedLongitude=String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,-1));
    }
    private void showMap(String apiKey, String country, String language, String[] mSupportedAreas) {
        Intent intent=new Intent(getApplicationContext(), MapActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(SimplePlacePicker.API_KEY,apiKey);
        bundle.putString(SimplePlacePicker.COUNTRY,country);
        bundle.putString(SimplePlacePicker.LANGUAGE,language);
        bundle.putStringArray(SimplePlacePicker.SUPPORTED_AREAS,mSupportedAreas);

        intent.putExtras(bundle);
        startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE);
    }
}