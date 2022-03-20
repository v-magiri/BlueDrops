package com.riconets.bluedrop.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.OrderHistory;
import com.riconets.bluedrop.R;
import com.riconets.bluedrop.model.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context context;
    List<OrderModel> orderList;
    DatabaseReference databaseReference;
    String  VendorID;
    ProgressDialog progressDialog;
    public OrderAdapter(Context context, List<OrderModel> orderList,String vendorID) {
        this.context = context;
        this.orderList = orderList;
        this.VendorID=vendorID;
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Canceling Order");
        progressDialog.setIndeterminate(true);
        databaseReference= FirebaseDatabase.getInstance().getReference("Order").child(VendorID);
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderModel order=orderList.get(position);
        holder.OrderStatus.setText(order.getOrderStatus());
        holder.OrderDateTxt.setText(order.getOrderTime());
        holder.OrderIDTxt.setText(order.getOrderId());
        String OrderID=order.getOrderId();
        databaseReference.child(OrderID).child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                int totalPrice=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        totalPrice=Integer.parseInt(dataSnapshot.child("ProductPrice").getValue().toString());
                        count++;
                }
                holder.PriceTxt.setText("Kshs: "+String.valueOf(totalPrice));
                holder.itemsNumTxt.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder cancelOrder=new AlertDialog.Builder(v.getContext());
                cancelOrder.setTitle("Are you sure you want to cancel Order");
                cancelOrder.setIcon(R.drawable.ic_cancel_button);
                cancelOrder.setMessage("Canceling of the order can not be undone");
                cancelOrder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: canceled request");
                        dialog.cancel();
                    }
                });
                cancelOrder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child(OrderID).getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                           Toast toast=Toast.makeText(v.getContext(), "SuccessFully Delated Item",Toast.LENGTH_SHORT);
                                           toast.setGravity(Gravity.CENTER,0,0);
                                           toast.show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.d(TAG, "onClick: Cancel Request Called");
                    }
                });
                AlertDialog alertDialog = cancelOrder.create();
                alertDialog.show();
            }
        });
        holder.ViewOrderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), OrderHistory.class);
                intent.putExtra("OrderID",order.getOrderId());
                v.getContext().startActivity(intent);
//                Toast toast=Toast.makeText(v.getContext(),"To Implement View Order",Toast.LENGTH_SHORT);
//                View view=toast.getView();
//                view.getBackground().setColorFilter(Color.parseColor("#949494"), PorterDuff.Mode.SRC_IN);
//                TextView text=view.findViewById(android.R.id.message);
//                text.setTextColor(Color.parseColor("#FFFFFF"));
//                text.setTextSize(16);
//                toast.setGravity(Gravity.CENTER,0,0);
//                toast.show();
            }
        });
    }

    @Override
    public int getItemCount() {
         return orderList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView OrderIDTxt,OrderDateTxt,OrderStatus,itemsNumTxt,PriceTxt,ViewOrderTxt;
        private ImageView cancelBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderIDTxt=itemView.findViewById(R.id.orderID);
            OrderDateTxt=itemView.findViewById(R.id.DateTxt);
            OrderStatus=itemView.findViewById(R.id.orderStatus);
            cancelBtn=itemView.findViewById(R.id.cancelBtn);
            itemsNumTxt=itemView.findViewById(R.id.itemsNum);
            PriceTxt=itemView.findViewById(R.id.totalPrice);
            ViewOrderTxt=itemView.findViewById(R.id.viewOrder);
        }
    }

}
