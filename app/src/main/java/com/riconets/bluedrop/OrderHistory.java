package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.model.OrderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistory extends AppCompatActivity {
    private static final String TAG = "Database Error";
    private ImageView logoutBtn,backBtn;
    private TextView OrderIdTxt,OrderDateTxt,OrderStatusTxt,DeliveryTxt;
    private TextView itemTotalTxt,deliveryTxt,totalTxt;
    private RecyclerView itemRecyclerView;
    DatabaseReference mRef;
    ItemsAdapter itemsAdapter;
    List<Map> itemList;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        String OrderId=getIntent().getStringExtra("OrderID");
        logoutBtn=findViewById(R.id.logoutBtn);
        logoutBtn.setVisibility(View.GONE);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v
                -> finish());
        itemRecyclerView=findViewById(R.id.orderItems);
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderIdTxt=findViewById(R.id.orderIDTxt);
        OrderDateTxt=findViewById(R.id.dateTxt);
        OrderStatusTxt=findViewById(R.id.orderStatusDetails);
        DeliveryTxt=findViewById(R.id.deliveryLocation);
        itemTotalTxt=findViewById(R.id.itemTotalPrice);
        deliveryTxt=findViewById(R.id.deliveryCharges);
        totalTxt=findViewById(R.id.paidAmountTxt);
        mRef= FirebaseDatabase.getInstance().getReference("Order");
        itemList=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        itemsAdapter=new ItemsAdapter(OrderHistory.this,itemList);
        getOrderDetails(OrderId);
    }

    private void getOrderDetails(String OrderID) {
        String UserId=mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Customers")
                .child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String vendorID=snapshot.child("vendorID").getValue().toString();
                mRef.child(vendorID).child(OrderID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemTotalTxt.setText("KSH: "+snapshot.child("orderCost").getValue().toString());
                        DeliveryTxt.setText(snapshot.child("shippingAddress").getValue().toString());
                        DeliveryTxt.setText(snapshot.child("shippingAddress").getValue().toString());
                        OrderStatusTxt.setText(snapshot.child("orderStatus").getValue().toString());
                        OrderIdTxt.setText(snapshot.child("orderId").getValue().toString());
                        OrderDateTxt.setText(snapshot.child("orderTime").getValue().toString());
//                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                        }
                        mRef.child(vendorID).child(OrderID).child("Items").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                itemList.clear();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    HashMap<String,String> map= (HashMap<String, String>) dataSnapshot.getValue();
                                    itemList.add(map);
                                    itemRecyclerView.setAdapter(itemsAdapter);
                                    itemsAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d(TAG, "onCancelled: "+error.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: "+error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error.getMessage());
            }
        });


    }
    public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
        Context context;
        List<Map> itemList;

        public ItemsAdapter(Context context, List<Map> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemsAdapter.MyViewHolder holder, int position) {
                HashMap<String,String> mapList= (HashMap<String, String>) itemList.get(position);
                holder.ProductNameTxt.setText(mapList.get("ProductName"));
                holder.productPriceTxt.setText("KSH: "+mapList.get("ProductPrice"));
                holder.productQuantityTxt.setText(mapList.get("ProductQuantity"));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView ProductNameTxt,productQuantityTxt,productPriceTxt;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ProductNameTxt=itemView.findViewById(R.id.productNameTxt);
                productPriceTxt=itemView.findViewById(R.id.productPriceTxt);
                productQuantityTxt=itemView.findViewById(R.id.productQuantityTxt);
            }
        }

    }
}