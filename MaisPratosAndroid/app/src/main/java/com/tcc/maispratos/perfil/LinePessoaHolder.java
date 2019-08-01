package com.tcc.maispratos.perfil;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcc.maispratos.BuildConfig;
import com.tcc.maispratos.R;

public class LinePessoaHolder extends RecyclerView.ViewHolder{
    public TextView txtNomePessoa;
    public TextView txtClassificacaoPessoa;
    public TextView txtQtdePratosAdd;
    public Button btnAdd;

    public LinePessoaHolder(@NonNull View itemView) {
        super(itemView);
        txtNomePessoa = itemView.findViewById(R.id.txtNomePessoa);
        txtClassificacaoPessoa = itemView.findViewById(R.id.txtClassificacaoPessoa);
        txtQtdePratosAdd = itemView.findViewById(R.id.txtQtdePratosAdd);
        btnAdd = itemView.findViewById(R.id.btnAdd);
    }
}
