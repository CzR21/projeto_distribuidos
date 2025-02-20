package models;

import enums.Operacao;

public class LoginResponse extends ModelBase {

	private String token;

	public LoginResponse(Operacao operacao) {
		super(operacao);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}	
}
