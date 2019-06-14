package com.tcc.maispratos.ingrediente.prato;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.prato.PratoActivity;

import java.util.List;

public class IngredientePratoAdapter extends RecyclerView.Adapter<LineIngredientePratoHolder> {
    private final List<IngredientePrato> ingredientesPrato;
    private PratoActivity activity;

    public IngredientePratoAdapter(List<IngredientePrato> ingredientesPrato, PratoActivity activity) {

        this.ingredientesPrato = ingredientesPrato;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LineIngredientePratoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineIngredientePratoHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_line_ingrediente_prato, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineIngredientePratoHolder lineIngredientePratoHolder, int i) {
        lineIngredientePratoHolder.txtNome.setText(ingredientesPrato.get(i).getNome());
        lineIngredientePratoHolder.txtQuantidade.setText(String.valueOf(ingredientesPrato.get(i).getQuantidade()));
        lineIngredientePratoHolder.txtTipo.setText(ingredientesPrato.get(i).getTipo());
        lineIngredientePratoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return ingredientesPrato != null ? ingredientesPrato.size() : 0;
    }

    public void updateList(IngredientePrato ingredientePrato) {
        insertItem(ingredientePrato);
    }

    private void insertItem(IngredientePrato ingredientePrato) {
        ingredientesPrato.add(ingredientePrato);
        notifyItemInserted(getItemCount());
    }

    public void clear(){
        ingredientesPrato.clear();
        notifyDataSetChanged();
    }
}
