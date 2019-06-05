package com.tcc.maispratos.activity.ingrediente;

import java.io.Serializable;

public class UnidadeMedida implements Serializable {
    private int id;
    private String sigla;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
