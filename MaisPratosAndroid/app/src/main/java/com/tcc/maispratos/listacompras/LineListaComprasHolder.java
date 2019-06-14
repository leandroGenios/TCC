package com.tcc.maispratos.listacompras;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LineListaComprasHolder extends RecyclerView.ViewHolder{
    public TextView txtQtdeIngredienteListaCompras;
    public TextView txtNomeIngredienteListaCompras;

    public LineListaComprasHolder(@NonNull View itemView) {
        super(itemView);
        txtQtdeIngredienteListaCompras = (TextView) itemView.findViewById(R.id.txtQtdeIngredienteListaCompras);
        txtNomeIngredienteListaCompras = (TextView) itemView.findViewById(R.id.txtNomeIngredienteListaCompras);
    }
}
