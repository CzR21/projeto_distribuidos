package models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import enums.Operacao;

public class ListarCategoriaResponse extends ModelBase {

	private List<CategoriaResponse> categorias;

	public ListarCategoriaResponse() {
		super(Operacao.listarCategorias);
		this.categorias = new ArrayList<>();
	}
	
	public void adicionarCategoria(CategoriaResponse categoria) {
		this.categorias.add(categoria);
	}
	
	@Override
	public JSONObject toJson() {
	    JSONObject json = new JSONObject();

	    json.put("operacao", this.getOperacao().toString());
	    json.put("status", this.getStatus());
	    json.put("mensagem", this.getMensagem());
	    
	    if(this.getStatus() != 401) {
		    JSONArray categoriasArray = new JSONArray();
		    
		    for (CategoriaResponse categoria : this.categorias) {
		    	categoriasArray.put(categoria.toJson());
	        }
		    
		    json.put("categorias", categoriasArray);
	    }

	    return json;
	}
	
	public JSONObject toJson2() {
	    JSONObject json = new JSONObject();

	    json.put("operacao", this.getOperacao().toString());
	    json.put("status", this.getStatus());
	    json.put("mensagem", this.getMensagem());
	    
	    if(this.getStatus() != 401) {
		    JSONArray categoriasArray = new JSONArray();
		    
		    for (CategoriaResponse categoria : this.categorias) {
		    	categoriasArray.put(categoria.getId());
	        }
		    
		    json.put("categorias", categoriasArray);
	    }

	    return json;
	}
}
