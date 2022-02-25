package com.riconets.bluedrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductListing extends AppCompatActivity {
    private Button cartBtn;
    private TextView priceTv,sizeTv,nameTv,descTv;
    ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        productImage=findViewById(R.id.product_image);
        cartBtn=findViewById(R.id.cartBtn);
        priceTv=findViewById(R.id.priceTxt);
        sizeTv=findViewById(R.id.sizeTxt);
        nameTv=findViewById(R.id.ProductNameTxt);
        descTv=findViewById(R.id.productDescTxt);
        Bundle productBundle=getIntent().getExtras();
        sizeTv.setText(productBundle.getString("ProductSize"));
        String ProductImageUri=productBundle.getString("ProductImage");
        nameTv.setText(productBundle.getString("ProductName"));
        descTv.setText(productBundle.getString("ProductDesc"));
        Picasso.get().load(ProductImageUri).into(productImage);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CustomerHome.class);
                intent.putExtra("Fragment_ID",1);
                intent.putExtras(productBundle);
                startActivity(intent);
            }
        });

    }
}