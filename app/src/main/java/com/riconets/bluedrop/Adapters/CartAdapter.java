package com.riconets.bluedrop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riconets.bluedrop.R;
import com.riconets.bluedrop.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView priceTv,nameTv,quantityTxt,totalPriceTxt;
        ImageButton addBtn,lessBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.cartProductImage);
            priceTv=itemView.findViewById(R.id.priceTV);
            nameTv=itemView.findViewById(R.id.ProductNameTxt);
            addBtn=itemView.findViewById(R.id.addBtn);
            lessBtn=itemView.findViewById(R.id.lessBtn);
            quantityTxt=itemView.findViewById(R.id.cartQuantity);
            totalPriceTxt=itemView.findViewById(R.id.totalPriceTV);


        }
    }

}
