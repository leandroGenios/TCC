package com.tcc.maispratos.prato;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.prato.PratoActivity;
import com.tcc.maispratos.activity.prato.PratosActivity;

import java.util.List;

public class PratoAdapter extends RecyclerView.Adapter<LinePratoHolder> {
    private final List<Prato> pratos;
    private PratosActivity activity;

    public PratoAdapter(List<Prato> pratos, PratosActivity activity) {

        this.pratos = pratos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LinePratoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinePratoHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_line_prato, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinePratoHolder linePratoHolder, final int i) {
        linePratoHolder.txtNomePrato.setText(pratos.get(i).getNome());
        linePratoHolder.txtIngredientesCompativeis.setText(pratos.get(i).getIngredientesCompativeis() + " ingredientes compatíveis");

        byte[] bitMapData = Base64.decode(pratos.get(i).getImagemBase64(),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
        linePratoHolder.imgPrato.setImageBitmap(bitmap);

        linePratoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), PratoActivity.class);
                intent.putExtra("prato.id", pratos.get(i).getId());
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pratos != null ? pratos.size() : 0;
    }

    public void updateList(Prato prato) {
        insertItem(prato);
    }

    private void insertItem(Prato prato) {
        pratos.add(prato);
        notifyItemInserted(getItemCount());
    }

    public void clear(){
        pratos.clear();
        notifyDataSetChanged();
    }
}
