package com.tcc.maispratos.perfil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
import com.tcc.maispratos.activity.ingrediente.UpdateIngredienteActivity;
import com.tcc.maispratos.activity.perfil.AmigosActivity;
import com.tcc.maispratos.activity.perfil.PerfilPublicoActivity;
import com.tcc.maispratos.activity.prato.PratosActivity;
import com.tcc.maispratos.activity.usuario.Usuario;
import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.ingrediente.LineIngredienteHolder;
import com.tcc.maispratos.prato.Classificacao;
import com.tcc.maispratos.util.Constants;
import com.tcc.maispratos.util.TaskConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class PessoaAdapter extends RecyclerView.Adapter<LinePessoaHolder> {
    private final List<Usuario> pessoas;
    private AmigosActivity activity;

    public PessoaAdapter(List<Usuario> pessoas, AmigosActivity activity) {
        this.activity = activity;
        this.pessoas = pessoas;
    }

    @NonNull
    @Override
    public LinePessoaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinePessoaHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_line_amigo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinePessoaHolder linePessoaHolder, final int i) {
        linePessoaHolder.txtNomePessoa.setText(pessoas.get(i).getNome());
        linePessoaHolder.txtClassificacaoPessoa.setText(getClassificacao(pessoas.get(i)));
        linePessoaHolder.txtQtdePratosAdd.setText(getCountMeusPratos(pessoas.get(i)) + " pratos cadastrados");
        linePessoaHolder.btnAdd.setOnClickListener(adicionarAmigo(pessoas.get(i)));
        if(pessoas.get(i).isAmigo()){
            linePessoaHolder.btnAdd.setVisibility(View.INVISIBLE);
        }
        linePessoaHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), PerfilPublicoActivity.class);
                intent.putExtra("usuario", activity.getUsuario());
                intent.putExtra("amigo", pessoas.get(i));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pessoas != null ? pessoas.size() : 0;
    }

    public void updateList(Usuario pessoa) {
        insertItem(pessoa);
    }

    private void insertItem(Usuario pessoa) {
        pessoas.add(pessoa);
        notifyItemInserted(getItemCount());
    }

    public void clear(){
        pessoas.clear();
        notifyDataSetChanged();
    }

    private View.OnClickListener adicionarAmigo(final Usuario pessoa){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibeDialogo(pessoa);
            }
        };
        return onClickListener;
    }

    private void exibeDialogo(final Usuario pessoa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Atenção");
        builder.setMessage("Deseja adicionar " + pessoa.getNome() + " em sua lista de amigos?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                adicionar(pessoa);
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

    private void adicionar(Usuario pessoa){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.POST;
        params[Constants.NOME_DO_RESOURCE] = "usuario/amigo/" + activity.getUsuario().getId();
        String gson = new Gson().toJson(pessoa);
        try {
            params[Constants.OBJETO] = new JSONObject(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connection.execute(params);

        try {
            if(((String) connection.get()).equals("true")){
                exibeMensagem("Amigo adicionado!");
                this.activity.carregarLista();
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao Adicionar. Tente novamente mais tarde.");
        }
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

    private void exibirErro(String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Erro");
        builder.setMessage(mensagem);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private String getClassificacao(Usuario usuario){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "usuario/classificacao/" + usuario.getId();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<Classificacao>(){}.getType();
            Classificacao c = new Gson().fromJson(json, type);
            return c.getDescricao();
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar buscar os dados. Tente novamente mais tarde.");
        }

        return "";
    }

    private Integer getCountMeusPratos(Usuario usuario){
        TaskConnection connection = new TaskConnection();
        Object[] params = new Object[Constants.QUERY_COM_ENVIO_DE_OBJETO];
        params[Constants.TIPO_DE_REQUISICAO] = Constants.GET;
        params[Constants.NOME_DO_RESOURCE] = "prato/meusPratos/" + usuario.getId();
        connection.execute(params);

        String json = null;
        try {
            json = (String) connection.get();
            Type type = new TypeToken<Integer>(){}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro("Ocorreu um problema ao tentar buscar os dados. Tente novamente mais tarde.");
        }

        return 0;
    }
}
