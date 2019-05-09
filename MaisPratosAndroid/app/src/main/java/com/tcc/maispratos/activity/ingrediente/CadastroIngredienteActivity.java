package com.tcc.maispratos.activity.ingrediente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tcc.maispratos.R;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.MenusAction;

public class CadastroIngredienteActivity extends AppCompatActivity {

    String MesesVetor[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio",
            "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    EditText edtCodigoBarras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ingrediente);
        setTitle("Cadastro de ingrediente");

        AutoCompleteTextView acObjText = (AutoCompleteTextView) findViewById(R.id.aucUnidadeMedida);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MesesVetor);
        acObjText.setAdapter(adapter);

        Button insereCodBarras = findViewById(R.id.btnCodBarras);
        insereCodBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCodBarras();
            }
        });

        edtCodigoBarras = findViewById(R.id.edtCodBarras);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MenusAction.onActionMenu(item.getItemId()));
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText(this, "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }*/

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        String codigo = result.getContents();
        edtCodigoBarras.setText(codigo);

        /*if(result.getContents() == null) {
            Log.d("MainActivity", "Cancelled scan");
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MainActivity", "Scanned");
            Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        }*/
    }

    public void initCodBarras(){
        new IntentIntegrator(this).initiateScan();
    }

    private String verificaPaisFabricacao(String codigo){
        int codPais = Integer.parseInt(codigo.substring(0,3));
        String codigos;
        int codInit;
        int codFim;
        for (String pais: Constants.PAISES.split(",")) {
            codigos = pais.split(":")[0];
            codInit = Integer.parseInt(codigos.split("-")[0]);
            codFim = Integer.parseInt(codigos.split("-")[1]);
            if(codPais >= codInit && codPais <= codFim){
                return pais.split(":")[1];
            }
        }
        return "PAÍS NÃO ENCONTRADO";
    }

    private String buscaFabricante(String codigo){
        return "Quero";
    }
}
