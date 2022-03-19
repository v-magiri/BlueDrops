package com.riconets.bluedrop;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class NotifyVendor extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView datePick, Quantity,QuantityTxt;
    String[] WaterPackages={"500ml","1L","5L","10L","20L","25L","30L","Over 30L"};
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String contentType = "application/json";
    private String serverKey;
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
        serverKey="key"+getString(R.string.FCM_KEY);
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
                String Quantity=QuantityTxt.getText().toString();
                if(supplyDate!=null&& Quantity!=null&& amountRemaining!=null&& Amount!=null){
                    String Message=notifyEdiTxt.getText().toString();
                    notifyVendor(supplyDate,Quantity,amountRemaining,Amount,Message);
                }
            }
        });
    }

    private void notifyVendor(String supplyDate, String quantity,
                              String amountRemaining, String amount, String message) {
        String UID=FirebaseAuth.getInstance().getUid();
        if(UID!=null&&vendorID!=null){
            HashMap<String,String> map=new HashMap<>();
            map.put("UserID",UID);
            map.put("SupplyDate",supplyDate);
            map.put("Quantity",quantity);
            map.put("RemainingAmount",amountRemaining);
            map.put("Amount",amount);
            map.put("Message",message);
            FirebaseDatabase.getInstance().getReference("Customer   Requests")
                    .child(vendorID).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    BuildNotification();
                   finish();
                }
            });
        }
    }

    private void BuildNotification() {
        if(vendorUserName!=null&& customerUserName!=null){
            String Topic="/topics/"+customerUserName;
            String Notification_Title="New Notification From"+customerUserName;
            String Notification_Message=notifyEdiTxt.getText().toString();
            JSONObject notification = new JSONObject();
            JSONObject notificationBody = new JSONObject();
            try {
                notificationBody.put("title", Notification_Title);
                notificationBody.put("message", Notification_Message);

                notification.put("to", Topic);
                notification.put("data", notificationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
            sendNotification(notification);
        }
        else{
            Log.d(TAG, "BuildNotification: NUll Values");
        }
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i(TAG, "onResponse: " + response.toString());
                    Log.e(TAG, "onResponse: Hurray Working");
                },
                error -> {
                    Toast.makeText(NotifyVendor.this, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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