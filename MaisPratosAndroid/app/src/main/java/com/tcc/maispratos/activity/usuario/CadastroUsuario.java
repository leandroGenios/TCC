package com.tcc.maispratos.activity.usuario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tcc.maispratos.R;

public class CadastroUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_usuario);
    }
}
