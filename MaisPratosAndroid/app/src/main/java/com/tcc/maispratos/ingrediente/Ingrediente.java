package com.tcc.maispratos.ingrediente;

import com.tcc.maispratos.unidademedida.UnidadeMedida;

import java.io.Serializable;

public class Ingrediente implements Serializable {
    private int id;
    private float quantidade;
    private double codigoBarras;
    private String nome;
    private UnidadeMedida unidadeMedida;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public double getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(double codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}
