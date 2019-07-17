package com.tcc.maispratos.prato;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.UpdateIngredienteActivity;
import com.tcc.maispratos.activity.prato.CadastroPratoActivity;
import com.tcc.maispratos.activity.prato.UpdateIngredientePratoActivity;
import com.tcc.maispratos.activity.prato.UpdatePratoActivity;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.ingrediente.LineIngredienteHolder;
import com.tcc.maispratos.util.BaseMenuActivity;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import java.util.List;

public class IngredientePratoAdapter extends RecyclerView.Adapter<LineIngredienteHolder> {
    private final List<Ingrediente> ingredientes;
    private CadastroPratoActivity activityCadastro;
    private UpdatePratoActivity activityUpdate;

    public IngredientePratoAdapter(List<Ingrediente> ingredientes, CadastroPratoActivity activity) {
        this.activityCadastro = activity;
        this.ingredientes = ingredientes;
    }

    public IngredientePratoAdapter(List<Ingrediente> ingredientes, UpdatePratoActivity activity) {
        this.activityUpdate = activity;
        this.ingredientes = ingredientes;
    }

    private BaseMenuActivity getActivity(){
        if(activityCadastro != null){
            return activityCadastro;
        }else{
            return activityUpdate;
        }
    }

    @NonNull
    @Override
    public LineIngredienteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineIngredienteHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_line_ingrediente, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineIngredienteHolder lineIngredienteHolder, final int i) {
        lineIngredienteHolder.txtNomeIngrediente.setText(ingredientes.get(i).getNome());
        lineIngredienteHolder.txtQtdeIngrediente.setText(String.valueOf(ingredientes.get(i).getQuantidade() + " " + ingredientes.get(i).getUnidadeMedida().getSigla()));
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
        System.out.println(ingrediente.getNome());
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }

    public List<Ingrediente> getIngredientes(){
        return ingredientes;
    }

    public void clear(){
        ingredientes.clear();
        notifyDataSetChanged();
    }


    private void exibeDialogo(final Ingrediente ingrediente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Atenção");
        builder.setMessage("Qual a ação desejada?");
        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                exibeDialogoDelete(ingrediente);
            }
        });
        builder.setNegativeButton("Alterar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateIngredientePratoActivity.class);
                intent.putExtra("ingrediente", ingrediente);
                intent.putExtra("usuario", getActivity().getUsuario());
                getActivity().startActivityForResult(intent, 1);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Atenção");
        builder.setMessage("Deseja realmente excluir o ingrediente " + ingrediente.getNome().toUpperCase() + "?");
        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ingredientes.remove(ingrediente);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void exibeMensagem(String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
