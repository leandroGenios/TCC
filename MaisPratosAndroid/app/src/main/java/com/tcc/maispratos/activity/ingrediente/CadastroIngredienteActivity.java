package com.tcc.maispratos.activity.ingrediente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.BaseIngrediente;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.unidademedida.UnidadeMedida;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;
import com.tcc.maispratos.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CadastroIngredienteActivity extends BaseIngrediente {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ingrediente);
        setTitle("Cadastro de ingrediente");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
        iniciaAutoCompleteUnidadeMedida();
        iniciaAutoCompleteIngrediente();
    }

    @Override
    public void iniciaElementos(){
        btnInserirCodBarras = (Button) findViewById(R.id.btnCodBarras);
        edtCodigoBarras = (EditText) findViewById(R.id.edtCodBarras);
        aucNomeIngrediente = (AutoCompleteTextView) findViewById(R.id.aucNome);
        edtQuantidade = (EditText) findViewById(R.id.edtQuantidade);
        aucUnidadeMedida = (AutoCompleteTextView) findViewById(R.id.aucUnidadeMedida);
        btnAdicionar = (Button) findViewById(R.id.btnAdicionarIngrediente);

        btnInserirCodBarras.setOnClickListener(getCodigoBarras());
        edtCodigoBarras.setOnFocusChangeListener(focusOutCodigoBarras());
        btnAdicionar.setOnClickListener(salvarIngrediente());
    }

    @Override
    public View.OnClickListener salvarIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    Boolean retorno = cadastrarIngrediente();
                    if(retorno != null){
                        if(retorno == true){
                            Intent intent = new Intent(getApplicationContext(), IngredientesActivity.class);
                            intent.putExtra("usuario", getUsuario());
                            intent.putExtra("acao", "Cadastro realizado");
                            startActivity(intent);
                            finish();
                        }else if(retorno == false){

                            exibirErro("Não foi possível salvar o ingrediente. Tente novamente mais tarde.");
                        }
                    }
                }
            }
        };
        return onClickListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        String codigo = result.getContents();
        edtCodigoBarras.setText(codigo);
        if(edtCodigoBarras.getText().toString().length() > 0){
            if(verificarCodigoBarras()){
                verificarIngrediente(getIngredienteByCodigoBarras());
            }else{
                exibirMensagem("O código precisa ter " + Constants.QTDE_DIGITOS_CODIGO_MARRAS + " dígitos.");
                aucNomeIngrediente.setText("");
            }
        }
    }

    private Boolean cadastrarIngrediente(){
        Ingrediente ingrediente = new Ingrediente();
        if(edtCodigoBarras.getText() != null && !edtCodigoBarras.getText().toString().equals("")){
            ingrediente.setCodigoBarras(Double.parseDouble(edtCodigoBarras.getText().toString()));
        }
        ingrediente.setQuantidade(Float.parseFloat(edtQuantidade.getText().toString()));

        for (Ingrediente ingred: ingredientes) {
            if(ingred.getNome().equals(aucNomeIngrediente.getText().toString())){
                ingrediente.setNome(ingred.getNome());
                break;
            }
        }
        if(ingrediente.getNome() == null){
            if(aucNomeIngrediente.getText() != null && !aucNomeIngrediente.getText().toString().equals("")){
                ingrediente.setNome(aucNomeIngrediente.getText().toString());
            }
        }

        for (UnidadeMedida unidadeMedida: unidadesMedida) {
            if(unidadeMedida.getSigla().equals(aucUnidadeMedida.getText().toString())){
                ingrediente.setUnidadeMedida(unidadeMedida);
                break;
            }
        }

        getUsuario().setIngrediente(ingrediente);

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente";
        String gson = new Gson().toJson(getUsuario());
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            String retorno = ((String) connection.get());
            if(retorno.toLowerCase().split("duplicate").length > 1){
                exibirAlerta("A sua lista já contem este ingrediente");
                return null;
            }else{
                return ((String) connection.get()).equals("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao cadastrar. Tente novamente mais tarde.");
        }

        return false;
    }

    public void exibirAlerta(String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta");
        builder.setMessage(mensagem);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getApplicationContext(), IngredientesActivity.class);
                intent.putExtra("usuario", getUsuario());
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

}
