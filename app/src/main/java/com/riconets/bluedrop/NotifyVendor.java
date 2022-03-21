package com.riconets.bluedrop;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NotifyVendor extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView datePick, Quantity,QuantityTxt;
    String[] WaterPackages={"500ml","1L","5L","10L","20L","25L","30L","Over 30L"};
    String[] remainingAmount={"Quarter Level","Half Level"};
    private ImageButton addBtn, lessBtn;
    final String TAG = "NOTIFICATION TAG";
    String UID,vendorUserName,vendorID,Amount,amountRemaining,customerUserName;
    LinearLayout linearLayout;
    private AutoCompleteTextView packageAutoComplete,amountRemainingAutoComplete;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> amountRemainingAdapter;
    private EditText notifyEdiTxt;
    Button notifyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_vendor);
        datePick=findViewById(R.id.date_pick);
        QuantityTxt=findViewById(R.id.quantityTxt);
        notifyEdiTxt=findViewById(R.id.notifyTxt);
        addBtn=findViewById(R.id.addBtn);
        notifyBtn=findViewById(R.id.notify);
        linearLayout=findViewById(R.id.Quantity);
        lessBtn=findViewById(R.id.lessBtn);
        Quantity=findViewById(R.id.quantity);
        UID= FirebaseAuth.getInstance().getUid();
        packageAutoComplete=findViewById(R.id.packageAutoComplete);
        amountRemainingAutoComplete=findViewById(R.id.remainingAutoComplete);
        datePick.setOnClickListener(view -> showDateDialog());
        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,WaterPackages);
        packageAutoComplete.setAdapter(arrayAdapter);
        amountRemainingAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,remainingAmount);
        //get My Vendor
        getVendor();
        amountRemainingAutoComplete.setAdapter(amountRemainingAdapter);
        amountRemainingAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                amountRemaining=parent.getItemAtPosition(position).toString();
            }
        });
        //redirect user to homepage
        packageAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            Amount=adapterView.getItemAtPosition(i).toString();
            if(Amount.equals("Over 30L")){
                Quantity.setVisibility(View.GONE);
                QuantityTxt.setVisibility(View.GONE);
                lessBtn.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);
            }else{
                Quantity.setVisibility(View.VISIBLE);
                QuantityTxt.setVisibility(View.VISIBLE);
                lessBtn.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.VISIBLE);
            }

        });
        lessBtn.setOnClickListener(view -> {
            int itemCount=Integer.parseInt(Quantity.getText().toString());
            if(itemCount<=1){
                FancyToast.makeText(getApplicationContext(),"Quantity can not be less than 1",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
            }
            else{
                Quantity.setText(String.valueOf(itemCount-1));
            }
        });
        addBtn.setOnClickListener(view -> {
            int quantity=Integer.parseInt(Quantity.getText().toString());
            Quantity.setText(String.valueOf(quantity+1));
        });
        FirebaseMessaging.getInstance().getToken().
                addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String token=task.getResult();
                        Log.d(TAG, "onComplete: "+token);
                        if(UID!=null) {
                            FirebaseDatabase.getInstance().getReference("Tokens").child(UID).child("Messaging_Token").setValue(token);
                        }else{
                            Log.e(TAG, "onCreate: User ID is null");
                        }
                    }
                });
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String supplyDate=datePick.getText().toString();
                String quantityAmount=Quantity.getText().toString();
                if(supplyDate!=null&& quantityAmount!=null&& amountRemaining!=null&& Amount!=null){
                    String Message=notifyEdiTxt.getText().toString();
                    notifyVendor(supplyDate,quantityAmount,amountRemaining,Amount,Message);
                }
            }
        });
    }

    private void notifyVendor(String supplyDate, String quantity,
                              String amountRemaining, String amount, String message) {
        String UID=FirebaseAuth.getInstance().getUid();
        String PushID="CR"+System.currentTimeMillis();
        Date CurrentDate=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(CurrentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String timeStamp=sdf.format(calendar.getTime());
        if(UID!=null&&vendorID!=null){
            HashMap<String,String> map=new HashMap<>();
            map.put("UserID",UID);
            map.put("SupplyDate",supplyDate);
            map.put("Quantity",quantity);
            map.put("RemainingAmount",amountRemaining);
            map.put("Amount",amount);
            map.put("Message",message);
            map.put("TimeStamp",timeStamp);
            map.put("RequestStatus","Pending");
            map.put("RequestID",PushID);
            FirebaseDatabase.getInstance().getReference("CustomerRequests")
                    .child(vendorID).child(PushID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   finish();
                }
            });
        }
    }



    private void getVendor() {
        String UID;
        UID=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference("Customers").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vendorID=snapshot.child("vendorID").getValue().toString();
                customerUserName=snapshot.child("userName").getValue().toString();
                FirebaseDatabase.getInstance().getReference("Vendors")
                        .child(vendorID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        vendorUserName=snapshot.child("userName").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error.getMessage());
            }
        });
    }



    private void showDateDialog() {
        DatePickerDialog datePickerDialog= new DatePickerDialog(
                NotifyVendor.this, (DatePickerDialog.OnDateSetListener) NotifyVendor.this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year);
        stringBuilder.append("/");
        stringBuilder.append(month+1);
        stringBuilder.append("/");
        stringBuilder.append(dayOfMonth);
        String Date=stringBuilder.toString();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dd");
        //get Current Date
        Date CurrentDate=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(CurrentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar pickerCalendarDate=Calendar.getInstance();
        try {
            Date pickedDate=formatter.parse(Date);
            pickerCalendarDate.setTime(pickedDate);
            int result=calendar.compareTo(pickerCalendarDate);
            if(result<=0){
                datePick.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                datePick.setText(Date);
            }
            else{
                FancyToast.makeText(getApplicationContext(),"Choose an Appropriate Date",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
    }