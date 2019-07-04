package br.com.meuspratos.model;

import java.util.Date;
import java.util.List;

public class Prato {
	private int id;
	private String nome;
	private List<Ingrediente> ingredientes;
	private String modoPreparo;
	private int ingredientesCompativeis = 0;
	private byte[] imagem;
	private String imagemBase64;
	private int tempoPreparo;
	private Usuario criador;
	private int avaliacao;
	private int nota;
    private Date horaPreparo;
    private Date ultimoPreparo;

	public int getAvaliacao() {
		return avaliacao;
	}
	public void setAvaliacao(int avaliacao) {
		this.avaliacao = avaliacao;
	}
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
	public String getImagemBase64() {
		return imagemBase64;
	}
	public void setImagemBase64(String imagemBase64) {
		this.imagemBase64 = imagemBase64;
	}
	public Usuario getCriador() {
		return criador;
	}
	public void setCriador(Usuario criador) {
		this.criador = criador;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	public Date getHoraPreparo() {
		return horaPreparo;
	}
	public void setHoraPreparo(Date horaPreparo) {
		this.horaPreparo = horaPreparo;
	}
	public Date getUltimoPreparo() {
		return ultimoPreparo;
	}
	public void setUltimoPreparo(Date ultimoPreparo) {
		this.ultimoPreparo = ultimoPreparo;
	}
}
