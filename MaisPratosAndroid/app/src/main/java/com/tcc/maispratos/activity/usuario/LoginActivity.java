package com.tcc.maispratos.activity.usuario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
import com.tcc.maispratos.activity.prato.PratosActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCcadastrar;
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        iniciaElementos();
    }

    public void iniciaElementos(){
        edtEmail  = (EditText) findViewById(R.id.edtEmailLogin);
        edtSenha  = (EditText) findViewById(R.id.pwdSenhaLogin);

        btnCcadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCcadastrar.setOnClickListener(acaoCadastrar());

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(acaoEntrar());
    }

    private View.OnClickListener acaoCadastrar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        };
        return onClickListener;
    }

    private View.OnClickListener acaoEntrar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarPreenchimento()){
                    Usuario usuario = login();
                    if(usuario != null){
                        Intent intent = new Intent(getApplicationContext(), IngredientesActivity.class);
                        intent.putExtra("usuario", usuario);
                    }else{
                        exibirMensagem("Usuario ou senha inválido.");
                    }
                }else{
                    exibirMensagem("Todos os campos devem ser preenchidos");
                }
            }
        };
        return onClickListener;
    }

    private Usuario popularUsuario(){
        Usuario usuario = new Usuario();
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setSenha(edtSenha.getText().toString());

        return usuario;
    }

    private boolean verificarPreenchimento(){
        if(edtEmail.getText() == null || edtEmail.getText().toString().equals("")){
            return false;
        }

        if(edtSenha.getText() == null || edtSenha.getText().toString().equals("")){
            return false;
        }

        return true;
    }

    private Usuario login(){
        Usuario usuario = null;
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/login/" + edtEmail.getText().toString() + "/" + edtSenha.getText().toString();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            if(usuarioLogado(json)){
                usuario = new Usuario();
                JSONObject jsonObj = new JSONObject(json);
                usuario.setId(jsonObj.getInt("id"));
                usuario.setNome(jsonObj.getString("nome"));
                usuario.setEmail(jsonObj.getString("email"));
                usuario.setSenha(jsonObj.getString("senha"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar autenticar os dados. Tente novamente mais tarde.");
        }

        return usuario;
    }

    private boolean usuarioLogado(String json){
        return !json.equals("false");
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
}
