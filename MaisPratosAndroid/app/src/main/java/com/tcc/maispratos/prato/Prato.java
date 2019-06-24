package com.tcc.maispratos.prato;

import com.tcc.maispratos.ingrediente.Ingrediente;

import java.io.Serializable;
import java.util.List;

public class Prato implements Serializable {
    private int id;
    private String nome;
    private List<Ingrediente> ingredientes;
    private String modoPreparo;
    private int ingredientesCompativeis;
    private byte[] imagem;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }
    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
    public String getModoPreparo() {
        return modoPreparo;
    }
    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }
    public int getTempoPreparo() {
        return tempoPreparo;
    }
    public void setTempoPreparo(int tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }
    private int tempoPreparo;

    public int getIngredientesCompativeis() {
        return ingredientesCompativeis;
    }

    public void setIngredientesCompativeis(int ingredientesCompativeis) {
        this.ingredientesCompativeis = ingredientesCompativeis;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
}
