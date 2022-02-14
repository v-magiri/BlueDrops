package com.riconets.bluedrop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class Services extends AppCompatActivity {
    private ImageView backBtn;
    private CircleImageView logout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        logout=findViewById(R.id.logoutBtn);
        mAuth=FirebaseAuth.getInstance();
//        backBtn.setOnClickListener(view -> {
//
//        });
        logout.setOnClickListener(view -> {
            AlertDialog.Builder logoutAlertDialog=new AlertDialog.Builder(Services.this);
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
        });


        int intentFragment=getIntent().getExtras().getInt("Fragment_ID");
        switch (intentFragment){
            case 0:
                NotifyVendor notifyVendor =new NotifyVendor();
                FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frLayout,notifyVendor,"");
                fragmentTransaction.commit();
                break;
            case 1:
                Update_location update_location=new Update_location();
                FragmentTransaction fragmentTransaction1= getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.frLayout,update_location,"");
                fragmentTransaction1.commit();
                break;
            case 2:
                Change_Vendor change_vendor=new Change_Vendor();
                FragmentTransaction fragmentTransaction2= getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.frLayout,change_vendor,"");
                fragmentTransaction2.commit();
                break;
            case 3:
                Refill refill=new Refill();
                FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.frLayout,refill,"");
                fr.commit();
                break;
            case 4:
                Update_Profile update_profile=new Update_Profile();
                FragmentTransaction frTransUpdate= getSupportFragmentManager().beginTransaction();
                frTransUpdate.replace(R.id.frLayout,update_profile,"");
                frTransUpdate.commit();
                break;
        }
    }
}