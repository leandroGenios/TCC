package com.tcc.maispratos.activity.listacompras;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.listacompras.ListaComprasAdapter;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListaComprasActivity extends BaseMenuActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView rcvIngrediente;
    private ListaComprasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);
        setTitle("Lista de compras");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));

        iniciaElementos();
        montaLista(getListaCompras());
    }

    private void iniciaElementos(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(addIngrediente());

        rcvIngrediente = (RecyclerView) findViewById(R.id.rcvListaCompras);
        rcvIngrediente.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListaComprasAdapter(new ArrayList<Ingrediente>(0), this);
        rcvIngrediente.setAdapter(adapter);
        rcvIngrediente.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private View.OnClickListener addIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), CadastroListaComprasActivity.class);
                intent.putExtra("usuario", getUsuario());
                startActivity(intent);
                finish();
            }
        };
        return onClickListener;
    }

    private List<Ingrediente> getListaCompras(){
        List<Ingrediente> list = null;
        TaskConnection connection = new TaskConnection();
        String[] params = new String[Constants.QUERY_SEM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "listaCompras/" + getUsuario().getId();

        String json = null;
        connection.execute(params);
        try {
            json = (String) connection.get();
            Type listType = new TypeToken<ArrayList<Ingrediente>>(){}.getType();
            list = new Gson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void montaLista(List<Ingrediente> ingredientes){
        for (Ingrediente ingrediente: ingredientes) {
            adapter.updateList(ingrediente);
        }
    }
}
