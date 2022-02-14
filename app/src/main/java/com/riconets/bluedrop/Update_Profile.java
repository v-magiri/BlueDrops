package com.riconets.bluedrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.model.Customer;

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
    private static final int PICK_IMAGE_REQUEST= 22;
    private Uri imageUri;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
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
        UpdateProfileBtn=v.findViewById(R.id.updateProfileBtn);
        accountProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //select Image to upload
                SelectProfilePic();

            }
        });
        mAuth=FirebaseAuth.getInstance();
        UpdateProfileBtn.setOnClickListener(view -> UpdateProfile());

        return v;
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

    private void UpdateProfile() {
        String FirstName=fNameEditTxt.getText().toString().trim();
        String LastName=lNameEditTxt.getText().toString().trim();
        String PhoneNumber=phoneEditTxt.getText().toString().trim();
    }

    @Override
    public void onStart() {
        super.onStart();
        String CurrentUserID=mAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Customers");
        databaseReference.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String FirstName=snapshot.child("firstName").getValue().toString();
                String LastName=snapshot.child("lastName").getValue().toString();
                String PhoneNumber=snapshot.child("phoneNumber").getValue().toString();
                lNameEditTxt.setText(LastName);
                phoneEditTxt.setText(PhoneNumber);
                fNameEditTxt.setText(FirstName);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}