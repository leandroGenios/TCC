package com.tcc.maispratos.activity.usuario;

import com.tcc.maispratos.ingrediente.Ingrediente;
import com.tcc.maispratos.prato.Classificacao;
import com.tcc.maispratos.prato.Prato;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
    private int id;
    private String senha;
    private String email;
    private String nome;
    private Ingrediente ingrediente;
    private List<Ingrediente> ingredientes;
    private Prato prato;
    private List<Prato> pratos;
    private Classificacao classificacao;
    private List<Usuario> amigos;
    private boolean amigo;

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Prato getPrato() {
        return prato;
    }
    public void setPrato(Prato prato) {
        this.prato = prato;
    }
    public List<Prato> getPratos() {
        return pratos;
    }
    public void setPratos(List<Prato> pratos) {
        this.pratos = pratos;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }

    public List<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Usuario> amigos) {
        this.amigos = amigos;
    }

    public boolean isAmigo() {
        return amigo;
    }

    public void setAmigo(boolean amigo) {
        this.amigo = amigo;
    }
}

