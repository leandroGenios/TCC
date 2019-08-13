package com.tcc.maispratos.activity.perfil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.prato.CadastroPratoActivity;
import com.tcc.maispratos.activity.prato.PratosActivity;
import com.tcc.maispratos.activity.usuario.UpdatePerfilActivity;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.lang.reflect.Type;

public class PerfilActivity extends BaseMenuActivity {

    private TextView txtNivelCozinheiro;
    private ProgressBar progressBar;
    private TextView txtProximoNivel;
    private TextView txtQtdePratosCadastrados;
    private TextView txtQtdePratosAvaliados;
    private TextView txtNomeUsuario;
    private TextView txtEmailUsuario;
    private Button btnEditarDados;
    private Button btnMeusPratos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle("Perfil");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
    }

    public void iniciaElementos(){
        txtNivelCozinheiro = findViewById(R.id.txtNivelCozinheiro);
        progressBar = findViewById(R.id.progressBar);
        txtProximoNivel = findViewById(R.id.txtProximoNivel);
        txtQtdePratosCadastrados = findViewById(R.id.txtQtdePratosCadastrados);
        txtQtdePratosAvaliados = findViewById(R.id.txtQtdePratosAvaliados);
        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);
        btnEditarDados = findViewById(R.id.btnEditarDados);
        btnMeusPratos = findViewById(R.id.btnMeusPratos);

        txtNivelCozinheiro.setText(getClassificacao());
        int progresso = getPróximoNivel();
        progressBar.setProgress((100 / 5) * (5 - progresso));
        txtProximoNivel.setText("Faltam " + progresso + " pratos para o próximo nível");
        txtQtdePratosCadastrados.setText(getCountMeusPratos() + " pratos cadastrados");
        txtQtdePratosAvaliados.setText(getCountPratosAvaliados() + " pratos avaliados");
        txtNomeUsuario.setText(getUsuario().getNome());
        txtEmailUsuario.setText(getUsuario().getEmail());
        btnMeusPratos.setOnClickListener(exibirMeusPratos());
        btnEditarDados.setOnClickListener(editarDados());
    }

    public View.OnClickListener exibirMeusPratos(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PratosActivity.class);
                intent.putExtra("usuario", getUsuario());
                intent.putExtra("aba", "MEUS");
                startActivity(intent);
            }
        };
        return onClickListener;
    }

    public View.OnClickListener editarDados(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdatePerfilActivity.class);
                intent.putExtra("usuario", getUsuario());
                intent.putExtra("aba", "MEUS");
                startActivity(intent);
            }
        };
        return onClickListener;
    }

    private String getClassificacao(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/classificacao/" + getUsuario().getId();
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

    private Integer getCountPratosAvaliados(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "prato/minhasAvaliacoes/" + getUsuario().getId();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<Integer>(){}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar buscar os dados. Tente novamente mais tarde.");
        }

        return 0;
    }

    private Integer getCountMeusPratos(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "prato/meusPratos/" + getUsuario().getId();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<Integer>(){}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar buscar os dados. Tente novamente mais tarde.");
        }

        return 0;
    }

    private Integer getPróximoNivel(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/proximoNivel/" + getUsuario().getId();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<Integer>(){}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar buscar os dados. Tente novamente mais tarde.");
        }

        return 0;
    }
}
