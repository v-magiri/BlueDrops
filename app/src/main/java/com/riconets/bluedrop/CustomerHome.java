package com.riconets.bluedrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class CustomerHome extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageView logoutBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.Theme_BlueDrop);
        setContentView(R.layout.activity_customer_home);
        mAuth=FirebaseAuth.getInstance();
        logoutBtn=findViewById(R.id.logoutBtn);
        bottomNavigationView=findViewById(R.id.bottom_navbar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);
        Home home =new Home();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,home,"");
        fragmentTransaction.commit();

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
                     Cart cart =new Cart();
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