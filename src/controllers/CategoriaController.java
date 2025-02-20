package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import dao.CategoriaDAO;
import dao.LoginDAO;
import entities.Usuario;
import enums.Key;
import enums.Operacao;
import enums.Role;
import models.BuscarCategoriaResponse;
import models.ListarCategoriaResponse;
import models.ModelBase;
import validators.Validator;

public class CategoriaController {

	private CategoriaDAO categoriaDao;
	private LoginDAO loginDao;

	public CategoriaController(Connection conn) {
		this.categoriaDao = new CategoriaDAO(conn);
		this.loginDao = new LoginDAO(conn);
	}
	
	public JSONObject localizarCategoria(JSONObject json) {
		try {
			String token = json.getString("token");
			int id = json.getInt("id");
			
			if(!Validator.validar(Key.id, String.valueOf(id)) || !Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.localizarCategoria, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null) {
				return new ModelBase(Operacao.localizarCategoria, 401, "Acesso não autorizado.").toJson();
			}

			BuscarCategoriaResponse result = categoriaDao.localizarCategoria(id);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.localizarCategoria, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.localizarCategoria, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject listarCategorias(JSONObject json) {
		try {
			String token = json.getString("token");
			
			if(!Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.listarCategorias, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null) {
				return new ModelBase(Operacao.listarCategorias, 401, "Acesso não autorizado.").toJson();
			}

			ListarCategoriaResponse result = categoriaDao.listarCategorias();
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarCategorias, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarCategorias, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}

	public JSONObject listarCategoriasUsuario(JSONObject json) {
		try {
			String token = json.getString("token");
	        String ra = json.getString("ra");
			
			if(!Validator.validar(Key.ra, token) || !Validator.validar(Key.ra, ra)) {
				return new ModelBase(Operacao.listarUsuarioCategorias, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || (usuario.getRole() == Role.User && !token.equals(ra))) {
				return new ModelBase(Operacao.listarUsuarioCategorias, 401, "Acesso não autorizado.").toJson();
			}

			ListarCategoriaResponse result = categoriaDao.listarCategorias(ra);
			
			return result.toJson2();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarUsuarioCategorias, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.listarUsuarioCategorias, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject cadastrarCategoriaUsuario(JSONObject json) {
		try {
			String token = json.getString("token");
	        String ra = json.getString("ra");
	        int idCategoria = json.getInt("categoria");
			
			if(!Validator.validar(Key.ra, token) || !Validator.validar(Key.ra, ra) || !Validator.validar(Key.id, String.valueOf(idCategoria))) {
				return new ModelBase(Operacao.cadastrarUsuarioCategoria, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || (usuario.getRole() == Role.User && !token.equals(ra))) {
				return new ModelBase(Operacao.cadastrarUsuarioCategoria, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = categoriaDao.cadastrarCategoriaUsuario(ra, idCategoria);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.cadastrarUsuarioCategoria, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.cadastrarUsuarioCategoria, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject salvarCategoria(JSONObject json) {
		try {
			String token = json.getString("token");
			JSONObject categoria = json.getJSONObject("categoria"); 
	        int id = categoria.getInt("id");
	        String nome = categoria.getString("nome");
			
			if(!Validator.validar(Key.ra, token) || !Validator.validar(Key.nome, nome) || !Validator.validar(Key.id, String.valueOf(id))) {
				return new ModelBase(Operacao.salvarCategoria, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || usuario.getRole() == Role.User) {
				return new ModelBase(Operacao.salvarCategoria, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = id == 0 ? categoriaDao.cadastrarCategoria(nome) : categoriaDao.editarCategoria(id, nome);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.salvarCategoria, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.salvarCategoria, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
	
	public JSONObject excluirCategoria(JSONObject json) {
		try {
			String token = json.getString("token");
			int id = json.getInt("id");
			
			if(!Validator.validar(Key.id, String.valueOf(id)) || !Validator.validar(Key.ra, token)) {
				return new ModelBase(Operacao.excluirCategoria, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || usuario.getRole() == Role.User) {
				return new ModelBase(Operacao.excluirCategoria, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = categoriaDao.excluirCategoria(id);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.excluirCategoria, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.excluirCategoria, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}

	public JSONObject excluirCategoriaUsuario(JSONObject json) {
		try {
			String token = json.getString("token");
	        String ra = json.getString("ra");
	        int idCategoria = json.getInt("categoria");
			
			if(!Validator.validar(Key.ra, token) || !Validator.validar(Key.ra, ra) || !Validator.validar(Key.id, String.valueOf(idCategoria))) {
				return new ModelBase(Operacao.descadastrarUsuarioCategoria, 401, "Os campos recebidos nao sao validos.").toJson();
			}
			
			Usuario usuario = loginDao.validarSessao(token);
			
			if(usuario == null || (usuario.getRole() == Role.User && !token.equals(ra))) {
				return new ModelBase(Operacao.descadastrarUsuarioCategoria, 401, "Acesso não autorizado.").toJson();
			}

			ModelBase result = categoriaDao.excluirCategoriaUsuario(ra, idCategoria);
			
			return result.toJson();							
		} catch (SQLException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.descadastrarUsuarioCategoria, 401, "O servidor nao conseguiu conectar com o banco de dados.").toJson();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.descadastrarUsuarioCategoria, 401, "Não foi possível ler o json recebido.").toJson();
		}
	}
}
