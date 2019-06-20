package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.ingrediente.IngredienteAdapter;
import com.tcc.maispratos.prato.IngredientePratoAdapter;
import com.tcc.maispratos.util.BaseMenuActivity;

import java.util.ArrayList;

public class CadastroPratoActivity extends BaseMenuActivity {
    private EditText edtNome;
    private Button btnAddIngrediente;
    private RecyclerView rcvIngrediente;
    private EditText mltModoPreparo;
    private EditText edtTempoPreparo;
    private IngredientePratoAdapter adapter;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_prato);
        setTitle("Cadastro de prato");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
    }

    private void iniciaElementos(){
        edtNome = (EditText) findViewById(R.id.edtNomePrato);
        btnAddIngrediente = (Button) findViewById(R.id.btnAddIngredientePrato);
        rcvIngrediente = (RecyclerView) findViewById(R.id.rcvIngredientesPrato);
        mltModoPreparo = (EditText) findViewById(R.id.edtQuantidade);
        edtTempoPreparo = (AutoCompleteTextView) findViewById(R.id.aucUnidadeMedida);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        rcvIngrediente.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientePratoAdapter(new ArrayList<Ingrediente>(0), this);
        rcvIngrediente.setAdapter(adapter);
        rcvIngrediente.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        btnAddIngrediente.setOnClickListener(addIngrediente());
        btnSalvar.setOnClickListener(salvar());
    }

    private View.OnClickListener addIngrediente(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        return onClickListener;
    }

    private View.OnClickListener salvar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroIngredientePratoActivity.class);
                intent.putExtra("usuario", getUsuario());
                startActivityForResult(intent, 1);
            }
        };
        return onClickListener;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            System.out.println(data.getExtras().getSerializable("ingrediente"));
            addIngrediente((Ingrediente) data.getExtras().getSerializable("ingrediente"));
        }
    }

    private void addIngrediente(Ingrediente ingrediente){
        adapter.updateList(ingrediente);
    }
}
