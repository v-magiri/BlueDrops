package com.riconets.bluedrop.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.riconets.bluedrop.R;
import com.riconets.bluedrop.model.CartModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<CartModel> cartModelList;
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference("Cart");
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

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteAlertDialog=new AlertDialog.Builder(v.getContext());
                deleteAlertDialog.setTitle("Confirm Delete");
                deleteAlertDialog.setIcon(R.drawable.delete);
                deleteAlertDialog.setMessage("Are you sure you want to delete Item");
                deleteAlertDialog.setCancelable(false);
                deleteAlertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        mRef.child(UID).child(itemID).removeValue();
                        cartModelList.remove(position);
                        notifyDataSetChanged();
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
