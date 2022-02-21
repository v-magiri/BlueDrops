    package com.riconets.bluedrop;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Patterns;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.riconets.bluedrop.model.Customer;
    import com.shashank.sony.fancytoastlib.FancyToast;

    public class customer_registration extends AppCompatActivity {
        String[] location={"Nakuru","Nairobi","Mombasa","Kisumu"};
        int resultStatus;
        String userName;
        String[] vendor={"Waba Water","Keringet Water","Dasani Water","AquaMist Water"};
        private EditText NameEditTxt, userNameEditTxt, PhoneEditTxt, RepeatPassEditTxt, PasswordTxt, emailEditTxt;
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
            NameEditTxt = findViewById(R.id.fNameEditTxt);
            userNameEditTxt = findViewById(R.id.lNameEditText);
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

            locationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
                String location=parent.getItemAtPosition(position).toString();
                FancyToast.makeText(getBaseContext(),location,2,FancyToast.SUCCESS,true).show();
            });

            vendorAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,vendor);
            vendorAutoComplete=findViewById(R.id.vendorAutoComplete);
            vendorAutoComplete.setAdapter(vendorAdapter);
            vendorAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
                String vendor=parent.getItemAtPosition(position).toString();
                FancyToast.makeText(getBaseContext(),vendor,2,FancyToast.SUCCESS,true).show();
            });
            signinTxt.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), customer_login.class)));
            RegisterBtn.setOnClickListener(v -> register());


        }

        private void register() {

            //validating the values from the user Input
            String fName = NameEditTxt.getText().toString().trim();
            userName = userNameEditTxt.getText().toString().trim();
            String email = emailEditTxt.getText().toString().trim();
            String Location=locationAutoComplete.getText().toString();
            String PhoneNumber = PhoneEditTxt.getText().toString().trim();
            String repeatPass = RepeatPassEditTxt.getText().toString().trim();
            String Password = PasswordTxt.getText().toString().trim();
            String ProfilePic="";

            if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(PhoneNumber) || TextUtils.isEmpty(Location)
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
                if(checkUserNameExists()==1){
                    userNameEditTxt.setError("userName Exists");
                    userNameEditTxt.requestFocus();
                    return;
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String customer_id=firebaseAuth.getUid();
                                Customer customer=new Customer(fName,userName,email,Location,PhoneNumber,ProfilePic);
                                if(customer_id != null){
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Customers");
                                    databaseReference.child(customer_id).setValue(customer).addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("Roles");
                                            mRef.child(customer_id).child("UserType").setValue("Customer");
                                            progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(),CustomerHome.class));
                                            finish();
                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        String reason=e.getMessage();
                        FancyToast.makeText(getApplicationContext(),reason,FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

                    });
                }
                }

                }

        private int checkUserNameExists() {
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("Customers");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(userName)){
                        resultStatus=1;
                    }
                    else{
                        resultStatus=0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return resultStatus;
        }
    }