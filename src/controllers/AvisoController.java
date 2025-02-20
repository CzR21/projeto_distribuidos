package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import dao.AvisoDAO;
import dao.LoginDAO;
import entities.Usuario;
import enums.Key;
import enums.Operacao;
import enums.Role;
import models.BuscarAvisoResponse;
import models.ListarAvisoResponse;
import models.ModelBase;
import validators.Validator;

public class AvisoController {

	private AvisoDAO avisoDao;
	private LoginDAO loginDao;

	public AvisoController(Connection conn) {
		this.avisoDao = new AvisoDAO(conn);
		this.loginDao = new LoginDAO(conn);
	}
	
	public JSONObject localizarAviso(JSONObject json) {
		try {
			String token = json.getString("token");
			int id = json.getInt("id");
			
			if(!Validator.validar(Key.id, String.valueOf(id)) || !Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.localizarAviso, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null) {
				return new ModelBase(Operacao.localizarAviso, 401, "Acesso não autorizado.").toJson();
			}

			BuscarAvisoResponse result = avisoDao.localizarAviso(id);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.localizarAviso, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.localizarAviso, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject listarAvisos(JSONObject json) {
		try {
			String token = json.getString("token");
	        int idCategoria = json.getInt("categoria");
			
			if(!Validator.validar(Key.ra, token) || !Validator.validar(Key.id, String.valueOf(idCategoria))) {
				return new ModelBase(Operacao.listarAvisos, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null) {
				return new ModelBase(Operacao.listarAvisos, 401, "Acesso não autorizado.").toJson();
			}

			ListarAvisoResponse result = idCategoria == 0 ? avisoDao.listarAvisos() : avisoDao.listarAvisos(idCategoria);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarAvisos, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarAvisos, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject listarAvisosUsuarios(JSONObject json) {
		try {
			String token = json.getString("token");
			String ra = json.getString("ra");
			
			if(!Validator.validar(Key.ra, token) || !Validator.validar(Key.ra, ra)) {
				return new ModelBase(Operacao.listarUsuarioAvisos, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || (usuario.getRole() == Role.User && !token.equals(ra))) {
				return new ModelBase(Operacao.listarUsuarioAvisos, 401, "Acesso não autorizado.").toJson();
			}

			ListarAvisoResponse result = avisoDao.listarAvisosUsuario(usuario.getId());
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarUsuarioAvisos, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarUsuarioAvisos, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject salvarAviso(JSONObject json) {
		try {
			String token = json.getString("token");
			JSONObject aviso = json.getJSONObject("aviso"); 
	        int id = aviso.getInt("id");
	        int idCategoria = aviso.getInt("categoria");
	        String titulo = aviso.getString("titulo");
	        String descricao = aviso.getString("descricao");
			
			if(!Validator.validar(Key.ra, token) || !Validator.validar(Key.titulo, titulo) || !Validator.validar(Key.descricao, descricao) || !Validator.validar(Key.id, String.valueOf(id)) || !Validator.validar(Key.id, String.valueOf(idCategoria))) {
				return new ModelBase(Operacao.salvarAviso, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || usuario.getRole() == Role.User) {
				return new ModelBase(Operacao.salvarAviso, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = id == 0 ? avisoDao.cadastrarAviso(titulo, descricao, idCategoria) : avisoDao.editarAviso(id, titulo, descricao, idCategoria);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.salvarAviso, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.salvarAviso, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject excluirAviso(JSONObject json) {
		try {
			String token = json.getString("token");
			int id = json.getInt("id");
			
			if(!Validator.validar(Key.id, String.valueOf(id)) || !Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.excluirAviso, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || usuario.getRole() == Role.User) {
				return new ModelBase(Operacao.excluirAviso, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = avisoDao.excluirAviso(id);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.excluirAviso, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.excluirAviso, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
}
