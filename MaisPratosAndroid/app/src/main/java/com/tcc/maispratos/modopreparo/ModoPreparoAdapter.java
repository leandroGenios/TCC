package com.tcc.maispratos.modopreparo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.prato.PratoActivity;

import java.util.List;

public class ModoPreparoAdapter extends RecyclerView.Adapter<LineModoPreparoHolder> {
    private final List<ModoPreparo> modosPreparo;
    private PratoActivity activity;

    public ModoPreparoAdapter(List<ModoPreparo> modosPreparo, PratoActivity activity) {

        this.modosPreparo = modosPreparo;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LineModoPreparoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineModoPreparoHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_line_modo_preparo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineModoPreparoHolder lineModoPreparoHolder, int i) {
        lineModoPreparoHolder.txtOrdem.setText(String.valueOf(modosPreparo.get(i).getOrdem()));
        lineModoPreparoHolder.txtDescricao.setText(String.valueOf(modosPreparo.get(i).getDescricao()));
        lineModoPreparoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return modosPreparo != null ? modosPreparo.size() : 0;
    }

    public void updateList(ModoPreparo modoPreparo) {
        insertItem(modoPreparo);
    }

    private void insertItem(ModoPreparo modoPreparo) {
        modosPreparo.add(modoPreparo);
        notifyItemInserted(getItemCount());
    }

    public void clear(){
        modosPreparo.clear();
        notifyDataSetChanged();
    }
}
