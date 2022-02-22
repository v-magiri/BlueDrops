package com.riconets.bluedrop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {
    private CardView OrderCard,ProfileCard,LocationCard,updateVendorCard;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
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
        View v=inflater.inflate(R.layout.fragment_account, container, false);
        OrderCard=v.findViewById(R.id.myOrder);
        ProfileCard=v.findViewById(R.id.profile);
        mAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference("Customers");
        updateVendorCard=v.findViewById(R.id.changeVendor);
        LocationCard=v.findViewById(R.id.updateLocation);
        OrderCard.setOnClickListener(view -> startActivity(new Intent(getActivity(),VendorDetails.class)));
        LocationCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),UpdateLocation.class);
            startActivity(intent);
        });
        updateVendorCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),UpdateVendor.class);
            startActivity(intent);
        });
        ProfileCard.setOnClickListener(view -> {
//            String UID=mAuth.getUid();
//            databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String Name=snapshot.child("name").getValue().toString();
//                    String PhoneNumber=snapshot.child("phoneNumber").getValue().toString();
//                    String Location=snapshot.child("location").getValue().toString();
//                    String ProfilePic=snapshot.child("profilePic").getValue().toString();
//                    String userName=snapshot.child("userName").getValue().toString();
//                    String email=snapshot.child("email").getValue().toString();
//                    Intent intent = new Intent(getActivity(),UpdateProfile.class);
//                    Bundle userDetails=new Bundle();
//                    userDetails.putString("userName",userName);
//                    userDetails.putString("Name",Name);
//                    userDetails.putString("phoneNumber",PhoneNumber);
//                    userDetails.putString("location",Location);
//                    userDetails.putString("email",email);
//                    userDetails.putString("ProfilePic",ProfilePic);
//                    intent.putExtras(userDetails);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            Intent intent = new Intent(getActivity(),UpdateProfile.class);
            startActivity(intent);
        });
        return v;
    }
}