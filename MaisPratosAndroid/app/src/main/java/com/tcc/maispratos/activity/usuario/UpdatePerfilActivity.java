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
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UpdatePerfilActivity extends BaseMenuActivity {
    private EditText edtNome;
    private EditText edtEmailAtual;
    private EditText edtEmailNovo;
    private EditText edtConfirmaEmail;
    private EditText edtSenhaAtual;
    private EditText edtSenhaNova;
    private EditText edtConfirmaSenha;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_perfil);
        setTitle("Editar Meus Dados");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
        carregarCampos();
    }

    private void iniciaElementos(){
        edtNome = findViewById(R.id.edtCadastroNome);
        edtEmailAtual = findViewById(R.id.edtCadastroEmailAtual);
        edtEmailNovo = findViewById(R.id.edtCadastroEmail);
        edtConfirmaEmail = findViewById(R.id.edtCadastroConfirmaEmail);
        edtSenhaAtual = findViewById(R.id.edtCadastroSenhaAtual);
        edtSenhaNova = findViewById(R.id.edtCadastroNovaSenha);
        edtConfirmaSenha = findViewById(R.id.edtCadastroConfirmaSenha);

        btnSalvar = (Button) findViewById(R.id.btnCadastroSalvar);
        btnSalvar.setOnClickListener(acaoSalvar());
    }

    private void carregarCampos(){
        edtNome.setText(getUsuario().getNome());
        edtEmailAtual.setText(getUsuario().getEmail());
        edtEmailAtual.setEnabled(false);
    }

    private View.OnClickListener acaoSalvar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarCampos()){
                    alterarUsuario();
                }
            }
        };
        return onClickListener;
    }

    private boolean verificarCampos(){
        if(!verificarPreenchimento()){
            return false;
        }
        return true;
    }

    private boolean verificarPreenchimento(){
        if(edtNome.getText() == null || edtNome.getText().toString().equals("")){
            exibirMensagem("O nome deve ser preenchido.");
            return false;
        }
        if(edtEmailNovo.getText() != null && !edtEmailNovo.getText().toString().equals("")){
            if(edtConfirmaEmail.getText() == null || edtConfirmaEmail.getText().toString().equals("")){
                exibirMensagem("Comfirme o novo e-mail.");
                return false;
            }

            if(!compararEmails()){
                exibirMensagem("Os campos de e-mail não estão iguais.");
                return false;
            }

            if(emailExiste()){
                exibirMensagem("O e-mail informado já existe.");
                return false;
            }
        }
        if(edtSenhaNova.getText() != null && !edtSenhaNova.getText().toString().equals("")){
            if(edtSenhaAtual.getText() == null || edtSenhaAtual.getText().toString().equals("")){
                exibirMensagem("Digite a senha atual.");
                return false;
            }
            if(edtConfirmaSenha.getText() == null || edtConfirmaSenha.getText().toString().equals("")){
                exibirMensagem("Comfirme a nova senha.");
                return false;
            }

            if(!compararSenhas()){
                exibirMensagem("Os campos de senha não estão iguais.");
                return false;
            }

            if(!compararSenhaAtual()){
                exibirMensagem("A senha atual é inválida.");
                return false;
            }
        }
        return true;
    }

    private boolean compararEmails(){
        return edtEmailNovo.getText().toString().equals(edtConfirmaEmail.getText().toString());
    }

    private boolean compararSenhas(){
        return edtSenhaNova.getText().toString().equals(edtConfirmaSenha.getText().toString());
    }

    private boolean compararSenhaAtual(){
        return edtSenhaAtual.getText().toString().equals(getUsuario().getSenha());
    }

    private boolean emailExiste(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/emailExiste/" + edtEmailNovo.getText().toString();
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
    private void alterarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setId(getUsuario().getId());
        usuario.setNome(edtNome.getText().toString());
        usuario.setSenha(edtSenhaNova.getText().toString());
        usuario.setEmail(edtEmailNovo.getText().toString());

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.PUT;
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
            getUsuario().setSenha(usuario.getSenha());
            getUsuario().setEmail(usuario.getEmail());
            getUsuario().setNome(usuario.getNome());
            exibeMensagemSucesso();
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao cadastrar. Tente novamente mais tarde.");
        }
    }

    private Usuario getUsuario(String json){
        return new Gson().fromJson(json, Usuario.class);
    }

    private void exibeMensagemSucesso(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sucesso");
        builder.setMessage("Perfil atualizado");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

}
