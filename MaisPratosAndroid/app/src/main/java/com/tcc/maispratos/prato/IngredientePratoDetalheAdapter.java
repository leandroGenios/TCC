package com.tcc.maispratos.prato;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.UpdateIngredienteActivity;
import com.tcc.maispratos.activity.prato.CadastroPratoActivity;
import com.tcc.maispratos.activity.prato.PratoActivity;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.ingrediente.LineIngredienteHolder;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.util.List;

public class IngredientePratoDetalheAdapter extends RecyclerView.Adapter<LineIngredienteHolder> {
    private final List<Ingrediente> ingredientes;
    private PratoActivity activity;

    public IngredientePratoDetalheAdapter(List<Ingrediente> ingredientes, PratoActivity activity) {
        this.activity = activity;
        this.ingredientes = ingredientes;
    }

    @NonNull
    @Override
    public LineIngredienteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineIngredienteHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_line_ingrediente, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineIngredienteHolder lineIngredienteHolder, final int i) {
        lineIngredienteHolder.txtNomeIngrediente.setText(ingredientes.get(i).getNome());
        lineIngredienteHolder.txtQtdeIngrediente.setText(String.valueOf(ingredientes.get(i).getQuantidade() + " " + ingredientes.get(i).getUnidadeMedida().getSigla()));
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

    public List<Ingrediente> getIngredientes(){
        return ingredientes;
    }
}
