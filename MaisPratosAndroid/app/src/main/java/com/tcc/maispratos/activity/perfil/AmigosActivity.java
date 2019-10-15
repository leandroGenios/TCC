package com.tcc.maispratos.activity.perfil;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.perfil.PessoaAdapter;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AmigosActivity extends BaseMenuActivity {
    private TextView edtNome;
    private Button btnBuscar;
    private RecyclerView rcvAmigos;
    private PessoaAdapter adapter;
    private List<Usuario> amigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        setTitle("Amigos");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));

        iniciaElementos();
        carregarLista();
    }

    private void iniciaElementos(){
        edtNome = findViewById(R.id.txtNome);
        btnBuscar = findViewById(R.id.btnBuscar);
        rcvAmigos = findViewById(R.id.rcvAmigos);

        rcvAmigos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PessoaAdapter(new ArrayList<Usuario>(0), this);
        rcvAmigos.setAdapter(adapter);
        rcvAmigos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        btnBuscar.setOnClickListener(buscar());
    }

    private List<Usuario> getAmigos(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/amigo/list/" + getUsuario().getId();

        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<List<Usuario>>(){}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema na busca. Tente novamente mais tarde.");
        }

        return null;
    }

    private List<Usuario> getUsuarios(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/" + getUsuario().getId();

        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<List<Usuario>>(){}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema na busca. Tente novamente mais tarde.");
        }

        return null;
    }

    public void carregarLista(){
        amigos = getAmigos();
        amigos.addAll(getUsuarios());

        adapter.clear();
        for (Usuario usuario: amigos) {
            adapter.updateList(usuario);
        }
    }

    private View.OnClickListener buscar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                String nome = edtNome.getText().toString();
                if(nome == null || nome.equals("")){
                    for (Usuario usuario: amigos) {
                        adapter.updateList(usuario);
                    }
                }else{
                    for (Usuario usuario: amigos) {
                        if(usuario.getNome().toLowerCase().contains(nome.toLowerCase())){
                            adapter.updateList(usuario);
                        }
                    }
                }
            }
        };
        return onClickListener;
    }
}
