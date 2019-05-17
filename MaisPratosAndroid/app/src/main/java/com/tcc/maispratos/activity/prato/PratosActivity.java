package com.tcc.maispratos.activity.prato;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.MenusAction;

import java.util.ArrayList;

public class PratosActivity extends BaseMenuActivity {

    private RecyclerView rcvPratos;
    private PratoAdapter adapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratos);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        RecyclerView rcvPratos = (RecyclerView) findViewById(R.id.rcvPratos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvPratos.setLayoutManager(layoutManager);
        adapter = new PratoAdapter(new ArrayList<Prato>(0), this);
        rcvPratos.setAdapter(adapter);
        rcvPratos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        listGeral();
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
