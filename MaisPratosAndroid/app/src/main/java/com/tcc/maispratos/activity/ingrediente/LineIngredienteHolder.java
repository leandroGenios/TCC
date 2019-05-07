package com.tcc.maispratos.activity.ingrediente;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.maispratos.R;

public class LineIngredienteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private static ClickListener clickListener;
    public TextView txtQtdeIngrediente;
    public TextView txtNomeIngrediente;

    public LineIngredienteHolder(@NonNull View itemView) {
        super(itemView);
        txtQtdeIngrediente = (TextView) itemView.findViewById(R.id.txtNomeIngrediente);
        txtNomeIngrediente = (TextView) itemView.findViewById(R.id.txtNomeIngrediente);
    }

    @Override
    public void onClick(View view) {
        clickListener.onItemClick(getAdapterPosition(), view);
    }

    @Override
    public boolean onLongClick(View view) {
        clickListener.onItemLongClick(getAdapterPosition(), view);
        return false;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        LineIngredienteHolder.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
