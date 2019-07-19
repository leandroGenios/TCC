package com.tcc.maispratos.activity.perfil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.prato.CadastroPratoActivity;
import com.tcc.maispratos.activity.prato.PratosActivity;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.util.BaseMenuActivity;

public class PerfilActivity extends BaseMenuActivity {

    private TextView txtNivelCozinheiro;
    private ProgressBar progressBar;
    private TextView txtProximoNivel;
    private TextView txtQtdePratosCadastrados;
    private TextView txtQtdePratosAvaliados;
    private TextView txtNomeUsuario;
    private TextView txtEmailUsuario;
    private Button btnEditarDados;
    private Button btnMeusPratos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle("Perfil");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
    }

    public void iniciaElementos(){
        txtNivelCozinheiro = findViewById(R.id.txtNivelCozinheiro);
        progressBar = findViewById(R.id.progressBar);
        txtProximoNivel = findViewById(R.id.txtProximoNivel);
        txtQtdePratosCadastrados = findViewById(R.id.txtQtdePratosCadastrados);
        txtQtdePratosAvaliados = findViewById(R.id.txtQtdePratosAvaliados);
        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);
        btnEditarDados = findViewById(R.id.btnEditarDados);
        btnMeusPratos = findViewById(R.id.btnMeusPratos);

        btnMeusPratos.setOnClickListener(exibirMeusPratos());
    }

    public View.OnClickListener exibirMeusPratos(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PratosActivity.class);
                intent.putExtra("usuario", getUsuario());
                intent.putExtra("aba", "MEUS");
                startActivity(intent);
            }
        };
        return onClickListener;
    }
}
