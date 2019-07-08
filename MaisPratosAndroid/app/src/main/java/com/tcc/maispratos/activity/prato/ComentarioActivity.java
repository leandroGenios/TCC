package com.tcc.maispratos.activity.prato;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.prato.Prato;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ComentarioActivity extends BaseMenuActivity {
    private Prato prato;
    private EditText mltComentario;
    private Button btnSalvar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);
        setTitle("Cadastrar comentário");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        prato = (Prato) getIntent().getExtras().getSerializable("prato");

        iniciaElementos();
    }

    public void iniciaElementos(){
        mltComentario = findViewById(R.id.mltComentario);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(salvar());
    }

    private View.OnClickListener salvar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskConnection connection = new TaskConnection();
                Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
                params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
                params[Constants.NOME_DO_RESOURCE] = "prato/comentario";

                prato.setComentario(mltComentario.getText().toString());
                getUsuario().setPrato(prato);
                String gson = new Gson().toJson(getUsuario());
                try {
                    params[Constants.OBJETO] = new JSONObject(gson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    connection.execute(params);

                    getIntent().putExtra("comentario", mltComentario.getText().toString());
                    setResult(RESULT_FIRST_USER, getIntent());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    exibirErro("Ocorreu um problema ao salvar o comentário. Tente novamente mais tarde.");
                }
            }
        };
        return onClickListener;
    }
}
