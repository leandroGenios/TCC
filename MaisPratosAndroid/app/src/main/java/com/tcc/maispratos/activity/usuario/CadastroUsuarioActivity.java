package com.tcc.maispratos.activity.usuario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtConfirmaEmail;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;
    private Button btnSalvar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_usuario);

        iniciaElementos();
    }

    private void iniciaElementos(){
        edtNome = (EditText) findViewById(R.id.edtCadastroNome);
        edtEmail = (EditText) findViewById(R.id.edtCadastroEmail);
        edtConfirmaEmail = (EditText) findViewById(R.id.edtCadastroConfirmaEmail);
        edtSenha = (EditText) findViewById(R.id.edtCadastroSenha);
        edtConfirmaSenha = (EditText) findViewById(R.id.edtCadastroConfirmaSenha);

        btnSalvar = (Button) findViewById(R.id.btnCadastroSalvar);
        btnSalvar.setOnClickListener(acaoSalvar());
    }

    private View.OnClickListener acaoSalvar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarCampos()){
                    cadastrarUsuario();
                }
            }
        };
        return onClickListener;
    }

    private boolean verificarCampos(){
        if(!verificarPreenchimento()){
            exibirMensagem("Todos os campos devem ser preenchidos.");
            return false;
        }
        if(!compararEmails()){
            exibirMensagem("Os campos de e-mail não estão iguais.");
            return false;
        }
        if(!compararSenhas()){
            exibirMensagem("Os campos de senha não estão iguais.");
            return false;
        }
        if(nomeExiste()){
            exibirMensagem("O nome informado já existe. Por favor, tente com outro nome.");
            return false;
        }
        if(emailExiste()){
            exibirMensagem("O e-mail informado já existe.");
            return false;
        }

        return true;
    }

    private boolean verificarPreenchimento(){
        if(edtNome.getText() == null || edtNome.getText().toString().equals("")){
            return false;
        }
        if(edtEmail.getText() == null || edtEmail.getText().toString().equals("")){
            return false;
        }
        if(edtConfirmaEmail.getText() == null || edtConfirmaEmail.getText().toString().equals("")){
            return false;
        }
        if(edtSenha.getText() == null || edtSenha.getText().toString().equals("")){
            return false;
        }
        if(edtConfirmaSenha.getText() == null || edtConfirmaSenha.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private boolean compararEmails(){
        return edtEmail.getText().toString().equals(edtConfirmaEmail.getText().toString());
    }

    private boolean compararSenhas(){
        return edtSenha.getText().toString().equals(edtConfirmaSenha.getText().toString());
    }

    private boolean emailExiste(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/emailExiste/" + edtEmail.getText().toString();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar validar o e-mail. Tente novamente mais tarde.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar validar o e-mail. Tente novamente mais tarde.");
        }

        if(!json.equals("true") && !json.equals("false")){
            exibirErro("Ocorreu um problema ao tentar validar o e-mail. Tente novamente mais tarde.");
        }

        return json.equals("true");
    }

    private boolean nomeExiste(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/nomeExiste/" + edtNome.getText().toString();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar validar o nome. Tente novamente mais tarde.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar validar o nome. Tente novamente mais tarde.");
        }

        if(!json.equals("true") && !json.equals("false")){
            exibirErro("Ocorreu um problema ao tentar validar o nome. Tente novamente mais tarde.");
        }

        return json.equals("true");
    }

    private void cadastrarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome(edtNome.getText().toString());
        usuario.setSenha(edtSenha.getText().toString());
        usuario.setEmail(edtEmail.getText().toString());

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "usuario";

        String gson = new Gson().toJson(usuario);
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            usuario = getUsuario(json);
            exibeMensagemSucesso(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao cadastrar. Tente novamente mais tarde.");
        }
    }

    private Usuario getUsuario(String json){
        return new Gson().fromJson(json, Usuario.class);
    }

    private void exibeMensagemSucesso(final Usuario usuario){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sucesso");
        builder.setMessage("Cadastro realizado");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getApplicationContext(), IngredientesActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void exibirErro(String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erro");
        builder.setMessage(mensagem);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void exibirMensagem(String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(mensagem);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
