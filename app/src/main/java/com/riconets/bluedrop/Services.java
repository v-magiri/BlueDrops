package com.riconets.bluedrop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Switch;

public class Services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        int intentFragment=getIntent().getExtras().getInt("Fragment_ID");
        switch (intentFragment){
            case 0:
                NotifyVendor notifyVendor =new NotifyVendor();
                FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frLayout,notifyVendor,"");
                fragmentTransaction.commit();
        }
    }
}