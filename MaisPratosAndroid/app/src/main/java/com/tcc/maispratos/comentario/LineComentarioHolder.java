package com.tcc.maispratos.comentario;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LineComentarioHolder extends RecyclerView.ViewHolder {
    public TextView txtUsuario;
    public TextView txtComentario;

    public LineComentarioHolder(@NonNull View itemView) {
        super(itemView);
        txtUsuario = (TextView) itemView.findViewById(R.id.txtUsuario);
        txtComentario = (TextView) itemView.findViewById(R.id.txtComentario);
    }

}
