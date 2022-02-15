package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Services extends AppCompatActivity {
    private ImageView backBtn;
    private CircleImageView logout;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

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
                fragmentTransaction1.replace(R.id.frLayout,update_location,"").addToBackStack(null);
                fragmentTransaction1.commit();
                break;
            case 2:
                Change_Vendor change_vendor=new Change_Vendor();
                FragmentTransaction fragmentTransaction2= getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.frLayout,change_vendor,"").addToBackStack(null);
                fragmentTransaction2.commit();
                break;
            case 3:
                Refill refill=new Refill();
                FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.frLayout,refill,"").addToBackStack(null);
                fr.commit();
                break;
            case 4:
                Update_Profile update_profile=new Update_Profile();
                String CurrentUserID=mAuth.getCurrentUser().getUid();
                databaseReference= FirebaseDatabase.getInstance().getReference("Customers");
                databaseReference.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String FirstName=snapshot.child("firstName").getValue().toString();
                        String LastName=snapshot.child("lastName").getValue().toString();
                        String PhoneNumber=snapshot.child("phoneNumber").getValue().toString();
                        String ProfileUri=snapshot.child("profilePic").getValue().toString();
                        if(!TextUtils.isEmpty(FirstName) || !TextUtils.isEmpty(LastName)||
                                !TextUtils.isEmpty(PhoneNumber) || !TextUtils.isEmpty(ProfileUri) ) {
                            Bundle updateProfileBundle =new Bundle();
                            updateProfileBundle.putString("FirstName",FirstName);
                            updateProfileBundle.putString("LastName",LastName);
                            updateProfileBundle.putString("PhoneNumber",PhoneNumber);
                            updateProfileBundle.putString("ProfileUri",ProfileUri);
                            update_profile.setArguments(updateProfileBundle);
                            FragmentTransaction frTransUpdate= getSupportFragmentManager().beginTransaction();
                            frTransUpdate.replace(R.id.frLayout,update_profile,"").addToBackStack(null);
                            frTransUpdate.commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
        }
    }
}