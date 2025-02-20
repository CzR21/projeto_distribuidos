package entities;

import java.sql.Date;
import enums.Role;

public class Usuario extends EntityBase {
	
	private String nome;
	private String ra;
	private String senha;
	private Role role;
	
	public Usuario() { }

	public Usuario(int id, String nome, String ra, String senha, Role role, Boolean status, Date dataCriacao, Date dataModificacao) {
		super(id, status, dataCriacao, dataModificacao);
		this.nome = nome;
		this.ra = ra;
		this.senha = senha;
		this.role = role;
	}
	
	public Usuario(int id, String nome, String ra, Role role) {
		super(id);
		this.nome = nome;
		this.ra = ra;
		this.role = role;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
