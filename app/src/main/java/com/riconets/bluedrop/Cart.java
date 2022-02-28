package com.riconets.bluedrop;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.Adapters.CartAdapter;
import com.riconets.bluedrop.model.CartModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView cartRecyclerView;
    private TextView priceTxt,AmountTv;
    private Button PlaceOrderBtn;
    List<CartModel> cartList;
    CartAdapter cartAdapter;
    DatabaseReference databaseReference,mRef,reference;
    RelativeLayout noItemLayout;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    int totalPrice;
    String VendorID,UID;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cart.
     */
    // TODO: Rename and change types and number of parameters
    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
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
        View v= inflater.inflate(R.layout.fragment_cart, container, false);
        PlaceOrderBtn=v.findViewById(R.id.payBtn);
        priceTxt=v.findViewById(R.id.TotalPriceTxt);
        AmountTv=v.findViewById(R.id.amountTV);
        cartRecyclerView=v.findViewById(R.id.cartRecyclerView);
        cartList=new ArrayList<>();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        cartRecyclerView.setHasFixedSize(true);
        noItemLayout=v.findViewById(R.id.EmptyCartLayout);
        mAuth=FirebaseAuth.getInstance();
        UID=mAuth.getUid();
        getVendorId();
        databaseReference= FirebaseDatabase.getInstance().getReference("Cart");
        mRef=FirebaseDatabase.getInstance().getReference("Order");
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getCartItems();
        PlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartList.size()<0){
                    Toast.makeText(getActivity(),"0 items can not be added to the cart",Toast.LENGTH_SHORT).show();
                }else{
                    if(VendorID!=null) {
                        progressDialog.show();
                        DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy @HH:mm", Locale.US);
                        String date=dateFormat.format(new Date());
                        String OrderId = "BD0" + System.currentTimeMillis();
                        String TimeStamp=String.valueOf(System.currentTimeMillis());
                        HashMap<String, String> map = new HashMap<>();
                        map.put("OrderId", OrderId);
                        map.put("OrderStatus", "In Progress");
                        map.put("OrderBy", UID);
                        map.put("OrderTo", VendorID);
                        map.put("Address","");
                        map.put("Cost",priceTxt.getText().toString());
                        map.put("OrderTime",date);
                        mRef.child(VendorID).child(TimeStamp).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    for (int n = 0; n < cartList.size(); n++) {
                                        String ProductId=cartList.get(n).getProductID();
                                        map.put("ProductId", ProductId);
                                        map.put("ProductName", cartList.get(n).getName());
                                        map.put("ProductQuantity", cartList.get(n).getProductQuantity());
                                        map.put("ProductPrice", cartList.get(n).getProductPrice());
                                        map.put("TotalPrice", cartList.get(n).getTotalPrice());
                                        mRef.child(VendorID).child(TimeStamp).child("Items").child(ProductId).setValue(map);
                                    }
                                    Log.d(TAG, "onComplete: Place Order");
                                    Intent intent =new Intent(getActivity(),Order.class);
                                    intent.putExtra("TimeStamp",TimeStamp);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getActivity(),"Vendor Id is null",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return v;
    }

    private void getCartItems() {
        cartList.clear();
        databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    CartModel curCart=dataSnapshot.getValue(CartModel.class);
                    cartList.add(curCart);
                }
                for(int n=0;n<cartList.size();n++){
                    totalPrice+=Integer.parseInt(cartList.get(n).getTotalPrice());
                }
                cartAdapter=new CartAdapter(getActivity(),cartList);
                cartRecyclerView.setAdapter(cartAdapter);
                if(cartList.size()==0){
                    PlaceOrderBtn.setVisibility(View.GONE);
                    priceTxt.setVisibility(View.GONE);
                    AmountTv.setVisibility(View.GONE);
                    noItemLayout.setVisibility(View.VISIBLE);
                }else{
                    PlaceOrderBtn.setVisibility(View.VISIBLE);
                    priceTxt.setVisibility(View.VISIBLE);
                    AmountTv.setVisibility(View.VISIBLE);
                    priceTxt.setText(String.valueOf(totalPrice));
                    noItemLayout.setVisibility(View.GONE);
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getVendorId() {
        reference= FirebaseDatabase.getInstance().getReference("Customers");
        String UserID=FirebaseAuth.getInstance().getUid();
        if(UserID!=null) {
            reference.child(UserID).addValueEventListener(new ValueEventListener() {
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