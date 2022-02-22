package com.riconets.bluedrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

public class Refill extends AppCompatActivity {
    private AutoCompleteTextView waterPackageAutoComplete;
    private TextView QuantityTxt,PriceTagTxt,TotalPriceTxt;
    private Button RefillBtn;
    private ImageButton lessBtn,AddBtn;
    private CheckBox bottleStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);
        waterPackageAutoComplete=findViewById(R.id.packageAutoComplete);
        QuantityTxt=findViewById(R.id.quantity);
        lessBtn=findViewById(R.id.lessBtn);
        AddBtn=findViewById(R.id.addBtn);
        PriceTagTxt=findViewById(R.id.priceTag);
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
        RefillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}