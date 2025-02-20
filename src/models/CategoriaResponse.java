package models;

import org.json.JSONObject;

public class CategoriaResponse {
	
	private int id;
	private String nome;
	
	public CategoriaResponse(int id, String nome) {
		this.id = id;
		this.nome = nome;
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
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("id", this.id);
		json.put("nome", this.nome);
		
		return json;
	}
}
