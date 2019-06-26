package com.tcc.maispratos.ingrediente;

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
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
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

public class BaseIngrediente extends BaseMenuActivity {
    public AutoCompleteTextView aucUnidadeMedida;
    public ArrayAdapter<String> adapterUnidadeMedida;
    public AutoCompleteTextView aucNomeIngrediente;
    public ArrayAdapter<String> adapterIngrediente;
    public EditText edtCodigoBarras;
    public EditText edtQuantidade;
    public Button btnInserirCodBarras;
    public Button btnAdicionar;
    public List<UnidadeMedida> unidadesMedida;
    public ArrayList<String> unidadesMedidaArray;
    public List<Ingrediente> ingredientes;
    public ArrayList<String> ingredienteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void iniciaElementos(){}

    public View.OnClickListener getCodigoBarras(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCapturaCodigoBarras();
            }
        };
        return onClickListener;
    }

    public void iniciarCapturaCodigoBarras(){
        new IntentIntegrator(this).initiateScan();
    }

    public View.OnFocusChangeListener focusOutCodigoBarras(){
        View.OnFocusChangeListener focus = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus && edtCodigoBarras.getText().toString().length() > 0){
                    if(verificarCodigoBarras()){
                        verificarIngrediente(getIngredienteByCodigoBarras());
                    }else{
                        exibirMensagem("O código precisa ter " + Constants.QTDE_DIGITOS_CODIGO_MARRAS + " dígitos.");
                        aucNomeIngrediente.setText("");
                    }
                }
            }
        };

        return focus;
    }

    private boolean verificarCodigoBarras(){
        return edtCodigoBarras.getText().toString().length() == Constants.QTDE_DIGITOS_CODIGO_MARRAS;
    }

    private void verificarIngrediente(Ingrediente ingrediente){
        aucNomeIngrediente.setText(ingrediente != null ? ingrediente.getNome() : "");
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

    public View.OnClickListener salvarIngrediente(){
        return null;
    }

    public void iniciaAutoCompleteUnidadeMedida(){
        getStringArray(getUnidadesMedida());
        adapterUnidadeMedida = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, unidadesMedidaArray);
        aucUnidadeMedida.setAdapter(adapterUnidadeMedida);
    }

    private void getStringArray(List<UnidadeMedida> unidadesMedida){
        unidadesMedidaArray = new ArrayList<>();
        for (UnidadeMedida unidadeMedida : unidadesMedida) {
            unidadesMedidaArray.add(unidadeMedida.getSigla());
        }
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

    public void iniciaAutoCompleteIngrediente(){
        getIngredientesToStringArray(getIngredientes());
        adapterIngrediente = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredienteArray);
        aucNomeIngrediente.setAdapter(adapterIngrediente);
        aucNomeIngrediente.setOnFocusChangeListener(focusOutNomeIngrediente());
    }

    private void getIngredientesToStringArray(List<Ingrediente> ingredientes){
        ingredienteArray = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            ingredienteArray.add(ingrediente.getNome());
        }
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

    private View.OnFocusChangeListener focusOutNomeIngrediente(){
        View.OnFocusChangeListener focus = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus && aucNomeIngrediente.getText().toString().length() > 0){
                    for (Ingrediente ingred: ingredientes) {
                        if(ingred.getNome().equals(aucNomeIngrediente.getText().toString())){
                            if(ingred.getCodigoBarras() > 0)
                                edtCodigoBarras.setText(String.valueOf(Utils.DOUBLE_TO_STRING(ingred.getCodigoBarras(), Constants.QTDE_DIGITOS_CODIGO_MARRAS)));
                            break;
                        }
                    }
                }
            }
        };

        return focus;
    }

    public boolean validarCampos(){
        if(!validarPreenchimento()){
            exibirMensagem("Todos os campos devem ser preenchidos.");
            return false;
        }
        return true;
    }

    private boolean validarPreenchimento(){
        if(aucNomeIngrediente.getText() == null || aucNomeIngrediente.getText().toString().equals("")){
            return false;
        }

        if(edtQuantidade.getText() == null || edtQuantidade.getText().toString().equals("")){
            return false;
        }

        if(aucUnidadeMedida.getText() == null || aucUnidadeMedida.getText().toString().equals("")){
            return false;
        }

        return true;
    }

    public void carregarCampos(Ingrediente ingrediente){
        if(ingrediente.getCodigoBarras() > 0){
            edtCodigoBarras.setText(Utils.DOUBLE_TO_STRING(ingrediente.getCodigoBarras(), Constants.QTDE_DIGITOS_CODIGO_MARRAS));
        }

        aucNomeIngrediente.setText(ingrediente.getNome());
        String quantidade = String.valueOf(ingrediente.getQuantidade());
        edtQuantidade.setText(formatarQuantidadeQuebrada(quantidade));

        for(int i = 0; i < unidadesMedida.size(); i++){
            if(unidadesMedida.get(i).getId() == ingrediente.getUnidadeMedida().getId()){
                aucUnidadeMedida.setText(aucUnidadeMedida.getAdapter().getItem(i).toString(), false);
            }
        }
    }

    private String formatarQuantidadeQuebrada(String quantidade){
        return quantidade.split("\\.")[1].equals("0") ? quantidade.split("\\.")[0] : quantidade;
    }
}
