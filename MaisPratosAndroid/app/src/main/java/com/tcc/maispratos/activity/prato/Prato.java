package com.tcc.maispratos.activity.prato;

public class Prato {
    private int id;
    private String nome;
    private String imagem;
    private String notaGeral;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNotaGeral() {
        return notaGeral;
    }

    public void setNotaGeral(String notaGeral) {
        this.notaGeral = notaGeral;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
