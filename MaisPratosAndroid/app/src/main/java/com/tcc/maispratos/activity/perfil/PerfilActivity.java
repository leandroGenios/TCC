package com.tcc.maispratos.activity.perfil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.util.BaseMenuActivity;

public class PerfilActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle("Perfil");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
    }
}
