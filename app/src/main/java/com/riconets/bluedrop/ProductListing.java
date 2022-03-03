package com.riconets.bluedrop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

public class ProductListing extends AppCompatActivity {
    private Button cartBtn;
    private TextView priceTv,sizeTv,nameTv,descTv,QuantityTxt;
    ImageView productImage,backBtn;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    ImageButton lessBtn,addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        productImage=findViewById(R.id.product_image);
        cartBtn=findViewById(R.id.cartBtn);
        priceTv=findViewById(R.id.priceTxt);
        sizeTv=findViewById(R.id.sizeTxt);
        backBtn=findViewById(R.id.backBtn);
        addBtn=findViewById(R.id.addBtn);
        lessBtn=findViewById(R.id.lessBtn);
        QuantityTxt=findViewById(R.id.quantity);
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
        lessBtn.setOnClickListener(view -> {
            int itemCount=Integer.parseInt(QuantityTxt.getText().toString());
            if(itemCount<=1){
                FancyToast.makeText(getApplicationContext(),"Quantity can not be less than 1",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
            }
            else{
                QuantityTxt.setText(String.valueOf(itemCount-1));
            }
        });
        addBtn.setOnClickListener(view -> {
            int quantity=Integer.parseInt(QuantityTxt.getText().toString());
            QuantityTxt.setText(String.valueOf(quantity+1));

        });
        //adding item to cart
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Quantity=QuantityTxt.getText().toString().trim();
                String TotalPrice=String.valueOf(Integer.parseInt(ProductPrice)*Integer.parseInt(Quantity));
                String cartID=String.valueOf(System.currentTimeMillis());
                CartModel cart=new CartModel(ProductName,ProductId,ProductPrice,Quantity,ProductImageUri,cartID,TotalPrice);
                databaseReference.child(UID).child(cartID).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Item Added to Cart",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });

    }
}