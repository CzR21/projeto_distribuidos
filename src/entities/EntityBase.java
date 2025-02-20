package entities;

import java.sql.Date;

public class EntityBase {
	
	private int id;
	private Boolean status;
	private Date dataCriacao;
	private Date dataModificacao;
	
	public EntityBase() { }
	
	public EntityBase(int id) {
		this.id = id;
	}
	
	public EntityBase(int id, Boolean status, Date dataCriacao, Date dataModificacao) {
		this.id = id;
		this.status = status;
		this.dataCriacao = dataCriacao;
		this.dataModificacao = dataModificacao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}
}
