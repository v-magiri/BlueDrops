package com.riconets.bluedrop;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Update_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Update_Profile extends Fragment {
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Update_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Update_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Update_Profile newInstance(String param1, String param2) {
        Update_Profile fragment = new Update_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_update__profile, container, false);
        accountProfilePic=v.findViewById(R.id.customerProfilePic);
        fNameEditTxt=v.findViewById(R.id.fNameEditTxt);
        lNameEditTxt=v.findViewById(R.id.lNameEditTxt);
        phoneEditTxt=v.findViewById(R.id.phoneEditTxt);
        vendorAutoCompleteTxt=v.findViewById(R.id.vendorAutoComplete);
        accountProfilePic=v.findViewById(R.id.customerProfilePic);
        databaseReference=FirebaseDatabase.getInstance().getReference("Customers");
        UpdateProfileBtn=v.findViewById(R.id.updateProfileBtn);
        mAuth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        ShowCurrentProfile();
        accountProfilePic.setOnClickListener(view -> {
         //select Image to upload
            checkReadPermissions();
        });
        UpdateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNameEditTxt.setSelection(fNameEditTxt.getText().length());
                lNameEditTxt.setSelection(lNameEditTxt.getText().length());
                phoneEditTxt.setSelection(phoneEditTxt.getText().length());
                Update_FName=fNameEditTxt.getText().toString();
                Update_LName=lNameEditTxt.getText().toString();
                phoneNumber=phoneEditTxt.getText().toString();
                if(TextUtils.isEmpty(Update_FName) || TextUtils.isEmpty(Update_LName)
                        || TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getActivity(), "No Update can be done Please fill the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    UpdateProfile();
                }
            }
        });
        return v;
    }

    private void ShowCurrentProfile() {
        Bundle userDetails=getArguments();
        String FirstName=String.valueOf(userDetails.getString("FirstName"));
        String LastName=String.valueOf(userDetails.getString("LastName"));
        String PhoneNumber=String.valueOf(userDetails.getString("PhoneNumber"));
        ProfileUri=String.valueOf(userDetails.getString("ProfileUri"));
        fNameEditTxt.setText(FirstName);
        lNameEditTxt.setText(LastName);
        phoneEditTxt.setText(PhoneNumber);
        fNameEditTxt.setSelection(fNameEditTxt.getText().length());
        lNameEditTxt.setSelection(lNameEditTxt.getText().length());
        phoneEditTxt.setSelection(phoneEditTxt.getText().length());
        if(!TextUtils.isEmpty(ProfileUri)) {
            Picasso.get().load(ProfileUri).into(accountProfilePic);
        }
    }

    private void checkReadPermissions() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            SelectProfilePic();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SelectProfilePic();
        }
        else{
            FancyToast.makeText(getActivity(),"Please Allow Read from Storage Permission",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
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
                                        Uri ProfileUri=uri;
                                        ProfilePicUri=uri.toString();
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void UpdateProfile() {
        String userID=mAuth.getCurrentUser().getUid();
        databaseReference.child(userID).child("firstName").setValue(Update_FName);
        databaseReference.child(userID).child("lastName").setValue(Update_LName);
        databaseReference.child(userID).child("phoneNumber").setValue(phoneNumber);
        if (TextUtils.isEmpty(ProfilePicUri)) {
            ProfilePicUri = "";
        }
        databaseReference.child(userID).child("profilePic").setValue(ProfilePicUri);
        Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();

    }

}