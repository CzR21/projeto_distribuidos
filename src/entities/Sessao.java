package entities;

import java.sql.Date;

public class Sessao {

	private int idUsuario;
	private Date validade;

	public Sessao() { }

	public int getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public Date getValidade() {
		return validade;
	}
	
	public void setValidade(Date validade) {
		this.validade = validade;
	}	
}
