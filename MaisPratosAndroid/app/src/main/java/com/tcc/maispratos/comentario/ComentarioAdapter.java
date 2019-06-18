package com.tcc.maispratos.comentario;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.prato.PratoActivity;

import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<LineComentarioHolder> {
    private final List<Comentario> comentarios;
    private PratoActivity activity;

    public ComentarioAdapter(List<Comentario> comentarios, PratoActivity activity) {

        this.comentarios = comentarios;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LineComentarioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineComentarioHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_line_comentario, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineComentarioHolder lineComentarioHolder, int i) {
        lineComentarioHolder.txtUsuario.setText(comentarios.get(i).getUsuario().getNome());
        lineComentarioHolder.txtComentario.setText(comentarios.get(i).getTexto());
        lineComentarioHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return comentarios != null ? comentarios.size() : 0;
    }

    public void updateList(Comentario comentario) {
        insertItem(comentario);
    }

    private void insertItem(Comentario comentario) {
        comentarios.add(comentario);
        notifyItemInserted(getItemCount());
    }

    public void clear(){
        comentarios.clear();
        notifyDataSetChanged();
    }
}
