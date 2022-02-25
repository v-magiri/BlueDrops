package com.riconets.bluedrop;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView productRecyclerView;
    List<Product> products;
    SearchView searchView;
    String WaterPackages[]={"Bottled Water","Accessories","Refill"};
    public ProductAdapter productAdapter;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    ImageView backBtn;
    String ProductType,VendorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        productRecyclerView=findViewById(R.id.productList);
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Products");
        backBtn=findViewById(R.id.productBackBtn);
        backBtn.setOnClickListener(v -> finish());
        products=new ArrayList<>();
        VendorID=getIntent().getStringExtra("VendorID");
        ProductType=getIntent().getStringExtra("ProductType");
        searchView=findViewById(R.id.productSearchView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(ViewProducts.this,2,GridLayoutManager.VERTICAL,false);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setHasFixedSize(true);
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
    }

    private void getProducts() {
        databaseReference.child(ProductType).child(VendorID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product productModel = dataSnapshot.getValue(Product.class);
                    products.add(productModel);
                }
                productAdapter = new ProductAdapter(ViewProducts.this, products);
                productRecyclerView.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}