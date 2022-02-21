package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {
    private CircleImageView accountProfilePic;
    private EditText fNameEditTxt,lNameEditTxt,phoneEditTxt;
    private AutoCompleteTextView vendorAutoCompleteTxt;
    private Button UpdateProfileBtn;
    private FirebaseStorage storage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST= 22;
    private Uri imageUri;
    FirebaseAuth mAuth;
    String ProfilePicUri,Update_FName,Update_LName,phoneNumber, ProfileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        accountProfilePic=findViewById(R.id.customerProfilePic);
        fNameEditTxt=findViewById(R.id.fNameEditTxt);
        lNameEditTxt=findViewById(R.id.lNameEditTxt);
        phoneEditTxt=findViewById(R.id.phoneEditTxt);
        vendorAutoCompleteTxt=findViewById(R.id.vendorAutoComplete);
        accountProfilePic=findViewById(R.id.customerProfilePic);
        databaseReference= FirebaseDatabase.getInstance().getReference("Customers");
        UpdateProfileBtn=findViewById(R.id.updateProfileBtn);
        mAuth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
//        ShowCurrentProfile();
        accountProfilePic.setOnClickListener(view -> {
            //select Image to upload
            checkReadPermissions();
        });
        UpdateProfileBtn.setOnClickListener(v1 -> {
            fNameEditTxt.setSelection(fNameEditTxt.getText().length());
            lNameEditTxt.setSelection(lNameEditTxt.getText().length());
            phoneEditTxt.setSelection(phoneEditTxt.getText().length());
            Update_FName=fNameEditTxt.getText().toString();
            Update_LName=lNameEditTxt.getText().toString();
            phoneNumber=phoneEditTxt.getText().toString();
            if(TextUtils.isEmpty(Update_FName) || TextUtils.isEmpty(Update_LName)
                    || TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(getApplicationContext(), "No Update can be done Please fill the fields", Toast.LENGTH_SHORT).show();
            }
            else{
                UpdateProfile();
            }
        });

    }
//    private void ShowCurrentProfile() {
////        Bundle userDetails=getArguments();
//        String FirstName=String.valueOf(userDetails.getString("FirstName"));
//        String LastName=String.valueOf(userDetails.getString("LastName"));
//        String PhoneNumber=String.valueOf(userDetails.getString("PhoneNumber"));
//        ProfileUri=String.valueOf(userDetails.getString("ProfileUri"));
//        fNameEditTxt.setText(FirstName);
//        lNameEditTxt.setText(LastName);
//        phoneEditTxt.setText(PhoneNumber);
//        fNameEditTxt.setSelection(fNameEditTxt.getText().length());
//        lNameEditTxt.setSelection(lNameEditTxt.getText().length());
//        phoneEditTxt.setSelection(phoneEditTxt.getText().length());
//        if(!TextUtils.isEmpty(ProfileUri)) {
//            Picasso.get().load(ProfileUri).into(accountProfilePic);
//        }
//    }
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
        databaseReference.child(userID).child("firstName").setValue(Update_FName);
        databaseReference.child(userID).child("lastName").setValue(Update_LName);
        databaseReference.child(userID).child("phoneNumber").setValue(phoneNumber);
        if (IsProfilePicChanged()) {
            databaseReference.child(userID).child("profilePic").setValue(ProfilePicUri);
        }
        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
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

}