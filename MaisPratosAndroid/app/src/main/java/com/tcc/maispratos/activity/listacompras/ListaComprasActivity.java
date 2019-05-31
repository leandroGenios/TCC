package com.tcc.maispratos.activity.listacompras;

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
import com.tcc.maispratos.activity.ingrediente.Ingrediente;
import com.tcc.maispratos.activity.ingrediente.IngredienteAdapter;
import com.tcc.maispratos.activity.ingrediente.UnidadeMedida;
import com.tcc.maispratos.util.BaseMenuActivity;

import java.util.ArrayList;

public class ListaComprasActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView rcvIngrediente = (RecyclerView) findViewById(R.id.rcvIngrediente);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvIngrediente.setLayoutManager(layoutManager);
        ListaComprasAdapter adapter = new ListaComprasAdapter(new ArrayList<Ingrediente>(0), this);
        rcvIngrediente.setAdapter(adapter);
        rcvIngrediente.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        UnidadeMedida unidade = new UnidadeMedida();
        unidade.setSigla("lata");
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(1);
        ingrediente.setNome("MASSA DE TOMATE");
        ingrediente.setQuantidade(4);
        ingrediente.setUnidadeMedida(unidade);

        adapter.updateList(ingrediente);

        UnidadeMedida unidade2 = new UnidadeMedida();
        unidade2.setSigla("pacote 1Kg");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setId(2);
        ingrediente2.setNome("Açucar");
        ingrediente2.setQuantidade(1);
        ingrediente2.setUnidadeMedida(unidade2);

        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
        adapter.updateList(ingrediente2);
    }

}
