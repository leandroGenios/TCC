package com.tcc.maispratos.activity.ingrediente;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tcc.maispratos.R;

import java.util.ArrayList;

public class IngredientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), CadastroIngredienteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        RecyclerView rcvIngrediente = (RecyclerView) findViewById(R.id.rcvIngrediente);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvIngrediente.setLayoutManager(layoutManager);
        IngredienteAdapter adapter = new IngredienteAdapter(new ArrayList<Ingrediente>(0));
        rcvIngrediente.setAdapter(adapter);
        rcvIngrediente.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        UnidadeMedida unidade = new UnidadeMedida();
        unidade.setTipo("lata");
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome("MASSA DE TOMATE");
        ingrediente.setQtde(4);
        ingrediente.setUnidadeMedida(unidade);

        adapter.updateList(ingrediente);

        UnidadeMedida unidade2 = new UnidadeMedida();
        unidade2.setTipo("pacote 1Kg");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Açucar");
        ingrediente2.setQtde(1);
        ingrediente2.setUnidadeMedida(unidade2);

        adapter.updateList(ingrediente2);

    }

}
