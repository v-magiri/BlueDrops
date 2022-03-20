package com.riconets.bluedrop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.Adapters.OrderAdapter;
import com.riconets.bluedrop.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity {
    String VendorId;
    String OrderTimeStamp;
    DatabaseReference mRef, reference;
    FirebaseAuth mAuth;
    String UID, VendorID;
    List<OrderModel> orderModelList;
    OrderAdapter orderAdapter;
    RecyclerView orderRecyclerView;
    private ImageView backBtn,logoutBtn;
    private RelativeLayout EmptyLayout;
    private Button OrderNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
        logoutBtn=findViewById(R.id.logoutBtn);
        logoutBtn.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Order");
        OrderNowBtn=findViewById(R.id.orderNowBtn);
        EmptyLayout=findViewById(R.id.EmptyOrderLayout);
        UID = mAuth.getUid();
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(Order.this));
        orderRecyclerView.setHasFixedSize(true);
        orderModelList = new ArrayList<>();
        getOrders();
    }

    private void getOrders() {
        reference = FirebaseDatabase.getInstance().getReference("Customers");
        if (UID != null) {
            reference.child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    VendorID = snapshot.child("vendorID").getValue().toString();
                    orderAdapter = new OrderAdapter(Order.this, orderModelList, VendorID);
                    mRef.child(VendorID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            orderModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);
                                orderModelList.add(orderModel);
                                orderRecyclerView.setAdapter(orderAdapter);
                                orderAdapter.notifyDataSetChanged();
                            }
                            if(orderModelList.size()>0){
                                EmptyLayout.setVisibility(View.GONE);
                                orderRecyclerView.setVisibility(View.VISIBLE);
                            }else{
                                EmptyLayout.setVisibility(View.VISIBLE);
                                orderRecyclerView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}