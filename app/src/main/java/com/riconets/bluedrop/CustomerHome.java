package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerHome extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BlueDrop);
        setContentView(R.layout.activity_customer_home);
        bottomNavigationView=findViewById(R.id.bottom_navbar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);
        Home home =new Home();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,home,"");
        fragmentTransaction.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                }
            };
}