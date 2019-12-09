package com.example.nfc_gacor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.nfc_gacor.R;

public class MainActivity extends AppCompatActivity {
ImageButton btntopup, btnbayar, btnaudit, btnrefund;
MaterialRippleLayout mt1, mt2, mt3, mt4, mt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mt1 = findViewById(R.id.material_topup);
        mt2 = findViewById(R.id.material_food);
        mt3 = findViewById(R.id.material_refund);
        mt4 = findViewById(R.id.material_audit);
        mt5 = findViewById(R.id.material_about);
//        btntopup=findViewById(R.id.btntopup);
//        btnbayar= findViewById(R.id.btnbayar);
//        btnaudit = findViewById(R.id.btnaudit);
//        btnrefund= findViewById(R.id.btnrefund);
        mt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TopUpActivity.class);
                startActivity(i);
            }
        });
        mt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProdukActivity.class);
                startActivity(i);
            }
        });
        mt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RefundActivity.class);
                startActivity(i);
            }
        });
        mt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AuditActivity.class);
                startActivity(i);
            }
        });


    }
}
