package com.tcc.maispratos.ingrediente;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LineIngredientePratoHolder extends RecyclerView.ViewHolder {
    public TextView txtNome;
    public TextView txtQuantidade;
    public TextView txtTipo;

    public LineIngredientePratoHolder(@NonNull View itemView) {
        super(itemView);
        txtNome = (TextView) itemView.findViewById(R.id.txtIngredientePrato);
        txtQuantidade = (TextView) itemView.findViewById(R.id.txtQuantidade);
        txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
    }

}
