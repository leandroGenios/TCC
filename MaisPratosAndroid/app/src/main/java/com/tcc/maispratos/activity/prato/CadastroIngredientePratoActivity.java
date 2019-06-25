package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.unidademedida.UnidadeMedida;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;
import com.tcc.maispratos.util.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CadastroIngredientePratoActivity extends BaseMenuActivity {
    private AutoCompleteTextView aucUnidadeMedida;
    private ArrayAdapter<String> adapterUnidadeMedida;
    private ArrayAdapter<String> adapterIngrediente;
    private Button insereCodBarras;
    private EditText edtCodigoBarras;
    private AutoCompleteTextView aucNome;
    private EditText edtQuantidade;
    private List<UnidadeMedida> unidadesMedida;
    private ArrayList<String> unidadesMedidaArray;
    private List<Ingrediente> ingredientes;
    private ArrayList<String> ingredienteArray;
    private Button adicionar;
    private Ingrediente ingrediente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ingrediente_prato);
        setTitle("Adicionar ingrediente");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
        iniciaAutoCompleteUnidadeMedida();
        iniciaAutoCompleteIngrediente();
    }

    private void iniciaElementos(){
        insereCodBarras = (Button) findViewById(R.id.btnCodBarras);
        edtCodigoBarras = (EditText) findViewById(R.id.edtCodBarras);
        aucNome = (AutoCompleteTextView) findViewById(R.id.aucNome);
        edtQuantidade = (EditText) findViewById(R.id.edtQuantidade);
        aucUnidadeMedida = (AutoCompleteTextView) findViewById(R.id.aucUnidadeMedida);
        adicionar = (Button) findViewById(R.id.btnAdicionarIngrediente);

        insereCodBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCodBarras();
            }
        });
        adicionar.setOnClickListener(addIngrediente());
        edtCodigoBarras.setOnFocusChangeListener(serFocus());
        aucNome.setOnItemClickListener(getIngrediente());
    }

    private void iniciaAutoCompleteUnidadeMedida(){
        getUnidadesMedidaToStringArray(getUnidadesMedida());
        adapterUnidadeMedida = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, unidadesMedidaArray);
        aucUnidadeMedida.setAdapter(adapterUnidadeMedida);
    }

    private void iniciaAutoCompleteIngrediente(){
        getIngredientesToStringArray(getIngredientes());
        adapterIngrediente = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredienteArray);
        aucNome.setAdapter(adapterIngrediente);
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

    private List<Ingrediente> getIngredientes(){
        ingredientes = null;
        TaskConnection connection = new TaskConnection();
        String[] params = new String[Constants.QUERY_SEM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente";

        String json = null;
        connection.execute(params);
        try {
            json = (String) connection.get();
            Type listType = new TypeToken<ArrayList<Ingrediente>>(){}.getType();
            ingredientes = new Gson().fromJson(json, listType);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ingredientes;
    }

    private void getUnidadesMedidaToStringArray(List<UnidadeMedida> unidadesMedida){
        unidadesMedidaArray = new ArrayList<>();
        for (UnidadeMedida unidadeMedida : unidadesMedida) {
            unidadesMedidaArray.add(unidadeMedida.getSigla());
        }
    }

    private void getIngredientesToStringArray(List<Ingrediente> ingredientes){
        ingredienteArray = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            ingredienteArray.add(ingrediente.getNome());
        }
    }

    private AdapterView.OnItemClickListener getIngrediente(){
       AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               for (Ingrediente ingrediente: ingredientes) {
                   if(ingrediente.getNome().equals(parent.getItemAtPosition(position))){
                       edtCodigoBarras.setText(Utils.DOUBLE_TO_STRING(ingrediente.getCodigoBarras(), Constants.QTDE_DIGITOS_CODIGO_MARRAS));
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
    }

    public void initCodBarras(){
        new IntentIntegrator(this).initiateScan();
    }

    private View.OnClickListener addIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingrediente = new Ingrediente();
                if(validarCampos()){
                    getIntent().putExtra("ingrediente", ingrediente);
                    setResult(RESULT_FIRST_USER, getIntent());
                    finish();
                }
            }
        };
        return onClickListener;
    }

    private View.OnFocusChangeListener serFocus(){
        View.OnFocusChangeListener focus = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus && edtCodigoBarras.getText().length() > 0){
                if(verificarCodigoBarras()){
                    validarIngrediente(getIngredienteByCodigoBarras());
                }else{
                    exibirMensagem("O código precisa ter " + Constants.QTDE_DIGITOS_CODIGO_MARRAS + " dígitos.");
                }
            }
            }
        };

        return focus;
    }

    private void validarIngrediente(Ingrediente ingrediente){
        aucNome.setText(ingrediente.getNome());
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
            return false;
        }else{
            ingrediente.setCodigoBarras(Double.parseDouble(edtCodigoBarras.getText().toString()));
        }

        if(aucNome.getText() == null || aucNome.getText().toString().equals("")){
            return false;
        }else{
            for (Ingrediente ingrediente: ingredientes) {
                if(ingrediente.getNome().equals(aucNome.getText().toString())){
                    this.ingrediente.setNome(ingrediente.getNome());
                    break;
                }
            }
        }

        if(edtQuantidade.getText() == null || edtQuantidade.getText().toString().equals("")){
            return false;
        }else{
            ingrediente.setQuantidade(Float.parseFloat(edtQuantidade.getText().toString()));
        }

        if(aucUnidadeMedida.getText() == null || aucUnidadeMedida.getText().toString().equals("")){
            return false;
        }else{
            for (UnidadeMedida unidadeMedida: unidadesMedida) {
                if(unidadeMedida.getSigla().equals(aucUnidadeMedida.getText().toString())){
                    ingrediente.setUnidadeMedida(unidadeMedida);
                    break;
                }
            }
        }

        return true;
    }
}
