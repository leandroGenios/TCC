package com.tcc.maispratos.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.tcc.maispratos.R;
import com.tcc.maispratos.adapter.PratoAdapter;
import com.tcc.maispratos.model.Prato;

import java.util.ArrayList;
import java.util.List;

public class PratosActivity extends AppCompatActivity {

    private RecyclerView rcvPratos;
    private PratoAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Prato p1 = new Prato();
                    p1.setNome("Prato 1");
                    adapter.updateList(p1);

                    Prato p2 = new Prato();
                    p2.setNome("Prato 2");
                    adapter.updateList(p2);

                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
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
        adapter = new PratoAdapter(new ArrayList<Prato>(0));
        rcvPratos.setAdapter(adapter);
        rcvPratos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

}
