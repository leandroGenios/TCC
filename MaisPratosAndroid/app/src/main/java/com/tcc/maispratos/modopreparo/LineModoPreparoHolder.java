package com.tcc.maispratos.modopreparo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LineModoPreparoHolder extends RecyclerView.ViewHolder {
    public TextView txtOrdem;
    public TextView txtDescricao;

    public LineModoPreparoHolder(@NonNull View itemView) {
        super(itemView);
        txtOrdem = (TextView) itemView.findViewById(R.id.txtOrdem);
        txtDescricao = (TextView) itemView.findViewById(R.id.txtDescricao);
    }

}
