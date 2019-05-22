package com.tcc.maispratos.activity.usuario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.tcc.maispratos.R;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtConfirmaEmail;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_usuario);

        iniciaElementos();
    }

    private void iniciaElementos(){
        edtNome = (EditText) findViewById(R.id.edtCadastroNome);
        edtEmail = (EditText) findViewById(R.id.edtCadastroEmail);
        edtConfirmaEmail = (EditText) findViewById(R.id.edtCadastroConfirmaEmail);
        edtSenha = (EditText) findViewById(R.id.edtCadastroSenha);
        edtConfirmaSenha = (EditText) findViewById(R.id.edtCadastroConfirmaSenha);
    }
}
