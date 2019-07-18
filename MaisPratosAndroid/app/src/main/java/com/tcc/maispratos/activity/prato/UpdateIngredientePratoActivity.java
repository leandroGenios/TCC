package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.BaseIngrediente;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.unidademedida.UnidadeMedida;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.Utils;

public class UpdateIngredientePratoActivity extends BaseIngrediente {
    private Ingrediente ingrediente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ingrediente_prato);
        setTitle("Alterar ingrediente");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        ingrediente = (Ingrediente) getIntent().getExtras().getSerializable("ingrediente");
        iniciaElementos();
        iniciaAutoCompleteUnidadeMedida();
        iniciaAutoCompleteIngrediente();
        carregarCampos(ingrediente);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        String codigo = result.getContents();
        edtCodigoBarras.setText(codigo);
    }

    @Override
    public View.OnClickListener salvarIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
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
                    getIntent().putExtra("ingrediente", ingrediente);
                    setResult(RESULT_FIRST_USER, getIntent());
                    finish();
                }
            }
        };
        return onClickListener;
    }
}
