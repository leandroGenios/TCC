package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcc.maispratos.R;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        verificarPreparo();
       /* RecyclerView rcvComentario = (RecyclerView) findViewById(R.id.rcvComentarios);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvComentario.setLayoutManager(layoutManager);
        comentarioAdapter = new ComentarioAdapter(new ArrayList<Comentario>(0), this);
        rcvComentario.setAdapter(comentarioAdapter);
        rcvComentario.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Usuario usuario1 = new Usuario();
        usuario1.setNome("ALESSANDRA");

        Comentario comentario1 = new Comentario();
        comentario1.setTexto("Este prato é muito bom e o tempo de preparo está certinho.");
        comentario1.setUsuario(usuario1);

        comentarioAdapter.updateList(comentario1);
        comentarioAdapter.updateList(comentario1);
        comentarioAdapter.updateList(comentario1);
        comentarioAdapter.updateList(comentario1);
        comentarioAdapter.updateList(comentario1);
        comentarioAdapter.updateList(comentario1);*/
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

        fab.setOnClickListener(fabAction());
        snackbar = criarTimer();
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
                for (int j = 0; j < estrelasAvaliacao.size(); j++) {
                    estrelasAvaliacao.get(j).setImageDrawable(estrela);
                }

                for(int j = 0; j <= i  ; j++){
                    estrelasAvaliacao.get(j).setImageDrawable(estrelaSelecionada);
                }

                salvarAvaliacao(i + 1);
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
                    iconFab = getResources().getDrawable(android.R.drawable.ic_media_play);
                    snackbar.dismiss();
                    getTimer(prato.getTempoPreparo() * 60000).cancel();
                }else{
                    salvarInicioPreparo();
                    iconFab = getResources().getDrawable(android.R.drawable.ic_media_pause);
                    getTimer(prato.getTempoPreparo() * 60000).start();
                    snackbar.show();
                }
                fab.setImageDrawable(iconFab);
            }
        };
        return onClickListener;
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

        prato.setHoraPreparo(Calendar.getInstance().getTime());
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
        if(prato.getUltimoPreparo() != null){
            int tempoPreparo = prato.getTempoPreparo() * 60000;
            long tempoFinal = prato.getUltimoPreparo().getTime() + tempoPreparo;
            if(tempoFinal > Calendar.getInstance().getTime().getTime()){
                iconFab = getResources().getDrawable(android.R.drawable.ic_media_pause);
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
}
