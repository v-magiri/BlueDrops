package com.riconets.bluedrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.riconets.bluedrop.model.CartModel;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Refill extends AppCompatActivity {
    private AutoCompleteTextView waterPackageAutoComplete;
    private TextView QuantityTxt,PriceTagTxt,TotalPriceTxt;
    String ProductSizes[]={"500ml","1L","5L","10L","20L"};
    private Button RefillBtn;
    private ImageButton lessBtn,AddBtn;
    private CheckBox bottleStatus;
    ArrayAdapter<String> arrayAdapter;
    private String ProductName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);
        waterPackageAutoComplete=findViewById(R.id.packageAutoComplete);
        QuantityTxt=findViewById(R.id.quantity);
        lessBtn=findViewById(R.id.lessBtn);
        AddBtn=findViewById(R.id.addBtn);
        PriceTagTxt=findViewById(R.id.priceTag);
        arrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.item,ProductSizes);
        bottleStatus=findViewById(R.id.emptyTag);
        TotalPriceTxt=findViewById(R.id.sumPriceTag);
        RefillBtn=findViewById(R.id.notify);
        lessBtn.setOnClickListener(view -> {
            int itemCount=Integer.parseInt(QuantityTxt.getText().toString());
            if(itemCount==0){
                FancyToast.makeText(getApplicationContext(),"Quantity can not be less than 0",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
            }
            else{
                QuantityTxt.setText(String.valueOf(itemCount-1));
            }
        });
        AddBtn.setOnClickListener(view -> {
            int quantity=Integer.parseInt(QuantityTxt.getText().toString());
            QuantityTxt.setText(String.valueOf(quantity+1));

        });
        RefillBtn.setOnClickListener(v -> {
            
        });



    }
}