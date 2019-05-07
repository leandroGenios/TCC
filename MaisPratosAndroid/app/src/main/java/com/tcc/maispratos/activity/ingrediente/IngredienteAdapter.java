package com.tcc.maispratos.activity.ingrediente;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.prato.LinePratoHolder;
import com.tcc.maispratos.activity.prato.Prato;

import java.util.List;

public class IngredienteAdapter extends RecyclerView.Adapter<LineIngredienteHolder> {
    private final List<Ingrediente> ingredientes;

    public IngredienteAdapter(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    @NonNull
    @Override
    public LineIngredienteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineIngredienteHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_line_ingrediente, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineIngredienteHolder lineIngredienteHolder, int i) {
        lineIngredienteHolder.txtNomeIngrediente.setText(ingredientes.get(i).getNome());
        lineIngredienteHolder.setOnItemClickListener(new LineIngredienteHolder.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                System.out.println(position);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                System.out.println(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientes != null ? ingredientes.size() : 0;
    }

    public void updateList(Ingrediente ingrediente) {
        insertItem(ingrediente);
    }

    private void insertItem(Ingrediente ingrediente) {
        ingredientes.add(ingrediente);
        notifyItemInserted(getItemCount());
    }

    public void clear(){
        ingredientes.clear();
        notifyDataSetChanged();
    }
}
