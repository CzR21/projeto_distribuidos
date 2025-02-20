package models;

import org.json.JSONObject;

import enums.Operacao;

public class BuscarAvisoResponse extends ModelBase {

	private AvisoResponse aviso;

	public BuscarAvisoResponse() {
		super(Operacao.localizarAviso);
	}
	
	public AvisoResponse getCategoria() {
		return aviso;
	}
	
	public void setAviso(AvisoResponse aviso) {
		this.aviso = aviso;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("operacao", this.getOperacao().toString());
		json.put("status", this.getStatus());
		json.put("mensagem", this.getMensagem());
		
		if(this.getStatus() != 401) {
			JSONObject aviso = this.aviso.toJson();
			
			json.put("aviso", aviso);
		}
		
		return json;
	}
}
