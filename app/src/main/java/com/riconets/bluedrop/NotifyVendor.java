package com.riconets.bluedrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotifyVendor extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView datePick, Quantity,QuantityTxt;
    String[] WaterPackages={"500ml","1L","5L","10L","20L","25L","30L","Over 30L"};
    String[] remainingAmount={"Quarter Level","Half Level"};
    private ImageButton addBtn, lessBtn;
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
        datePick=findViewById(R.id.date_pick);
        QuantityTxt=findViewById(R.id.quantityTxt);
        notifyEdiTxt=findViewById(R.id.notifyTxt);
        addBtn=findViewById(R.id.addBtn);
        notifyBtn=findViewById(R.id.notify);
        linearLayout=findViewById(R.id.Quantity);
        lessBtn=findViewById(R.id.lessBtn);
        Quantity=findViewById(R.id.quantity);
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
                startActivity(new Intent(getApplicationContext(),CustomerHome.class));
            }
        });
        packageAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            String Amount=adapterView.getItemAtPosition(i).toString();
            if(Amount=="Over 30L"){
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
        java.util.Date CurrentDate=new Date(System.currentTimeMillis());
        try {
            Date pickedDate=formatter.parse(Date);
            int result=CurrentDate.compareTo(pickedDate);
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