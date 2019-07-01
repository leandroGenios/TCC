package com.tcc.maispratos.prato;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LinePratoHolder extends RecyclerView.ViewHolder {
    public ImageView imgPrato;
    public TextView txtNomePrato;
    public TextView txtIngredientesCompativeis;
    public TextView txtNota;

    public LinePratoHolder(@NonNull View itemView) {
        super(itemView);
        imgPrato = (ImageView) itemView.findViewById(R.id.imgPrato);
        txtNomePrato = (TextView) itemView.findViewById(R.id.txtNomePrato);
        txtIngredientesCompativeis = (TextView) itemView.findViewById(R.id.txtIngredientesCompativeis);
        txtNota = (TextView) itemView.findViewById(R.id.txtNota);
    }

}
