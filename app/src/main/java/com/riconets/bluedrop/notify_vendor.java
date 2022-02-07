package com.riconets.bluedrop;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;

public class notify_vendor extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView datePick, Quantity;
    private ImageButton addBtn, lessBtn;
    private EditText notifyEdiTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_vendor);
        datePick=findViewById(R.id.date_pick);
        notifyEdiTxt=findViewById(R.id.notifyTxt);
        addBtn=findViewById(R.id.addBtn);
        lessBtn=findViewById(R.id.lessBtn);
        Quantity=findViewById(R.id.quantity);
        datePick.setOnClickListener(view -> showDateDialog());
        lessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemCount=Integer.parseInt(Quantity.getText().toString());
                if(itemCount==0){
                    FancyToast.makeText(getApplicationContext(),"Quantity can not be less than 0",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
                else{
                    Quantity.setText(String.valueOf(itemCount-1));
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity=Integer.parseInt(Quantity.getText().toString());
                Quantity.setText(String.valueOf(quantity+1));

            }
        });
    }

    private void showDateDialog() {
        DatePickerDialog datePickerDialog= new DatePickerDialog(
                this,this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        stringBuilder.append("/");
        stringBuilder.append(i1+1);
        stringBuilder.append("/");
        stringBuilder.append(i2);
        String Date=stringBuilder.toString();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dd");
        //get Current Date
        Date CurrentDate=new Date(System.currentTimeMillis());
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