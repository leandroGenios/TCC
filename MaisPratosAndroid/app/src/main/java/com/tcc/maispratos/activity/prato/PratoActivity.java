package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.comentario.Comentario;
import com.tcc.maispratos.comentario.ComentarioAdapter;
import com.tcc.maispratos.ingrediente.prato.IngredientePrato;
import com.tcc.maispratos.ingrediente.prato.IngredientePratoAdapter;
import com.tcc.maispratos.modopreparo.ModoPreparo;
import com.tcc.maispratos.modopreparo.ModoPreparoAdapter;
import com.tcc.maispratos.prato.Prato;
import com.tcc.maispratos.util.BaseMenuActivity;

import java.util.ArrayList;
import java.util.List;

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
        comentarioAdapter.updateList(comentario1);



        RecyclerView rcvIngrediente = (RecyclerView) findViewById(R.id.rcvIngredientes);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        rcvIngrediente.setLayoutManager(layoutManager1);
        IngredientePratoAdapter ingredientePratoAdapter = new IngredientePratoAdapter(new ArrayList<IngredientePrato>(0), this);
        rcvIngrediente.setAdapter(ingredientePratoAdapter);
        rcvIngrediente.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        IngredientePrato ingredientePrato = new IngredientePrato();
        ingredientePrato.setNome("Arroz");
        ingredientePrato.setQuantidade(1);
        ingredientePrato.setTipo("Kg");

        ingredientePratoAdapter.updateList(ingredientePrato);
        ingredientePratoAdapter.updateList(ingredientePrato);
        ingredientePratoAdapter.updateList(ingredientePrato);
        ingredientePratoAdapter.updateList(ingredientePrato);
        ingredientePratoAdapter.updateList(ingredientePrato);
        ingredientePratoAdapter.updateList(ingredientePrato);
        ingredientePratoAdapter.updateList(ingredientePrato);


        RecyclerView rcvModoPreparo = (RecyclerView) findViewById(R.id.rcvModoPreparo);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        rcvModoPreparo.setLayoutManager(layoutManager2);
        ModoPreparoAdapter modoPreparoAdapter = new ModoPreparoAdapter(new ArrayList<ModoPreparo>(0), this);
        rcvModoPreparo.setAdapter(modoPreparoAdapter);
        rcvModoPreparo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ModoPreparo modoPreparo = new ModoPreparo();
        modoPreparo.setOrdem(1);
        modoPreparo.setDescricao("Descasque as bataras");

        modoPreparoAdapter.updateList(modoPreparo);
        modoPreparoAdapter.updateList(modoPreparo);
        modoPreparoAdapter.updateList(modoPreparo);
        modoPreparoAdapter.updateList(modoPreparo);
        modoPreparoAdapter.updateList(modoPreparo);
        modoPreparoAdapter.updateList(modoPreparo);
        modoPreparoAdapter.updateList(modoPreparo);
        modoPreparoAdapter.updateList(modoPreparo);*/
    }

    public void iniciaElementos(){
        txtNotaPrato = (TextView) findViewById(R.id.txtNotaPrato);
        txtNomePrato = (TextView) findViewById(R.id.txtNomePrato);
        txtNomeCriador = (TextView) findViewById(R.id.txtNomeCriador);
        txtNivelCriador = (TextView) findViewById(R.id.txtNivelCriador);
        estrelasAvaliacao = new ArrayList<>();
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao1));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao2));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao3));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao4));
        estrelasAvaliacao.add((ImageView) findViewById(R.id.imgAvaliacao5));
        rcvIngredientes = (RecyclerView) findViewById(R.id.rcvIngredientes);
        txtModoPreparo = (TextView) findViewById(R.id.txtModoPreparo);
        txtTempoPreparo = (TextView) findViewById(R.id.txtTempoPreparo);
        rcvComentarios = (RecyclerView) findViewById(R.id.rcvComentarios);
        btnDeixarComentario = (Button) findViewById(R.id.btnDeixarComentario);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        txtNomePrato.setText(prato.getNome());
        txtNomeCriador.setText("Criado por " + prato.getCriador().getNome());
        txtNivelCriador.setText("Cozinheiro nível " + prato.getCriador().getClassificacao().getDescricao());
        txtModoPreparo.setText(prato.getModoPreparo());

        for (int i = 0; i < estrelasAvaliacao.size(); i++) {
            estrelasAvaliacao.get(i).setOnClickListener(setAvaliacao(i));
        }

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
            }
        };
        return onClickListener;
    }
}
