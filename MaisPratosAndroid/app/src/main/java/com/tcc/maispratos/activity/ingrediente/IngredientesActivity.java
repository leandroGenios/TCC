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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class IngredientesActivity extends BaseMenuActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView rcvIngrediente;
    private IngredienteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);
        setTitle("Lista de ingredientes");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));

        iniciaElementos();
        montaLista(getIngredientes());
    }

    private void iniciaElementos(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(addIngrediente());

        rcvIngrediente = (RecyclerView) findViewById(R.id.rcvIngrediente);
        rcvIngrediente.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredienteAdapter(new ArrayList<Ingrediente>(0), this);
        rcvIngrediente.setAdapter(adapter);
        rcvIngrediente.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private View.OnClickListener addIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), CadastroIngredienteActivity.class);
                intent.putExtra("usuario", getUsuario());
                startActivity(intent);
                finish();
            }
        };
        return onClickListener;
    }

    private List<Ingrediente> getIngredientes(){
        List<Ingrediente> list = null;
        TaskConnection connection = new TaskConnection();
        String[] params = new String[Constants.QUERY_SEM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente/" + getUsuario().getId();

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
