package com.riconets.bluedrop.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.Cart;
import com.riconets.bluedrop.R;
import com.riconets.bluedrop.model.CartModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private static final String TAG = "Database Error";
    Context context;
    List<CartModel> cartModelList;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    TextView PriceTxt;

    public CartAdapter(Context context, List<CartModel> cartModelList,TextView priceTxt) {
        this.context = context;
        this.cartModelList = cartModelList;
        this.PriceTxt=priceTxt;
        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference("Cart");
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Removing Item from Cart");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartModel cartModel=cartModelList.get(position);
        String UID=mAuth.getUid();
        String itemID=cartModel.getCartID();
        holder.quantityTxt.setText(cartModel.getProductQuantity());
        holder.priceTv.setText("KSH: "+cartModel.getProductPrice());
        holder.nameTv.setText(cartModel.getName());
        holder.quantityTxt.setText(cartModel.getProductQuantity());
        holder.totalPriceTxt.setText("KSH: "+cartModel.getTotalPrice());
        String ProductImage=cartModel.getProductImageUri();
        Picasso.get().load(ProductImage).into(holder.productImage);
        int totalPrice=0;
        for(int n=0;n<cartModelList.size();n++){
            totalPrice+=Integer.parseInt(cartModelList.get(n).getTotalPrice());
            PriceTxt.setText(String.valueOf(totalPrice));
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteAlertDialog=new AlertDialog.Builder(v.getContext());
                deleteAlertDialog.setTitle("Confirm Delete");
                deleteAlertDialog.setIcon(R.drawable.delete);
                deleteAlertDialog.setMessage("Are you sure you want to delete Item");
                deleteAlertDialog.setCancelable(false);
                deleteAlertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        progressDialog.show();
                        int ItemPrice=Integer.parseInt(cartModelList.get(position).getTotalPrice());
                        int CurrentPrice=Integer.parseInt(PriceTxt.getText().toString());
                        PriceTxt.setText(String.valueOf(CurrentPrice-ItemPrice));
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mRef.child(UID).child(itemID).getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "onCancelled: "+error.getMessage());
                        }
                    });
                });
                deleteAlertDialog.setNegativeButton("No", (dialog, which) ->
                        dialog.cancel());
                AlertDialog alertDialog = deleteAlertDialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView priceTv,nameTv,quantityTxt,totalPriceTxt;
        ImageView deleteBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.cartProductImage);
            priceTv=itemView.findViewById(R.id.priceTV);
            nameTv=itemView.findViewById(R.id.ProductNameTxt);
            quantityTxt=itemView.findViewById(R.id.cartQuantity);
            totalPriceTxt=itemView.findViewById(R.id.totalPriceTV);
            deleteBtn=itemView.findViewById(R.id.deleteItem);
        }
    }

}
