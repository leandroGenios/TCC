package com.tcc.maispratos.prato;

import java.io.Serializable;

public class Classificacao implements Serializable {
	private int id;
	private String descricao;
	private int menorValor;
	private int maiorValor;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getMenorValor() {
		return menorValor;
	}
	public void setMenorValor(int menorValor) {
		this.menorValor = menorValor;
	}
	public int getMaiorValor() {
		return maiorValor;
	}
	public void setMaiorValor(int maiorValor) {
		this.maiorValor = maiorValor;
	}
}
