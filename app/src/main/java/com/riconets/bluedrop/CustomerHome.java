package com.riconets.bluedrop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.model.CartModel;


public class CustomerHome extends AppCompatActivity {
    private static final String TAG = "CartItemsHome";
    private BottomNavigationView bottomNavigationView;
    private ImageView logoutBtn;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    BadgeDrawable badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        String VendorId=getIntent().getStringExtra("VendorId");
        mAuth=FirebaseAuth.getInstance();
        String UserID=mAuth.getCurrentUser().getUid();
        logoutBtn=findViewById(R.id.logoutBtn);
        bottomNavigationView=findViewById(R.id.bottom_navbar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        mRef= FirebaseDatabase.getInstance().getReference("Cart").child(UserID);
        badge=bottomNavigationView.getOrCreateBadge(R.id.cart);
        getCartItems();
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);
        Home home =new Home();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,home,"");
        fragmentTransaction.commit();

        int intentFragment=getIntent().getIntExtra("Fragment_ID",0);
        switch (intentFragment){
            case 1:
                BlueDropsCart cart=new BlueDropsCart();
                FragmentTransaction fragmentTransaction1= getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.content,cart,"");
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;
        }

        //implementation of the logout Functionality
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder logoutAlertDialog=new AlertDialog.Builder(CustomerHome.this);
                    logoutAlertDialog.setTitle("Confirm Logout");
                    logoutAlertDialog.setIcon(R.drawable.icon_bluedrops);
                    logoutAlertDialog.setMessage("Are you sure you want to logout");
                    logoutAlertDialog.setCancelable(false);
                    logoutAlertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(),customer_login.class));
                    });
                    logoutAlertDialog.setNegativeButton("No", (dialog, which) ->
                            dialog.cancel());
                    AlertDialog alertDialog = logoutAlertDialog.create();
                    alertDialog.show();

                }
        });
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

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){

        }else{
            startActivity(new Intent(getApplicationContext(),customer_login.class));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener=
            item -> {
             switch (item.getItemId()){
                 case R.id.home:
                     Home home =new Home();
                     FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                     fragmentTransaction.replace(R.id.content,home,"");
                     fragmentTransaction.commit();
                     return true;
                 case R.id.cart:
                     BlueDropsCart cart =new BlueDropsCart();
                     FragmentTransaction fragmentTransaction1= getSupportFragmentManager().beginTransaction();
                     fragmentTransaction1.replace(R.id.content,cart,"");
                     fragmentTransaction1.commit();
                     return true;
                 case R.id.account:
                     Account account =new Account();
                     FragmentTransaction fragmentTransaction2= getSupportFragmentManager().beginTransaction();
                     fragmentTransaction2.replace(R.id.content,account,"");
                     fragmentTransaction2.commit();
                     return true;
             }
             return false;
            };
}