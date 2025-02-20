package models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import enums.Operacao;

public class ListarUsuarioResponse extends ModelBase {

	private List<UsuarioResponse> usuarios;

	public ListarUsuarioResponse() {
		super(Operacao.listarUsuarios);
		this.usuarios = new ArrayList<>();
	}
	
	public void adicionarUsuario(UsuarioResponse usuario) {
		this.usuarios.add(usuario);
	}
	
	@Override
	public JSONObject toJson() {
	    JSONObject json = new JSONObject();

	    json.put("operacao", this.getOperacao().toString());
	    json.put("status", this.getStatus());
	    json.put("mensagem", this.getMensagem());
	    
	    if(this.getStatus() != 401) {
		    JSONArray usuariosArray = new JSONArray();
		    
		    for (UsuarioResponse usuario : this.usuarios) {
		    	usuariosArray.put(usuario.toJson());
	        }
		    
		    json.put("usuarios", usuariosArray);
	    }

	    return json;
	}
}
