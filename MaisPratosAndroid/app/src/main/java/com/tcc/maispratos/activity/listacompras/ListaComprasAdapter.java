package com.tcc.maispratos.activity.listacompras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.CadastroIngredienteActivity;
import com.tcc.maispratos.activity.ingrediente.Ingrediente;
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
import com.tcc.maispratos.activity.ingrediente.LineIngredienteHolder;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListaComprasAdapter extends RecyclerView.Adapter<LineIngredienteHolder> {
    private final List<Ingrediente> ingredientes;
    private ListaComprasActivity activity;

    public ListaComprasAdapter(List<Ingrediente> ingredientes, ListaComprasActivity activity) {
        this.activity = activity;
        this.ingredientes = ingredientes;
    }

    @NonNull
    @Override
    public LineIngredienteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineIngredienteHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_line_ingrediente, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineIngredienteHolder lineIngredienteHolder, final int i) {
        lineIngredienteHolder.txtNomeIngrediente.setText(ingredientes.get(i).getNome());
        lineIngredienteHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibeDialogo(ingredientes.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientes != null ? ingredientes.size() : 0;
    }

    public void updateList(Ingrediente ingrediente) {
        insertItem(ingrediente);
    }

    private void insertItem(Ingrediente ingrediente) {
        ingredientes.add(ingrediente);
        notifyItemInserted(getItemCount());
    }

    public void clear(){
        ingredientes.clear();
        notifyDataSetChanged();
    }


    private void exibeDialogo(final Ingrediente ingrediente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Atenção");
        builder.setMessage("Qual a ação desejada?");
        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                exibeDialogoDelete(ingrediente);
            }
        });
        builder.setNegativeButton("Alterar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(activity.getApplicationContext(), CadastroIngredienteActivity.class);
                intent.putExtra("ingrediente.id", ingrediente.getId());
                activity.startActivity(intent);
                activity.finish();
            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void exibeDialogoDelete(final Ingrediente ingrediente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Atenção");
        builder.setMessage("Qual o motivo da exclusão");
        builder.setPositiveButton("Comprado", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.setNegativeButton("Remover da lista", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void exibeMensagem(String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Atenção");
        builder.setMessage(mensagem);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
