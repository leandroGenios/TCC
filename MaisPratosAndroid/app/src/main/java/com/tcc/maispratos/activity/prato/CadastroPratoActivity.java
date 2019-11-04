package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.prato.IngredientePratoAdapter;
import com.tcc.maispratos.prato.Prato;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class CadastroPratoActivity extends BaseMenuActivity {
    private EditText edtNome;
    private Button btnAddIngrediente;
    private RecyclerView rcvIngrediente;
    private EditText mltModoPreparo;
    private EditText edtTempoPreparo;
    private IngredientePratoAdapter adapter;
    private Button btnSalvar;
    private ImageButton imgBtnAddImage;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_prato);
        setTitle("Cadastro de prato");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        iniciaElementos();
    }

    private void iniciaElementos(){
        edtNome = findViewById(R.id.edtNomePrato);
        btnAddIngrediente = findViewById(R.id.btnAddIngredientePrato);
        rcvIngrediente = findViewById(R.id.rcvAmigos);
        mltModoPreparo = findViewById(R.id.mltModoPreparoPrato);
        edtTempoPreparo = findViewById(R.id.edtTempoPreparo);
        btnSalvar = findViewById(R.id.btnSalvar);
        imgBtnAddImage = findViewById(R.id.imgbtnAddImagem);

        rcvIngrediente.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientePratoAdapter(new ArrayList<Ingrediente>(0), this);
        rcvIngrediente.setAdapter(adapter);
        rcvIngrediente.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        btnAddIngrediente.setOnClickListener(addIngrediente());
        btnSalvar.setOnClickListener(salvar());

        imgBtnAddImage.setOnClickListener(openGaleria());
    }

    private View.OnClickListener addIngrediente(){
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

    private View.OnClickListener salvar(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    if(cadastrarPrato()){
                        Intent intent = new Intent(getApplicationContext(), PratosActivity.class);
                        intent.putExtra("usuario", getUsuario());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        return onClickListener;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgBtnAddImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Log.e("Cancelado", "Selecting picture cancelled");
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_FIRST_USER) {
                        addIngrediente((Ingrediente) data.getExtras().getSerializable("ingrediente"));
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("Erro", "Exception in onActivityResult : " + e.getMessage());
        }

        if (resultCode == RESULT_OK && data != null) {
        }
    }

    private void addIngrediente(Ingrediente ingrediente){
        adapter.updateList(ingrediente);
    }

    private boolean validarCampos(){
        if(!validarPreenchimento()){
            exibirMensagem("Todos os campos devem ser preenchidos.");
            return false;
        }
        return true;
    }

    private boolean validarPreenchimento(){
        if(edtNome.getText() == null || edtNome.getText().toString().equals("")){
            edtNome.requestFocus();
            return false;
        }

        if(adapter.getIngredientes() == null || adapter.getIngredientes().size() < 1){
            return false;
        }

        if(mltModoPreparo.getText() == null || mltModoPreparo.getText().toString().equals("")){
            mltModoPreparo.requestFocus();
            return false;
        }

        if(edtTempoPreparo.getText() == null || edtTempoPreparo.getText().toString().equals("")){
            edtTempoPreparo.requestFocus();
            return false;
        }
        return true;
    }

    private boolean cadastrarPrato(){
        Prato prato = new Prato();
        prato.setNome(edtNome.getText().toString());
        prato.setIngredientes(adapter.getIngredientes());
        prato.setModoPreparo(mltModoPreparo.getText().toString());
        prato.setTempoPreparo(Integer.parseInt(edtTempoPreparo.getText().toString()));

        if(bitmap != null){
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, blob);
            prato.setImagem(blob.toByteArray());
        }

        getUsuario().setPrato(prato);

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "prato";
        String gson = new Gson().toJson(getUsuario());
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            getUsuario().setPrato(null);
            return ((String) connection.get()).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao cadastrar. Tente novamente mais tarde.");
        }

        return false;
    }

    private View.OnClickListener openGaleria(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);
            }
        };
        return onClickListener;
    }
}
