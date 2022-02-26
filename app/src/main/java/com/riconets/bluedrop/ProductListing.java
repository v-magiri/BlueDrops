package com.riconets.bluedrop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.riconets.bluedrop.model.CartModel;
import com.squareup.picasso.Picasso;

public class ProductListing extends AppCompatActivity {
    private Button cartBtn;
    private TextView priceTv,sizeTv,nameTv,descTv;
    ImageView productImage,backBtn;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        productImage=findViewById(R.id.product_image);
        cartBtn=findViewById(R.id.cartBtn);
        priceTv=findViewById(R.id.priceTxt);
        sizeTv=findViewById(R.id.sizeTxt);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
        nameTv=findViewById(R.id.ProductNameTxt);
        mAuth=FirebaseAuth.getInstance();
        String UID=mAuth.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Cart");
        descTv=findViewById(R.id.productDescTxt);
        Bundle productBundle=getIntent().getExtras();
        String ProductName=productBundle.getString("ProductName");
        String ProductSize=productBundle.getString("ProductSize");
        String ProductImageUri=productBundle.getString("ProductImage");
        String ProductId=productBundle.getString("ProductId");
        String ProductPrice=productBundle.getString("ProductPrice");

        //setting the views to the product Descriptions
        sizeTv.setText(ProductSize);
        nameTv.setText(ProductName);
        priceTv.setText(ProductPrice);
        descTv.setText(productBundle.getString("ProductDesc"));
        Picasso.get().load(ProductImageUri).into(productImage);
        //adding item to cart
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartID=String.valueOf(System.currentTimeMillis());
                CartModel cart=new CartModel(ProductName,ProductId,ProductPrice,"1",ProductImageUri,cartID);
                databaseReference.child(UID).child(cartID).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Item Added to Cart",Toast.LENGTH_SHORT).show();
                            cartBtn.setText("Continue Shopping");

                        }
                    }
                });

            }
        });

    }
}