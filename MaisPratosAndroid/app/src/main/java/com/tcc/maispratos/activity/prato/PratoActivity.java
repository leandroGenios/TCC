package com.tcc.maispratos.activity.prato;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.UpdateIngredienteActivity;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.comentario.Comentario;
import com.tcc.maispratos.comentario.ComentarioAdapter;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.ingrediente.prato.IngredientePrato;
import com.tcc.maispratos.modopreparo.ModoPreparo;
import com.tcc.maispratos.modopreparo.ModoPreparoAdapter;
import com.tcc.maispratos.prato.IngredientePratoAdapter;
import com.tcc.maispratos.prato.IngredientePratoDetalheAdapter;
import com.tcc.maispratos.prato.Prato;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.INVISIBLE;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class PratoActivity extends BaseMenuActivity {

    private TextView txtNomePrato;
    private TextView txtNotaPrato;
    private TextView txtNomeCriador;
    private TextView txtNivelCriador;
    private RecyclerView rcvIngredientes;
    private TextView txtModoPreparo;
    private TextView txtTempoPreparo;
    private RecyclerView rcvComentarios;
    private Button btnDeixarComentario;
    private FloatingActionButton fab;
    private List<ImageView> estrelasAvaliacao;

    private Prato prato;
    private Drawable estrelaSelecionada;
    private Drawable estrela;
    private IngredientePratoDetalheAdapter adapterIngredientes;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private long minutos;
    private long segundos;
    private Drawable iconFab;
    private CountDownTimer countTime;
    private List<Ingrediente> meusIngredientes;
    private List<Ingrediente> ingredientesFaltantes;


    private ComentarioAdapter comentarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prato);
        setTitle("Prato");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        prato = (Prato) getIntent().getExtras().getSerializable("prato");

        iniciaElementos();
        getEstrelas();
        setMinhaAvaliacaoPrato();
        montaListaIngredientes(prato.getIngredientes());
        montaListaIngredientes(prato.getId());
        verificarPreparo();
    }

    public void iniciaElementos(){
        txtNotaPrato = findViewById(R.id.txtNotaPrato);
        txtNomePrato = findViewById(R.id.txtNomePrato);
        txtNomeCriador = findViewById(R.id.txtNomeCriador);
        txtNivelCriador = findViewById(R.id.txtNivelCriador);
        estrelasAvaliacao = new ArrayList<>();
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao1));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao2));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao3));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao4));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao5));
        rcvIngredientes = findViewById(R.id.rcvIngredientes);
        txtModoPreparo = findViewById(R.id.txtModoPreparo);
        txtTempoPreparo = findViewById(R.id.txtTempoPreparo);
        rcvComentarios = findViewById(R.id.rcvComentarios);
        btnDeixarComentario = findViewById(R.id.btnDeixarComentario);
        fab = findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.actPrato);

        txtNomePrato.setText(prato.getNome());
        txtNomeCriador.setText("Criado por " + prato.getCriador().getNome());
        txtNivelCriador.setText("Cozinheiro nível " + prato.getCriador().getClassificacao().getDescricao());
        txtModoPreparo.setText(prato.getModoPreparo());
        txtNotaPrato.setText(String.valueOf(prato.getNota()));
        txtTempoPreparo.setText(prato.getTempoPreparo() + " minutos");

        for (int i = 0; i < estrelasAvaliacao.size(); i++) {
            estrelasAvaliacao.get(i).setOnClickListener(setAvaliacao(i));
        }

        rcvIngredientes.setLayoutManager(new LinearLayoutManager(this));
        adapterIngredientes = new IngredientePratoDetalheAdapter(new ArrayList<Ingrediente>(0), this);
        rcvIngredientes.setAdapter(adapterIngredientes);
        rcvIngredientes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rcvComentarios.setLayoutManager(new LinearLayoutManager(this));
        comentarioAdapter = new ComentarioAdapter(new ArrayList<Comentario>(0), this);
        rcvComentarios.setAdapter(comentarioAdapter);
        rcvComentarios.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        fab.setOnClickListener(fabAction());
        snackbar = criarTimer();

        btnDeixarComentario.setOnClickListener(addComentario());

        meusIngredientes = listMeusIngredientes();
        ingredientesFaltantes = new ArrayList<>();
    }

    private void getEstrelas() {
        estrelaSelecionada = estrelasAvaliacao.get(0).getDrawable();
        estrela = estrelasAvaliacao.get(4).getDrawable();
    }

    private void setMinhaAvaliacaoPrato(){
        for (ImageView imagem: estrelasAvaliacao) {
            imagem.setImageDrawable(estrela);
        }
        for(int i = 0 ; i < prato.getAvaliacao(); i++){
            estrelasAvaliacao.get(i).setImageDrawable(estrelaSelecionada);
        }
    }

    private View.OnClickListener setAvaliacao(final int i){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempoPreparo = prato.getTempoPreparo() * 60000;
                long tempoFinal = prato.getUltimoPreparo() + tempoPreparo;
                if(prato.getUltimoPreparo() != 0 && tempoFinal < Calendar.getInstance().getTime().getTime()){
                    for (int j = 0; j < estrelasAvaliacao.size(); j++) {
                        estrelasAvaliacao.get(j).setImageDrawable(estrela);
                    }

                    for(int j = 0; j <= i  ; j++){
                        estrelasAvaliacao.get(j).setImageDrawable(estrelaSelecionada);
                    }

                    salvarAvaliacao(i + 1);
                }else{
                    exibirMensagem("Você precisa preparar o prato para poder avaliar.");
                }
            }
        };
        return onClickListener;
    }

    private void salvarAvaliacao(final int avaliacao){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "prato/avaliacao";

        prato.setAvaliacao(avaliacao);
        getUsuario().setPrato(prato);
        String gson = new Gson().toJson(getUsuario());
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            getUsuario().setPrato(null);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao avaliar. Tente novamente mais tarde.");
        }
    }

    private void montaListaIngredientes(List<Ingrediente> ingredientes){
        for (Ingrediente ingrediente: ingredientes) {
            adapterIngredientes.updateList(ingrediente);
        }
    }

    private Snackbar criarTimer(){
        return Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_INDEFINITE);
    }

    private View.OnClickListener fabAction(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(snackbar.isShown()){
                    if(prato.isPreparadoSemIngredientes()){
                        exibirMensagem("O preparo desse prato não pode ser interrompido.");
                    }else{
                        iconFab = getResources().getDrawable(android.R.drawable.ic_media_play);
                        snackbar.dismiss();
                        getTimer(prato.getTempoPreparo() * 60000).cancel();
                        cancelarPreparo();
                    }
                }else{
                    if(verificaIngredientesCompativeis()){
                        prato.setPreparadoSemIngredientes(false);
                        salvarInicioPreparo();
                        descontarIngredientes();
                        iconFab = getResources().getDrawable(android.R.drawable.ic_media_pause);
                        getTimer(prato.getTempoPreparo() * 60000).start();
                        snackbar.show();
                    }else{
                        exibirIngredientesFaltantes();
                        iconFab = getResources().getDrawable(android.R.drawable.ic_media_play);
                    }
                }
                fab.setImageDrawable(iconFab);
            }
        };
        return onClickListener;
    }

    private boolean verificaIngredientesCompativeis(){
        Ingrediente ingrediente;
        for (Ingrediente ingredientePrato: prato.getIngredientes()) {
            ingrediente = null;
            for (Ingrediente meuIngrediente: meusIngredientes) {
                if(ingredientePrato.getNome().toLowerCase().equals(meuIngrediente.getNome().toLowerCase())){
                    ingrediente = meuIngrediente;
                }
            }

            if(ingrediente == null){
                ingredientesFaltantes.add(ingredientePrato);
            }else{
                if(ingrediente.getQuantidade() < ingredientePrato.getQuantidade()){
                    ingrediente.setQuantidade(ingredientePrato.getQuantidade() - ingrediente.getQuantidade());
                    ingredientesFaltantes.add(ingrediente);
                }
            }
        }

        return ingredientesFaltantes.size() == 0 ? true : false;

    }

    private CountDownTimer getTimer(long tempo){
        if(countTime == null){
            countTime = new CountDownTimer(tempo, 1000) {
                public void onTick(long millisUntilFinished) {
                    minutos = (millisUntilFinished / 60000);
                    segundos = ((millisUntilFinished % 60000) / 1000);
                    snackbar.setText("Tempo restante: " + (minutos < 10 ? "0" : "") + minutos + ":" + (segundos < 10 ? "0" : "") + segundos);
                }
                public void onFinish() {
                    snackbar.setText("Concluído!!!");
                }
            };
        }
        return countTime;
    }

    private void salvarInicioPreparo(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "prato/setInicioPreparo";

        prato.setHoraPreparo(Calendar.getInstance().getTime().getTime());
        getUsuario().setPrato(prato);
        String gson = new Gson().toJson(getUsuario());
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            getUsuario().setPrato(null);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao avaliar. Tente novamente mais tarde.");
        }
    }

    private Long verificarPreparo(){
        if(prato.getUltimoPreparo() != 0){
            int tempoPreparo = prato.getTempoPreparo() * 60000;
            long tempoFinal = prato.getUltimoPreparo() + tempoPreparo;
            if(tempoFinal > Calendar.getInstance().getTime().getTime()){
                iconFab = getResources().getDrawable(android.R.drawable.ic_media_pause);
                fab.setImageDrawable(iconFab);
                getTimer(tempoFinal - Calendar.getInstance().getTime().getTime()).start();
                snackbar.show();
                return tempoFinal;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    private View.OnClickListener addComentario(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempoPreparo = prato.getTempoPreparo() * 60000;
                long tempoFinal = prato.getUltimoPreparo() + tempoPreparo;
                if(prato.getUltimoPreparo() != 0 && tempoFinal < Calendar.getInstance().getTime().getTime()){
                    Intent intent = new Intent(getApplicationContext(), ComentarioActivity.class);
                    intent.putExtra("usuario", getUsuario());
                    intent.putExtra("prato", prato);
                    startActivityForResult(intent, 1);
                }else{
                    exibirMensagem("Você precisa preparar o prato para poder avaliar.");
                }
            }
        };
        return onClickListener;
    }

    private void montaListaIngredientes(int idPrato){
        List<Comentario> list = null;
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "prato/comentario/" + idPrato;
        String json = null;
        connection.execute(params);
        try {
            json = (String) connection.get();
            Type listType = new TypeToken<ArrayList<Comentario>>(){}.getType();
            list = new Gson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        comentarioAdapter.clear();
        if(list != null){
            for (Comentario comentario: list) {
                comentarioAdapter.updateList(comentario);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE:
                    break;
                case 1:
                    if (resultCode == RESULT_FIRST_USER) {
                        Comentario comentario = new Comentario();
                        comentario.setTexto((String) data.getExtras().getSerializable("comentario"));
                        comentario.setUsuario(getUsuario());
                        comentarioAdapter.updateList(comentario);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("Erro", "Exception in onActivityResult : " + e.getMessage());
        }

        if (resultCode == RESULT_OK && data != null) {
        }
    }

    private List<Ingrediente> listMeusIngredientes(){
        List<Ingrediente> list = null;
        TaskConnection connection = new TaskConnection();
        String[] params = new String[Constants.QUERY_SEM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente/" + getUsuario().getId();

        String json = null;
        connection.execute(params);
        try {
            json = (String) connection.get();
            Type listType = new TypeToken<ArrayList<Ingrediente>>(){}.getType();
            list = new Gson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void exibirIngredientesFaltantes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(montarMensagem());
        builder.setPositiveButton("Preparar mesmo assim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                prato.setPreparadoSemIngredientes(true);
                descontarIngredientes();
                salvarInicioPreparo();
                iconFab = getResources().getDrawable(android.R.drawable.ic_media_pause);
                getTimer(prato.getTempoPreparo() * 60000).start();
                snackbar.show();
            }
        });
        builder.setNegativeButton("Enviar para lista de compras", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private String montarMensagem(){
        String mensagem = "Faltam ingredientes para preparar esse prato\n";
        for (Ingrediente ingrediente: ingredientesFaltantes) {
            mensagem += ingrediente.getNome() + " " + ingrediente.getQuantidade() + ingrediente.getUnidadeMedida().getSigla() + "\n";
        }
        return mensagem;
    }

    private void descontarIngredientes(){
        for (Ingrediente ingredientePrato: prato.getIngredientes()) {
            for (Ingrediente meuIngrediente: meusIngredientes) {
                if(ingredientePrato.getNome().toLowerCase().equals(meuIngrediente.getNome().toLowerCase())){
                    meuIngrediente.setQuantidade(meuIngrediente.getQuantidade() - ingredientePrato.getQuantidade());
                }
            }
        }

        for (Ingrediente ingrediente: meusIngredientes) {
            if(ingrediente.getQuantidade() >= 0){
                alterarIngrediente(ingrediente);
            }else{
                deletarIngrediente(ingrediente);
            }
        }
    }

    private void alterarIngrediente(Ingrediente ingrediente){
        getUsuario().setIngrediente(ingrediente);

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.PUT;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente";
        String gson = new Gson().toJson(getUsuario());
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            ((String) connection.get()).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao atualizar. Tente novamente mais tarde.");
        }
    }

    private void deletarIngrediente(Ingrediente ingrediente){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_SEM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.DELETE;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente/" + getUsuario().getId() + "/" + ingrediente.getId();
        connection.execute(params);

        try {
            ((String) connection.get()).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelarPreparo(){
        Ingrediente ingrediente;
        for (Ingrediente ingredientePrato: prato.getIngredientes()) {
            ingrediente = null;
            for (Ingrediente meuIngrediente: meusIngredientes) {
                if(ingredientePrato.getNome().toLowerCase().equals(meuIngrediente.getNome().toLowerCase())){
                    ingrediente = meuIngrediente;
                }
            }

            if(ingrediente != null){
                ingrediente.setQuantidade(ingrediente.getQuantidade() + ingredientePrato.getQuantidade());
                alterarIngrediente(ingrediente);
            }else{
                addIngrediente(ingrediente);
            }
        }

        removerPreparo();
    }

    private void addIngrediente(Ingrediente ingrediente){
        getUsuario().setIngrediente(ingrediente);

        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "ingrediente";
        String gson = new Gson().toJson(getUsuario());
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            ((String) connection.get()).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao cadastrar. Tente novamente mais tarde.");
        }
    }

    private void removerPreparo(){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_SEM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.DELETE;
        params[Constants.NOME_DO_RESOURCE] = "prato/preparo/" + getUsuario().getId() + "/" + prato.getId() + "/" + prato.getUltimoPreparo();
        connection.execute(params);

        try {
            ((String) connection.get()).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
