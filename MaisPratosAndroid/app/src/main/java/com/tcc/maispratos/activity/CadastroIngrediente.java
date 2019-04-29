package com.tcc.maispratos.activity;

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
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tcc.maispratos.R;

public class CadastroIngrediente extends AppCompatActivity {

    String MesesVetor[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio",
            "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    TextView txtCodBarras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ingrediente);
        setTitle("Cadastro de ingrediente");

        AutoCompleteTextView acObjText = (AutoCompleteTextView) findViewById(R.id.acTexto);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MesesVetor);
        acObjText.setAdapter(adapter);

        Button insereCodBarras = findViewById(R.id.btnCodBarras);
        insereCodBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCodBarras();
            }
        });

        txtCodBarras = findViewById(R.id.txtCodBarras);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ingredientes) {
            Intent intent = new Intent(this, CadastroIngrediente.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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
        txtCodBarras.setText(result.getContents());

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
}
