package com.riconets.bluedrop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riconets.bluedrop.ProductListing;
import com.riconets.bluedrop.R;
import com.riconets.bluedrop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<Product> productList;
    List<Product> filteredProduct;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        filteredProduct=new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        Product product=productList.get(position);
        holder.productNameTxt.setText(product.getProductName());
        holder.productPriceTxt.setText("KSH "+product.getProductPrice());
        holder.productSizeTxt.setText(product.getProductSize());
        Picasso.get().load(product.getProductImageUri()).into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ProductListing.class);
                Bundle productBundle=new Bundle();
                productBundle.putString("ProductName",product.getProductName());
                productBundle.putString("ProductPrice",product.getProductPrice());
                productBundle.putString("ProductSize",product.getProductSize());
                productBundle.putString("ProductImage",product.getProductImageUri());
                productBundle.putString("ProductId",product.getProductId());
                productBundle.putString("ProductDesc",product.getProductDesc());
                intent.putExtras(productBundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }
    private Filter productFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> productFilter=new ArrayList<>();
            if(constraint.toString().isEmpty()){
                productFilter.addAll(filteredProduct);
            }
            else{
                String searchText=constraint.toString().toLowerCase().trim();
                for(Product productObj:filteredProduct){
                    if(productObj.getProductName().toLowerCase().contains(searchText)){
                        productFilter.add(productObj);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=productFilter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List<Product>)results.values);
            notifyDataSetChanged();
        }
    };

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productNameTxt,productPriceTxt,productSizeTxt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.productImage);
            productNameTxt=itemView.findViewById(R.id.productName);
            productPriceTxt=itemView.findViewById(R.id.productPrice);
            productSizeTxt=itemView.findViewById(R.id.productSize);
        }
    }
}
