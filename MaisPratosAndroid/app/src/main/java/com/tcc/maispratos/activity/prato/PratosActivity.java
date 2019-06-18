package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.util.BaseMenuActivity;

import java.util.ArrayList;

public class PratosActivity extends BaseMenuActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView rcvPratos;
    private PratoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratos);
        setTitle("Lista de pratos");
        setUsuario((Usuario) getIntent().getExtras().getSerializable("usuario"));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        rcvPratos = (RecyclerView) findViewById(R.id.rcvPratos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvPratos.setLayoutManager(layoutManager);
        adapter = new PratoAdapter(new ArrayList<Prato>(0), this);
        rcvPratos.setAdapter(adapter);
        rcvPratos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        iniciaElementos();
        listGeral();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    listGeral();
                    return true;
                case R.id.navigation_dashboard:
                    listGeral();
                    return true;
                case R.id.navigation_notifications:
                    listGeral();
                    return true;
            }
            return false;
        }
    };

    private void iniciaElementos(){
        fab = (FloatingActionButton) findViewById(R.id.flbAddPrato);
        fab.setOnClickListener(addPrato());
    }

    private View.OnClickListener addPrato(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), PratoActivity.class);
                intent.putExtra("usuario", getUsuario());
                startActivity(intent);
                finish();
            }
        };
        return onClickListener;
    }

    private void listGeral(){
        adapter.clear();

        Prato p1 = new Prato();
        p1.setNome("LASANHA AO MOLHO BOLONHESA");
        adapter.updateList(p1);

        Prato p2 = new Prato();
        p2.setNome("Prato 2");
        adapter.updateList(p2);
        adapter.updateList(p2);
        adapter.updateList(p2);
        adapter.updateList(p2);
        adapter.updateList(p2);
        adapter.updateList(p2);
        adapter.updateList(p2);
        adapter.updateList(p2);
    }
}
