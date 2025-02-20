package models;

import org.json.JSONObject;

public class UsuarioResponse {
	
	private String nome;
	private String ra;
	private String senha;
	
	public UsuarioResponse(String nome, String ra, String senha) {
		this.nome = nome;
		this.ra = ra;
		this.senha = senha;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getRa() {
		return ra;
	}
	
	public void setRa(String ra) {
		this.ra = ra;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}	
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("ra", this.ra);
		json.put("nome", this.nome);
		json.put("senha", this.senha);
		
		return json;
	}
}
