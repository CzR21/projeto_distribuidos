package controllers;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import dao.LoginDAO;
import enums.Key;
import enums.Operacao;
import models.LoginResponse;
import models.ModelBase;
import validators.Validator;

public class LoginController {
	
	private LoginDAO loginDao;

	public LoginController(Connection conn) {
		this.loginDao = new LoginDAO(conn);
	}

	public JSONObject login(JSONObject json) {
		try {
			String ra = json.getString("ra");
			String senha = json.getString("senha");
			
			if(!Validator.validar(Key.ra, ra) || !Validator.validar(Key.senha, senha)) {
				return new ModelBase(Operacao.login, 401, "Os campos recebidos nao sao validos.").toJson();
			}

			LoginResponse result = loginDao.login(ra, senha);
			
			JSONObject response = result.toJson();
			
			if(result.getStatus() == 200) {					
				response.put("token", result.getToken());
			}
			
			return response;				
			
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.login, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.login, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject logout(JSONObject json) {
		try {
			String ra = json.getString("token");
			
			if(!Validator.validar(Key.ra, ra)) {
				return new ModelBase(Operacao.login, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			ModelBase result = loginDao.logout(ra);
						
			return result.toJson();				
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.logout, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.logout, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject cadastro(JSONObject json) {
		try {
			String ra = json.getString("ra");
			String nome = json.getString("nome");
			String senha = json.getString("senha");
			
			if(!Validator.validar(Key.ra, ra) || !Validator.validar(Key.nome, nome) || !Validator.validar(Key.senha, senha)) {
				return new ModelBase(Operacao.cadastrarUsuario, 401, "Os campos recebidos nao sao validos.").toJson();
			}

			ModelBase result = loginDao.cadastro(ra, nome, senha);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.cadastrarUsuario, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.cadastrarUsuario, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
}
