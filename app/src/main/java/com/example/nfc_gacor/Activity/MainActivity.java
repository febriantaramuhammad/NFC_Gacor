package com.example.nfc_gacor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.nfc_gacor.R;

public class MainActivity extends AppCompatActivity {
ImageButton btntopup, btnbayar;
Button btnaudit, btnrefund;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btntopup=findViewById(R.id.btntopup);
        btnbayar= findViewById(R.id.btnbayar);
        btnaudit = findViewById(R.id.btnaudit);
        btnrefund= findViewById(R.id.btnrefund);
        btnrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RefundActivity.class);
                startActivity(i);
            }
        });
        btntopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TopUpActivity.class);
                startActivity(i);
            }
        });
        btnaudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AuditActivity.class);
                startActivity(i);
            }
        });

        btnbayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProdukActivity.class);
                startActivity(i);
            }
        });
    }
}
