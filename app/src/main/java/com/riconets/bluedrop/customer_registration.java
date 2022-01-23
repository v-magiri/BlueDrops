    package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.riconets.bluedrop.model.Customer;
import com.shashank.sony.fancytoastlib.FancyToast;

    public class customer_registration extends AppCompatActivity {
        String[] location={"Nakuru","Nairobi","Mombasa","Kisumu"};
        String[] vendor={"Waba Water","Keringet Water","Dasani Water","AquaMist Water"};
        private EditText fNameEditTxt, lNameEditTxt, PhoneEditTxt, RepeatPassEditTxt, PasswordTxt, emailEditTxt;
        public Button RegisterBtn;
        private ProgressDialog progressDialog;
        private FirebaseAuth firebaseAuth;
        private TextView signinTxt;
        private AutoCompleteTextView locationAutoComplete,vendorAutoComplete;
        ArrayAdapter<String> arrayAdapter;
        ArrayAdapter<String> vendorAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_customer_registration);

            //Binding the the various UI element to the java file
            locationAutoComplete=findViewById(R.id.locationAutoComplete);
            vendorAutoComplete=findViewById(R.id.vendorAutoComplete);
            fNameEditTxt = findViewById(R.id.fNameEditTxt);
            lNameEditTxt = findViewById(R.id.lNameEditText);
            PhoneEditTxt = findViewById(R.id.customer_PhoneNumber);
            PasswordTxt = findViewById(R.id.password);
            emailEditTxt = findViewById(R.id.customerEmailEditTxt);
            RepeatPassEditTxt = findViewById(R.id.repeat_Password);
            RegisterBtn = findViewById(R.id.registerBtn);
            signinTxt = findViewById(R.id.signin);
            progressDialog = new ProgressDialog(this);
            firebaseAuth = FirebaseAuth.getInstance();

            arrayAdapter=new ArrayAdapter<String>(customer_registration.this,R.layout.item,location);
            locationAutoComplete=findViewById(R.id.locationAutoComplete);

            locationAutoComplete.setAdapter(arrayAdapter);

            locationAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String location=parent.getItemAtPosition(position).toString();
                    FancyToast.makeText(getBaseContext(),location,2,FancyToast.SUCCESS,true).show();
                }
            });

            vendorAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,vendor);
            vendorAutoComplete=findViewById(R.id.vendorAutoComplete);
            vendorAutoComplete.setAdapter(vendorAdapter);
            vendorAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String vendor=parent.getItemAtPosition(position).toString();
                    FancyToast.makeText(getBaseContext(),vendor,2,FancyToast.SUCCESS,true).show();
                }
            });
            signinTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), customer_login.class));
                }
            });
            RegisterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();
                }
            });


        }

        private void register() {

            //validating the values from the user Input
            String fName = fNameEditTxt.getText().toString().trim();
            String lName = lNameEditTxt.getText().toString().trim();
            String email = emailEditTxt.getText().toString().trim();
            String Location=locationAutoComplete.getText().toString();
            String PhoneNumber = PhoneEditTxt.getText().toString().trim();
            String repeatPass = RepeatPassEditTxt.getText().toString().trim();
            String Password = PasswordTxt.getText().toString().trim();

            if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(PhoneNumber) || TextUtils.isEmpty(Location)
                    || TextUtils.isEmpty(repeatPass) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(email)) {
                FancyToast.makeText(getApplicationContext(), "Please Fill all the Fields", Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            } else if (Password.length() < 6) {
                FancyToast.makeText(getBaseContext(), "Password Should Be 6 characters or more", Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
            } else if (!Password.equals(repeatPass)) {
                RepeatPassEditTxt.setError("Password Do Not Match");
                RepeatPassEditTxt.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditTxt.setError("Invalid Email");
                emailEditTxt.requestFocus();
            } else {
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("Registering you");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                //register User
                firebaseAuth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String customer_id=firebaseAuth.getCurrentUser().getUid();
                            Customer customer=new Customer(fName,lName,email,Location,PhoneNumber);
                            if(customer_id != null){
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Customers");
                                databaseReference.child(customer_id).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();
                                            FancyToast.makeText(getApplicationContext(),"Account Created ",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                            startActivity(new Intent(getApplicationContext(),customer_login.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        String reason=e.getMessage();
                        FancyToast.makeText(getApplicationContext(),reason,FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

                    }
                });
            }
        }
    }