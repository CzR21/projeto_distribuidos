package controllers;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import dao.LoginDAO;
import dao.UsuarioDAO;
import entities.Usuario;
import enums.Key;
import enums.Operacao;
import enums.Role;
import models.BuscarUsuarioResponse;
import models.ListarUsuarioResponse;
import models.ModelBase;
import validators.Validator;

public class UsuarioController {

	private UsuarioDAO usuarioDao;
	private LoginDAO loginDao;

	public UsuarioController(Connection conn) {
		this.usuarioDao = new UsuarioDAO(conn);
		this.loginDao = new LoginDAO(conn);
	}
	
	public JSONObject localizarUsuario(JSONObject json) {
		try {
			String token = json.getString("token");
			String ra = json.getString("ra");
			
			if(!Validator.validar(Key.ra, ra) || !Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.localizarUsuario, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
					
			if(usuario == null || (usuario.getRole() == Role.User && !token.equals(ra))) {
				return new ModelBase(Operacao.localizarUsuario, 401, "Acesso não autorizado.").toJson();
			}

			BuscarUsuarioResponse result = usuarioDao.localizarUsuario(ra);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.localizarUsuario, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.localizarUsuario, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject listarUsuarios(JSONObject json) {
		try {
			String token = json.getString("token");
			
			if(!Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.listarUsuarios, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || usuario.getRole() == Role.User) {
				return new ModelBase(Operacao.listarUsuarios, 401, "Acesso não autorizado.").toJson();
			}

			ListarUsuarioResponse result = usuarioDao.listarUsuarios();
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarUsuarios, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarUsuarios, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject editarUsuario(JSONObject json) {
		try {
			String token = json.getString("token");
			
			JSONObject usuarioObject = json.getJSONObject("usuario"); 
			String ra = usuarioObject.getString("ra");
			String nome = usuarioObject.getString("nome");
			String senha = usuarioObject.getString("senha");
			
			if(!Validator.validar(Key.ra, ra) || !Validator.validar(Key.ra, token) || !Validator.validar(Key.nome, nome) || !Validator.validar(Key.senha, senha)) {
				return new ModelBase(Operacao.editarUsuario, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || (usuario.getRole() == Role.User && !token.equals(ra))) {
				return new ModelBase(Operacao.editarUsuario, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = usuarioDao.editarUsuario(ra, nome, senha);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.editarUsuario, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.editarUsuario, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject excluirUsuario(JSONObject json) {
		try {
			String token = json.getString("token");
			String ra = json.getString("ra");
			
			if(!Validator.validar(Key.ra, ra) || !Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.excluirUsuario, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || (usuario.getRole() == Role.User && !token.equals(ra))) {
				return new ModelBase(Operacao.excluirUsuario, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = usuarioDao.excluirUsuario(ra);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.excluirUsuario, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.excluirUsuario, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
}
