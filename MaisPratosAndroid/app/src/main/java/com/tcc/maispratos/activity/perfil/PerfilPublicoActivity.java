package com.tcc.maispratos.activity.perfil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.perfil.PratoPerfilPublicoAdapter;
import com.tcc.maispratos.prato.Prato;
import com.tcc.maispratos.prato.PratoAdapter;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PerfilPublicoActivity extends BaseMenuActivity {
    private TextView txtNome;
    private TextView txtNivel;
    private TextView txtQtdePratos;
    private RecyclerView rcvPratos;
    private Button btnAddAmigo;
    private Usuario amigo;

    private PratoPerfilPublicoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_publico);
        setTitle("Perfil");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        amigo = (Usuario) getIntent().getExtras().getSerializable("amigo");
        iniciaElementos();
        listPratos();
    }

    private void iniciaElementos(){
        txtNome = findViewById(R.id.txtNome);
        txtNivel = findViewById(R.id.txtNivel);
        txtQtdePratos = findViewById(R.id.txtQtdePratos);
        rcvPratos = findViewById(R.id.rcvPratos);
        btnAddAmigo = findViewById(R.id.btnAddAmigo);

        txtNome.setText(amigo.getNome());
        txtNivel.setText(getClassificacao());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvPratos.setLayoutManager(layoutManager);
        adapter = new PratoPerfilPublicoAdapter(new ArrayList<Prato>(0), this);
        rcvPratos.setAdapter(adapter);
        rcvPratos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void listPratos(){
        List<Prato> list = null;
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "prato/meus/" + amigo.getId();
        String json = null;
        connection.execute(params);
        try {
            json = (String) connection.get();
            Type listType = new TypeToken<ArrayList<Prato>>(){}.getType();
            list = new Gson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.clear();
        if(list != null){
            txtQtdePratos.setText(list.size() + " pratos adicionados");
            for (Prato prato: list) {
                adapter.updateList(prato);
            }
        }
    }

    private String getClassificacao(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/classificacao/" + amigo.getId();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<String>(){}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar buscar os dados. Tente novamente mais tarde.");
        }

        return "";
    }
}
