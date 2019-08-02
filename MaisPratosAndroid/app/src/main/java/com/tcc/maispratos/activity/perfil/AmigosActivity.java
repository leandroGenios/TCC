package com.tcc.maispratos.activity.perfil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.perfil.PessoaAdapter;
import com.tcc.maispratos.prato.IngredientePratoAdapter;
import com.tcc.maispratos.util.BaseMenuActivity;

import java.util.ArrayList;

public class AmigosActivity extends BaseMenuActivity {
    private TextView edtNome;
    private Button btnBuscar;
    private RecyclerView rcvAmigos;
    private PessoaAdapter adapter;

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

        rcvAmigos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PessoaAdapter(new ArrayList<Usuario>(0), this);
        rcvAmigos.setAdapter(adapter);
        rcvAmigos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.updateList(getUsuario());
    }
}
