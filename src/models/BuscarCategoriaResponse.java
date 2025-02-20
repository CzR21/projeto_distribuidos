package models;

import org.json.JSONObject;

import enums.Operacao;

public class BuscarCategoriaResponse extends ModelBase {

	private CategoriaResponse categoria;

	public BuscarCategoriaResponse() {
		super(Operacao.localizarCategoria);
	}
	
	public CategoriaResponse getCategoria() {
		return categoria;
	}
	
	public void setCategoria(CategoriaResponse categoria) {
		this.categoria = categoria;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("operacao", this.getOperacao().toString());
		json.put("status", this.getStatus());
		json.put("mensagem", this.getMensagem());
		
		if(this.getStatus() != 401) {
			JSONObject categoria = this.categoria.toJson();
		
			json.put("categoria", categoria);
		}
		
		return json;
	}
}
