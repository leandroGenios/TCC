package com.tcc.maispratos.activity.ingrediente;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LineIngredienteHolder extends RecyclerView.ViewHolder{
    public TextView txtQtdeIngrediente;
    public TextView txtNomeIngrediente;

    public LineIngredienteHolder(@NonNull View itemView) {
        super(itemView);
        txtQtdeIngrediente = (TextView) itemView.findViewById(R.id.txtNomeIngrediente);
        txtNomeIngrediente = (TextView) itemView.findViewById(R.id.txtNomeIngrediente);
    }
}
