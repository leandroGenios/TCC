package com.tcc.maispratos.activity.prato;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class PratoActivity extends BaseMenuActivity {

    private TextView txtNomePrato;
    private TextView txtNotaPrato;
    private TextView txtNomeCriador;
    private TextView txtNivelCriador;
    private ImageView imgAvaliacao1;
    private ImageView imgAvaliacao2;
    private ImageView imgAvaliacao3;
    private ImageView imgAvaliacao4;
    private ImageView imgAvaliacao5;
    private RecyclerView rcvIngredientes;
    private TextView txtModoPreparo;
    private TextView txtTempoPreparo;
    private RecyclerView rcvComentarios;
    private Button btnDeixarComentario;
    private FloatingActionButton fab;

    private Prato prato;


    private ComentarioAdapter comentarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prato);
        setTitle("Prato");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));
        prato = (Prato) getIntent().getExtras().getSerializable("prato");

        iniciaElementos();

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
        imgAvaliacao1 = (ImageView) findViewById(R.id.imgAvaliacao1);
        imgAvaliacao2 = (ImageView) findViewById(R.id.imgAvaliacao2);
        imgAvaliacao3 = (ImageView) findViewById(R.id.imgAvaliacao3);
        imgAvaliacao4 = (ImageView) findViewById(R.id.imgAvaliacao4);
        imgAvaliacao5 = (ImageView) findViewById(R.id.imgAvaliacao5);
        rcvIngredientes = (RecyclerView) findViewById(R.id.rcvIngredientes);
        txtModoPreparo = (TextView) findViewById(R.id.txtModoPreparo);
        txtTempoPreparo = (TextView) findViewById(R.id.txtTempoPreparo);
        rcvComentarios = (RecyclerView) findViewById(R.id.rcvComentarios);
        btnDeixarComentario = (Button) findViewById(R.id.btnDeixarComentario);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        txtNomePrato.setText(prato.getNome());
        txtNomeCriador.setText(prato.getCriador().getNome());
        txtModoPreparo.setText(prato.getModoPreparo());
    }
}
