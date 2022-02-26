package com.riconets.bluedrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    LinearLayout notify_Vendor;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    String VendorID;
    CardView refillCard,BottledWaterCard,vendorCard,AccessoriesCard;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        refillCard=v.findViewById(R.id.refillCard);
        BottledWaterCard=v.findViewById(R.id.bottledCard);
        vendorCard=v.findViewById(R.id.changeVendor);
        AccessoriesCard=v.findViewById(R.id.accessories);
        mAuth=FirebaseAuth.getInstance();
        notify_Vendor=v.findViewById(R.id.notify);
        getVendorId();
            notify_Vendor.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), NotifyVendor.class);
                startActivity(intent);
            });
            refillCard.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), Refill.class);
                String type = "Refill";
                intent.putExtra("ProductType", type);
                startActivity(intent);
            });
            AccessoriesCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ViewProducts.class);
                    String type = "Accessories";
                    intent.putExtra("ProductType", type);
                    intent.putExtra("VendorID", VendorID);
                    startActivity(intent);
                }
            });
            vendorCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UpdateVendor.class);
                    startActivity(intent);
                }
            });
            BottledWaterCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ViewProducts.class);
                    String type = "Bottled Water";
                    intent.putExtra("ProductType", type);
                    intent.putExtra("VendorID", VendorID);
                    startActivity(intent);
                }
            });
        return v;
    }

    private void getVendorId() {
        mRef= FirebaseDatabase.getInstance().getReference("Customers");
        String UID=mAuth.getUid();
        if(UID!=null) {
            mRef.child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    VendorID = snapshot.child("vendorID").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}