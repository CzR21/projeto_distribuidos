package routes;

import java.sql.Connection;

import org.json.JSONException;
import org.json.JSONObject;

import controllers.AvisoController;
import controllers.CategoriaController;
import controllers.LoginController;
import controllers.UsuarioController;
import enums.Operacao;
import models.ModelBase;

public class Rotas {
	
	private AvisoController avisoController;
	private CategoriaController categoriaController;
	private LoginController loginController;
	private UsuarioController usuarioController;
	
	public Rotas(Connection conn) {
		this.avisoController = new AvisoController(conn);
		this.categoriaController = new CategoriaController(conn);
		this.loginController = new LoginController(conn);
		this.usuarioController = new UsuarioController(conn);
	}

    public JSONObject handleRequest(JSONObject requisicao) {
        try {
        	Operacao operacao = Operacao.valueOf(requisicao.getString("operacao"));
        	
        	return switch (operacao) {
        		//Login
	            case login -> this.loginController.login(requisicao);
	            case logout -> this.loginController.logout(requisicao);
	            case cadastrarUsuario -> this.loginController.cadastro(requisicao);
	            
	            //Usuario
	            case localizarUsuario -> this.usuarioController.localizarUsuario(requisicao);
	            case listarUsuarios -> this.usuarioController.listarUsuarios(requisicao);
	            case editarUsuario -> this.usuarioController.editarUsuario(requisicao);
	            case excluirUsuario -> this.usuarioController.excluirUsuario(requisicao);
	            
	            //Categoria
	            case localizarCategoria -> this.categoriaController.localizarCategoria(requisicao);
	            case listarCategorias -> this.categoriaController.listarCategorias(requisicao);
	            case salvarCategoria -> this.categoriaController.salvarCategoria(requisicao);
	            case excluirCategoria -> this.categoriaController.excluirCategoria(requisicao);
	            case listarUsuarioCategorias -> this.categoriaController.listarCategoriasUsuario(requisicao);
	            case cadastrarUsuarioCategoria -> this.categoriaController.cadastrarCategoriaUsuario(requisicao);
	            case descadastrarUsuarioCategoria -> this.categoriaController.excluirCategoriaUsuario(requisicao);
	            
	            //Aviso
	            case localizarAviso -> this.avisoController.localizarAviso(requisicao);
	            case listarAvisos -> this.avisoController.listarAvisos(requisicao);
	            case listarUsuarioAvisos -> this.avisoController.listarAvisosUsuarios(requisicao);
	            case salvarAviso -> this.avisoController.salvarAviso(requisicao);
	            case excluirAviso -> this.avisoController.excluirAviso(requisicao);
	            
	            //Erro
	            default -> new ModelBase(Operacao.naoEncontrada, 401, "Operacao nao encontrada").toJson();
	        };
        } catch (JSONException e) {
			e.printStackTrace();
			return new ModelBase(Operacao.naoEncontrada, 401, "Não foi possível ler o json recebido.").toJson();
		} catch (IllegalArgumentException e) {
			return new ModelBase(Operacao.naoEncontrada, 401, "Operacao nao encontrada").toJson();
        }
    }
}
