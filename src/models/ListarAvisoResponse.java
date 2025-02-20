package models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import enums.Operacao;

public class ListarAvisoResponse extends ModelBase {

	private List<AvisoResponse> avisos;

	public ListarAvisoResponse() {
		super(Operacao.listarAvisos);
		this.avisos = new ArrayList<>();
	}
	
	public ListarAvisoResponse(Operacao operacao) {
		super(operacao);
		this.avisos = new ArrayList<>();
	}
	
	public void adicionarAviso(AvisoResponse aviso) {
		this.avisos.add(aviso);
	}
	
	@Override
	public JSONObject toJson() {
	    JSONObject json = new JSONObject();

	    json.put("operacao", this.getOperacao().toString());
	    json.put("status", this.getStatus());
	    json.put("mensagem", this.getMensagem());
	    
	    if(this.getStatus() != 401) {
		    JSONArray avisosArray = new JSONArray();
		    
		    for (AvisoResponse aviso : this.avisos) {
	            avisosArray.put(aviso.toJson());
	        }
		    
		    json.put("avisos", avisosArray);
	    }

	    return json;
	}

}
