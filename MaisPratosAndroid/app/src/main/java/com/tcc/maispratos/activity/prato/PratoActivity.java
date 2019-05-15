package com.tcc.maispratos.activity.prato;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.comentario.Comentario;
import com.tcc.maispratos.comentario.ComentarioAdapter;
import com.tcc.maispratos.ingrediente.IngredientePrato;
import com.tcc.maispratos.ingrediente.IngredientePratoAdapter;
import com.tcc.maispratos.modopreparo.ModoPreparo;
import com.tcc.maispratos.modopreparo.ModoPreparoAdapter;

import java.util.ArrayList;

public class PratoActivity extends AppCompatActivity {

    private ComentarioAdapter comentarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prato);

        RecyclerView rcvComentario = (RecyclerView) findViewById(R.id.rcvComentarios);
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
        modoPreparoAdapter.updateList(modoPreparo);
    }
}
