package models;

import org.json.JSONObject;

import enums.Operacao;

public class BuscarUsuarioResponse extends ModelBase {

	private UsuarioResponse usuario;

	public BuscarUsuarioResponse() {
		super(Operacao.localizarUsuario);
	}
	
	public UsuarioResponse getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioResponse usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("operacao", this.getOperacao().toString());
		json.put("status", this.getStatus());
		json.put("mensagem", this.getMensagem());
		
		if(this.getStatus() != 401) {
			JSONObject usuario = this.usuario.toJson();
		
			json.put("usuario", usuario);
		}
		
		return json;
	}
}
