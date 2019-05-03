package com.tcc.maispratos.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.model.Prato;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PratoAdapter extends RecyclerView.Adapter<LinePratoHolder> {
    private final List<Prato> pratos;

    public PratoAdapter(List<Prato> pratos) {
        this.pratos = pratos;
    }

    @NonNull
    @Override
    public LinePratoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinePratoHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_line, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinePratoHolder linePratoHolder, int i) {
        linePratoHolder.txtNomePrato.setText(pratos.get(i).getNome());
    }

    @Override
    public int getItemCount() {
        return pratos != null ? pratos.size() : 0;
    }

    public void updateList(Prato prato) {
        insertItem(prato);
    }

    private void insertItem(Prato prato) {
        pratos.add(prato);
        notifyItemInserted(getItemCount());
    }
}
