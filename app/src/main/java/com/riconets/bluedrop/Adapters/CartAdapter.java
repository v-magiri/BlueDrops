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
        holder.totalPriceTxt.setText("KSH: "+cartModel.getProductPrice());
        String ProductImage=cartModel.getProductImageUri();
        Picasso.get().load(ProductImage).into(holder.productImage);
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Quantity=Integer.parseInt(holder.quantityTxt.getText().toString());
                int Price=Integer.parseInt(holder.priceTv.getText().toString().split("KSH: ")[1]);
                int TotalPrice=Integer.parseInt(holder.totalPriceTxt.getText().toString().split("KSH: ")[1]);
                holder.quantityTxt.setText(String.valueOf(Quantity+1));
                holder.totalPriceTxt.setText("KSH: "+String.valueOf(TotalPrice+Price));
            }
        });
        holder.lessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Quantity=Integer.parseInt(holder.quantityTxt.getText().toString());
                int Price=Integer.parseInt(holder.priceTv.getText().toString().split("KSH: ")[1]);
                int TotalPrice=Integer.parseInt(holder.totalPriceTxt.getText().toString().split("KSH: ")[1]);
                if(Quantity==1){
                    Toast.makeText(context.getApplicationContext(),"Quantity Can not be Less than 1",Toast.LENGTH_SHORT).show();
                }else {
                    holder.quantityTxt.setText(String.valueOf(Quantity-1));
                    holder.totalPriceTxt.setText("KSH: "+String.valueOf(TotalPrice-Price));
                }
            }
        });
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
        ImageButton addBtn,lessBtn;
        ImageView deleteBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.cartProductImage);
            priceTv=itemView.findViewById(R.id.priceTV);
            nameTv=itemView.findViewById(R.id.ProductNameTxt);
            addBtn=itemView.findViewById(R.id.addBtn);
            lessBtn=itemView.findViewById(R.id.lessBtn);
            quantityTxt=itemView.findViewById(R.id.cartQuantity);
            totalPriceTxt=itemView.findViewById(R.id.totalPriceTV);
            deleteBtn=itemView.findViewById(R.id.deleteItem);


        }
    }

}
