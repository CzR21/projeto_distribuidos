package models;

import org.json.JSONObject;
import enums.Operacao;

public class ModelBase {

	private Operacao operacao;
	private int status;
	private String mensagem;
	
	public ModelBase() { }
	
	public ModelBase(Operacao operacao) {
		this.operacao = operacao;
	}
	
	public ModelBase(Operacao operacao, int status, String mensagem) {
		this.operacao = operacao;
		this.status = status;
		this.mensagem = mensagem;
	}
	
	public Operacao getOperacao() {
		return operacao;
	}
	
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("operacao", this.operacao.toString());
		json.put("status", this.status);
		json.put("mensagem", this.mensagem);
		
		return json;
	}
}