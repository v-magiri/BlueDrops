package com.riconets.bluedrop;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
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
    String[] remainingAmount={"Quarter Level","Half Level"};
    private ImageButton addBtn, lessBtn;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String contentType = "application/json";
    private String serverKey;
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    String UID;
    LinearLayout linearLayout;
    private AutoCompleteTextView packageAutoComplete,amountRemainingAutoComplete;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> amountRemaining;
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
        amountRemaining=new ArrayAdapter<String>(getApplicationContext(),R.layout.item,remainingAmount);
        amountRemainingAutoComplete.setAdapter(amountRemaining);
        //redirect user to homepage
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyVendor();
            }
        });
        packageAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            String Amount=adapterView.getItemAtPosition(i).toString();
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
            if(itemCount==0){
                FancyToast.makeText(getApplicationContext(),"Quantity can not be less than 0",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
            }
            else{
                Quantity.setText(String.valueOf(itemCount-1));
            }
        });
        addBtn.setOnClickListener(view -> {
            int quantity=Integer.parseInt(Quantity.getText().toString());
            Quantity.setText(String.valueOf(quantity+1));
        });
//        FirebaseMessaging.getInstance().getToken().
//                addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        String token=task.getResult();
//                        Log.d(TAG, "onComplete: "+token);
//                        if(UID!=null) {
//                            FirebaseDatabase.getInstance().getReference("Tokens").child(UID).setValue(token);
//                        }
//                    }
//                });
    }

    private void notifyVendor() {

        TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = "Customer Request";
        NOTIFICATION_MESSAGE = "Please Refill Water for Me";

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", NOTIFICATION_TITLE);
            notificationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotifyVendor.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
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