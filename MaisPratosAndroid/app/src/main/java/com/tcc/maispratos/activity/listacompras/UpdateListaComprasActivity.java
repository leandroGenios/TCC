package com.tcc.maispratos.activity.listacompras;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.unidademedida.UnidadeMedida;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;
import com.tcc.maispratos.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UpdateListaComprasActivity extends BaseMenuActivity {

    private Ingrediente ingrediente;
    private Button btnCodBarras;
    private EditText edtCodBarras;
    private EditText edtNome;
    private EditText edtQuantidade;
    private AutoCompleteTextView aucUnidadeMedida;
    private Button btnSalvar;

    private List<UnidadeMedida> unidadesMedida;
    private ArrayList<String> unidadesMedidaArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lista_compras);
        setTitle("Editar item");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        ingrediente = (Ingrediente) getIntent().getExtras().getSerializable("ingrediente");

        iniciaElementos();
        iniciaAutoComplete();
        carregarCampos();
    }

    private void iniciaElementos(){
        btnCodBarras = (Button) findViewById(R.id.btnUpdateListaComprasCodBarras);
        edtCodBarras = (EditText) findViewById(R.id.edtUpdateUpdateListaCodBarras);
        edtNome = (EditText) findViewById(R.id.edtUpdateUpdateListaNome);
        edtQuantidade = (EditText) findViewById(R.id.edtUpdateUpdateListaQuantidade);
        aucUnidadeMedida = (AutoCompleteTextView) findViewById(R.id.aucUpdateUpdateListaUnidadeMedida);
        btnSalvar = (Button) findViewById(R.id.btnUpdateUpdateListaSalvar);

        btnCodBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCodBarras();
            }
        });
        btnSalvar.setOnClickListener(updateIngrediente());
        edtCodBarras.setOnFocusChangeListener(serFocus());
    }

    private void iniciaAutoComplete(){
        getStringArray(getUnidadesMedida());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, unidadesMedidaArray);
        aucUnidadeMedida.setAdapter(adapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        String codigo = result.getContents();
        edtCodBarras.setText(codigo);
    }

    public void initCodBarras(){
        new IntentIntegrator(this).initiateScan();
    }

    private View.OnClickListener updateIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    if(atualizarIngrediente()){
                        Intent intent = new Intent(getApplicationContext(), ListaComprasActivity.class);
                        intent.putExtra("usuario", getUsuario());
                        startActivity(intent);
                        finish();
                    }else{
                        exibirErro("Não foi possível salvar o item. Tente novamente mais tarde.");
                    }
                }
            }
        };
        return onClickListener;
    }

    private boolean validarCampos(){
        if(!validarPreenchimento()){
            exibirMensagem("Todos os campos devem ser preenchidos.");
            return false;
        }
        return true;
    }

    private boolean validarPreenchimento(){
        if(edtCodBarras.getText() == null || edtCodBarras.getText().toString().equals("")){
            edtCodBarras.requestFocus();
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

        if(aucUnidadeMedida.getText() == null || aucUnidadeMedida.getText().toString().equals("")){
            aucUnidadeMedida.requestFocus();
            return false;
        }

        return true;
    }

    private boolean atualizarIngrediente(){
        ingrediente.setCodigoBarras(Double.parseDouble(edtCodBarras.getText().toString()));
        ingrediente.setQuantidade(Float.parseFloat(edtQuantidade.getText().toString()));
        ingrediente.setNome(edtNome.getText().toString());
        for (UnidadeMedida unidadeMedida: unidadesMedida) {
            if(unidadeMedida.getSigla().equals(aucUnidadeMedida.getText().toString())){
                ingrediente.setUnidadeMedida(unidadeMedida);
                break;
            }
        }

        getUsuario().setIngrediente(ingrediente);

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.PUT;
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
            exibirErro("Ocorreu um problema ao atualizar. Tente novamente mais tarde.");
        }

        return false;
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
                        edtCodBarras.requestFocus();
                    }
                }
            }
        };

        return focus;
    }

    private boolean verificarCodigoBarras(){
        return edtCodBarras.getText().toString().length() == Constants.QTDE_DIGITOS_CODIGO_MARRAS;
    }

    private void validarIngrediente(Ingrediente ingrediente){
        if(ingrediente != null){
            edtNome.setText(ingrediente.getNome());
            edtNome.setEnabled(false);
        }else{
            edtNome.setEnabled(true);
        }
    }

    private Ingrediente getIngredienteByCodigoBarras(){
        Ingrediente ingrediente = null;
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente/getByCodigoBarras/" + edtCodBarras.getText().toString();
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

    private void carregarCampos(){
        edtCodBarras.setText(Utils.DOUBLE_TO_STRING(ingrediente.getCodigoBarras(), Constants.QTDE_DIGITOS_CODIGO_MARRAS));
        edtNome.setText(ingrediente.getNome());
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
