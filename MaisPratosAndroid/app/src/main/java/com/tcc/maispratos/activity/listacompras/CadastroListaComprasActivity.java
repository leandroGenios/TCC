package com.tcc.maispratos.activity.listacompras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tcc.maispratos.R;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.unidademedida.UnidadeMedida;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CadastroListaComprasActivity extends BaseMenuActivity {
    private AutoCompleteTextView acObjText;
    private ArrayAdapter<String> adapter;
    private Button insereCodBarras;
    private EditText edtCodigoBarras;
    private EditText edtNome;
    private EditText edtQuantidade;
    private List<UnidadeMedida> unidadesMedida;
    private ArrayList<String> unidadesMedidaArray;
    private Button adicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_lista_compras);
        setTitle("Cadastro de lista de compras");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
        iniciaAutoComplete();
    }

    private void iniciaElementos(){
        insereCodBarras = (Button) findViewById(R.id.btnCodBarrasListaCompras);
        edtCodigoBarras = (EditText) findViewById(R.id.edtCodBarrasListaCompras);
        edtNome = (EditText) findViewById(R.id.edtNomeListaCompras);
        edtQuantidade = (EditText) findViewById(R.id.edtQuantidadeListaCompras);
        acObjText = (AutoCompleteTextView) findViewById(R.id.aucUnidadeMedidaListaCompras);
        adicionar = (Button) findViewById(R.id.btnAdicionarIngredienteListaCompras);

        insereCodBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCodBarras();
            }
        });
        adicionar.setOnClickListener(addIngrediente());
        edtCodigoBarras.setOnFocusChangeListener(serFocus());
    }

    private void iniciaAutoComplete(){
        getStringArray(getUnidadesMedida());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, unidadesMedidaArray);
        acObjText.setAdapter(adapter);
    }

    private List<UnidadeMedida> getUnidadesMedida(){
        unidadesMedida = null;
        TaskConnection connection = new TaskConnection();
        String[] params = new String[Constants.QUERY_SEM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "unidadeMedida";

        String json = null;
        connection.execute(params);
        try {
            json = (String) connection.get();
            Type listType = new TypeToken<ArrayList<UnidadeMedida>>(){}.getType();
            unidadesMedida = new Gson().fromJson(json, listType);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return unidadesMedida;
    }

    private void getStringArray(List<UnidadeMedida> unidadesMedida){
        unidadesMedidaArray = new ArrayList<>();
        for (UnidadeMedida unidadeMedida : unidadesMedida) {
            unidadesMedidaArray.add(unidadeMedida.getSigla());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        String codigo = result.getContents();
        edtCodigoBarras.setText(codigo);
    }

    public void initCodBarras(){
        new IntentIntegrator(this).initiateScan();
    }

    private View.OnClickListener addIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    if(cadastrarIngrediente()){
                        Intent intent = new Intent(getApplicationContext(), ListaComprasActivity.class);
                        intent.putExtra("usuario", getUsuario());
                        startActivity(intent);
                        finish();
                    }else{
                        exibirErro("Não foi possível salvar o ingrediente. Tente novamente mais tarde.");
                    }
                }
            }
        };
        return onClickListener;
    }

    private View.OnFocusChangeListener serFocus(){
        View.OnFocusChangeListener focus = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if(verificarCodigoBarras()){
                        validarIngrediente(getIngredienteByCodigoBarras());
                    }else{
                        exibirMensagem("O código precisa ter " + Constants.QTDE_DIGITOS_CODIGO_MARRAS + " dígitos.");
                        edtCodigoBarras.requestFocus();
                    }
                }
            }
        };

        return focus;
    }

    private void validarIngrediente(Ingrediente ingrediente){
        if(ingrediente != null){
            edtNome.setText(ingrediente.getNome());
            edtNome.setEnabled(false);
        }else{
            edtNome.setEnabled(true);
        }
    }

    private boolean verificarCodigoBarras(){
        return edtCodigoBarras.getText().toString().length() == Constants.QTDE_DIGITOS_CODIGO_MARRAS;
    }

    private Ingrediente getIngredienteByCodigoBarras(){
        Ingrediente ingrediente = null;
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente/getByCodigoBarras/" + edtCodigoBarras.getText().toString();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            if(!json.equals("")){
                ingrediente = new Ingrediente();
                JSONObject jsonObj = new JSONObject(json);
                ingrediente.setId(jsonObj.getInt("id"));
                ingrediente.setNome(jsonObj.getString("nome"));
                ingrediente.setCodigoBarras(jsonObj.getInt("codigoBarras"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao buscar o código de barras. Tente novamente mais tarde.");
        }

        return ingrediente;
    }

    private boolean validarCampos(){
        if(!validarPreenchimento()){
            exibirMensagem("Todos os campos devem ser preenchidos.");
            return false;
        }
        return true;
    }

    private boolean validarPreenchimento(){
        if(edtCodigoBarras.getText() == null || edtCodigoBarras.getText().toString().equals("")){
            edtCodigoBarras.requestFocus();
            return false;
        }

        if(edtNome.getText() == null || edtNome.getText().toString().equals("")){
            edtNome.requestFocus();
            return false;
        }

        if(edtQuantidade.getText() == null || edtQuantidade.getText().toString().equals("")){
            edtQuantidade.requestFocus();
            return false;
        }

        if(acObjText.getText() == null || acObjText.getText().toString().equals("")){
            acObjText.requestFocus();
            return false;
        }

        return true;
    }

    private boolean cadastrarIngrediente(){
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setCodigoBarras(Double.parseDouble(edtCodigoBarras.getText().toString()));
        ingrediente.setQuantidade(Float.parseFloat(edtQuantidade.getText().toString()));
        ingrediente.setNome(edtNome.getText().toString());
        for (UnidadeMedida unidadeMedida: unidadesMedida) {
            if(unidadeMedida.getSigla().equals(acObjText.getText().toString())){
                ingrediente.setUnidadeMedida(unidadeMedida);
                break;
            }
        }

        getUsuario().setIngrediente(ingrediente);

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "listaCompras";
        String gson = new Gson().toJson(getUsuario());
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            return ((String) connection.get()).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao cadastrar. Tente novamente mais tarde.");
        }

        return false;
    }
}
