package com.riconets.bluedrop;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {
    private CircleImageView accountProfilePic;
    private EditText NameEditTxt,phoneEditTxt;
    private TextView locationTxt, vendorTxt;
    private Button UpdateProfileBtn;
    private FirebaseStorage storage;
    private CircleImageView logoutBtn;
    private ImageView backBtn,viewVendorDetails;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog,startProgressDialog;
    StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST= 22;
    private Uri imageUri;
    FirebaseAuth mAuth;
    String ProfilePicUri,Update_FName,Update_LName,phoneNumber, ProfileUri,vendorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        accountProfilePic=findViewById(R.id.customerProfilePic);
        NameEditTxt=findViewById(R.id.nameEditTxt);
        locationTxt=findViewById(R.id.customerLocationTxt);
        phoneEditTxt=findViewById(R.id.phoneEditTxt);
        viewVendorDetails=findViewById(R.id.viewVendorDetails);
        viewVendorDetails.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),VendorDetails.class)));
        logoutBtn=findViewById(R.id.logoutBtn);
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Customers");
        progressDialog=new ProgressDialog(this);
        startProgressDialog=new ProgressDialog(this);
        startProgressDialog.setMessage("Fetching Profile Details");
        startProgressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Updating Profile");
        backBtn=findViewById(R.id.backBtn);
        vendorTxt=findViewById(R.id.vendor);
        accountProfilePic=findViewById(R.id.customerProfilePic);
        UpdateProfileBtn=findViewById(R.id.updateProfileBtn);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        backBtn.setOnClickListener(v -> finish());
        logoutBtn.setVisibility(View.GONE);
        accountProfilePic.setOnClickListener(view -> {
            //select Image to upload
            checkReadPermissions();
        });
        UpdateProfileBtn.setOnClickListener(v1 -> {
            NameEditTxt.setSelection(NameEditTxt.getText().length());
            phoneEditTxt.setSelection(phoneEditTxt.getText().length());
            Update_FName=NameEditTxt.getText().toString();
            phoneNumber=phoneEditTxt.getText().toString();
            if(TextUtils.isEmpty(Update_FName) || TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(getApplicationContext(), "No Update can be done Please fill the fields", Toast.LENGTH_SHORT).show();
            }
            else{
                UpdateProfile();
            }
        });

    }
    private void checkReadPermissions() {
        if(ContextCompat.checkSelfPermission(UpdateProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            SelectProfilePic();
        }else{
            ActivityCompat.requestPermissions(UpdateProfile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_IMAGE_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SelectProfilePic();
        }
        else{
            FancyToast.makeText(UpdateProfile.this,"Please Allow Read from Storage Permission",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
        }
    }
    private void SelectProfilePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data!=null && data.getData() !=null ){
            try {
                imageUri=data.getData();
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                accountProfilePic.setImageBitmap(bitmap);
                uploadProfilePic();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void uploadProfilePic() {
            if(imageUri!=null){
            String fileName=String.valueOf(System.currentTimeMillis());
            storageReference.child("Images").child(fileName).putFile(imageUri)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            storageReference.child("Images").child(fileName).getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                            ProfilePicUri= uri.toString();
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void UpdateProfile() {
        String userID=mAuth.getCurrentUser().getUid();
        databaseReference.child(userID).child("name").setValue(Update_FName);
        databaseReference.child(userID).child("lastName").setValue(Update_LName);
        databaseReference.child(userID).child("phoneNumber").setValue(phoneNumber);
        if (IsProfilePicChanged()) {
            progressDialog.show();
            databaseReference.child(userID).child("profilePic").setValue(ProfilePicUri);
        }
        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        finish();
    }
    private boolean IsProfilePicChanged() {
        if ((!TextUtils.isEmpty(ProfilePicUri)) && (!TextUtils.isEmpty(ProfileUri))) {
            return true;
        } else if ((!TextUtils.isEmpty(ProfilePicUri)) && (ProfileUri.equals(""))){
            return  true;
        }else{
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startProgressDialog.show();
        String UID=mAuth.getUid();
            databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String Name=snapshot.child("name").getValue().toString();
                    String PhoneNumber=snapshot.child("phoneNumber").getValue().toString();
                    String Location=snapshot.child("location").getValue().toString();
                    ProfileUri=snapshot.child("profilePic").getValue().toString();
                    if(!ProfileUri.equals("")) {
                        Picasso.get().load(ProfileUri).into(accountProfilePic);
                    }
                    String VendorId=snapshot.child("vendorID").getValue().toString();
                    FirebaseDatabase.getInstance().getReference("Vendors").child(VendorId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String VendorName=snapshot.child("name").getValue().toString();
                                    vendorTxt.setText(VendorName);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e(TAG, "onCancelled: "+error.getMessage() );
                                }
                            });
                    locationTxt.setText(Location);
                    NameEditTxt.setText(Name);
                    phoneEditTxt.setText(PhoneNumber);
                    NameEditTxt.setSelection(NameEditTxt.getText().length());
                    phoneEditTxt.setSelection(phoneEditTxt.getText().length());
                    startProgressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}