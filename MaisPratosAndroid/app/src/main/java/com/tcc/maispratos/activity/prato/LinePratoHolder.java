package com.tcc.maispratos.activity.prato;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LinePratoHolder extends RecyclerView.ViewHolder {

    public ImageView imgPrato;
    public TextView txtNomePrato;

    public LinePratoHolder(@NonNull View itemView) {
        super(itemView);
        imgPrato = (ImageView) itemView.findViewById(R.id.imgPrato);
        txtNomePrato = (TextView) itemView.findViewById(R.id.txtNomePrato);
    }
}
