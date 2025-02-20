package models;

import org.json.JSONObject;

public class AvisoResponse {

	private int id;
	private String titulo;
	private String descricao;
	private CategoriaResponse categoria;
	
	public AvisoResponse(int id, String titulo, String descricao, CategoriaResponse categoria) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.categoria = categoria;
	}	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public CategoriaResponse getCategoria() {
		return categoria;
	}
	
	public void setCategoria(CategoriaResponse categoria) {
		this.categoria = categoria;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("id", this.id);
		json.put("titulo", this.titulo);
		json.put("descricao", this.descricao);
		
		JSONObject categoria = this.categoria.toJson();
		
		json.put("categoria", categoria);
		
		return json;
	}
}
