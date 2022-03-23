package com.riconets.bluedrop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.Adapters.ProductAdapter;
import com.riconets.bluedrop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewProducts extends AppCompatActivity {
    private static final String TAG = "Database Error";
    private RecyclerView productRecyclerView;
    List<Product> products;
    SearchView searchView;
    String WaterPackages[]={"Bottled Water","Accessories","Refill"};
    public ProductAdapter productAdapter;
    DatabaseReference databaseReference,mRef;
    FirebaseAuth mAuth;
    private Button backHomeBtn;
    FrameLayout cartFrameLayout;
    private RelativeLayout emptyProductLayout;
    ImageView backBtn,cartImg;
    BadgeDrawable badge;
    String ProductType,VendorID;

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        ProductType=getIntent().getStringExtra("ProductType");
        VendorID=getIntent().getStringExtra("VendorID");
        productRecyclerView=findViewById(R.id.productList);
        mAuth=FirebaseAuth.getInstance();
        String UserID=mAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Products");
        mRef= FirebaseDatabase.getInstance().getReference("Cart").child(UserID);
        backBtn=findViewById(R.id.productBackBtn);
        backBtn.setOnClickListener(v -> finish());
        cartFrameLayout=findViewById(R.id.cart_FrameLayout);
        products=new ArrayList<>();
        backHomeBtn=findViewById(R.id.backHomeBtn);
        cartImg=findViewById(R.id.cartIcon);
        backHomeBtn.setOnClickListener(v -> finish());
        searchView=findViewById(R.id.productSearchView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(ViewProducts.this,2,GridLayoutManager.VERTICAL,false);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setHasFixedSize(true);
        emptyProductLayout=findViewById(R.id.EmptyProductsLayout);
        getProducts();
        searchView.setQueryHint("Search an Item");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
        //setting the badge for the cart icon
        badge=BadgeDrawable.create(this);
        cartFrameLayout.setForeground(badge);
        cartFrameLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {


            //either of the following two lines of code  work
            //badgeDrawable.updateBadgeCoordinates(imageView, frameLayout);
            BadgeUtils.attachBadgeDrawable(badge, cartImg, cartFrameLayout);
        });
        getCartItems();
    }

    private void getCartItems() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cartItemNum= (int) snapshot.getChildrenCount();
                if(cartItemNum>0){
                    badge.setVisible(true);
                    badge.setNumber(cartItemNum);
                    badge.setBackgroundColor(getResources().getColor(R.color.light_blue));
                    badge.setBadgeTextColor(getResources().getColor(R.color.white));
                }else{
                    Log.d(TAG, "onDataChange: Cart item: "+cartItemNum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error.getMessage());
            }
        });
    }

    private void getProducts() {
        products.clear();
        databaseReference.child(ProductType).child(VendorID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product productModel = dataSnapshot.getValue(Product.class);
                    products.add(productModel);
                }
                if(products.size()>0){
                    emptyProductLayout.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    productRecyclerView.setVisibility(View.VISIBLE);
                    productAdapter = new ProductAdapter(ViewProducts.this, products);
                    productRecyclerView.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                }else{
                    emptyProductLayout.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.INVISIBLE);
                    productRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error.getMessage());
            }
        });
    }
}