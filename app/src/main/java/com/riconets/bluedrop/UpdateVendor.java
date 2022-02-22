package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateVendor extends AppCompatActivity {
    private ImageView backBtn;
    private CircleImageView logout;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendor);
        logout=findViewById(R.id.logoutBtn);
        mAuth=FirebaseAuth.getInstance();
//        backBtn.setOnClickListener(view -> {
//
//        });
        logout.setOnClickListener(view -> {
            AlertDialog.Builder logoutAlertDialog=new AlertDialog.Builder(UpdateVendor.this);
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

    }
}