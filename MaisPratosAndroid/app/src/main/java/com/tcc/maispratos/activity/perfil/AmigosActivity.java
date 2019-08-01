package com.tcc.maispratos.activity.perfil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.util.BaseMenuActivity;

public class AmigosActivity extends BaseMenuActivity {
    private TextView edtNome;
    private Button btnBuscar;
    private RecyclerView rcvAmigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        setTitle("Amigos");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));

        iniciaElementos();
    }

    private void iniciaElementos(){
        edtNome = findViewById(R.id.edtNome);
        btnBuscar = findViewById(R.id.btnBuscar);
        rcvAmigos = findViewById(R.id.rcvAmigos);
    }
}
