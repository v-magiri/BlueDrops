package com.riconets.bluedrop.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.riconets.bluedrop.R;
import com.riconets.bluedrop.model.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context context;
    List<OrderModel> orderList;
    DatabaseReference databaseReference;
    String  VendorID;
    public OrderAdapter(Context context, List<OrderModel> orderList,String vendorID) {
        this.context = context;
        this.orderList = orderList;
        this.VendorID=vendorID;
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
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder cancelOrder=new AlertDialog.Builder(v.getContext());
                cancelOrder.setTitle("Are you sure you want to cancel Order");
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
                        databaseReference.child(OrderID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

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
    }

    @Override
    public int getItemCount() {
         return orderList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView OrderIDTxt,OrderDateTxt,OrderStatus;
        private ImageView cancelBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderIDTxt=itemView.findViewById(R.id.orderID);
            OrderDateTxt=itemView.findViewById(R.id.DateTxt);
            OrderStatus=itemView.findViewById(R.id.orderStatus);
            cancelBtn=itemView.findViewById(R.id.cancelBtn);
        }
    }

}
