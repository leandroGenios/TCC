package com.tcc.maispratos.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tcc.maispratos.R;

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro);
    }
}
