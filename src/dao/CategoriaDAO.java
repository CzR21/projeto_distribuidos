package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import enums.Operacao;
import models.BuscarCategoriaResponse;
import models.ModelBase;
import models.CategoriaResponse;
import models.ListarCategoriaResponse;

public class CategoriaDAO {

	private Connection conn;
	
	public CategoriaDAO(Connection conn) {
		this.conn = conn;
	}
	
	public BuscarCategoriaResponse localizarCategoria(int id) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT * FROM categoria WHERE id = ? AND status");

	        st.setInt(1, id);
	        
	        rs = st.executeQuery();
	        
	        BuscarCategoriaResponse response = new BuscarCategoriaResponse();

	        if(rs.next()) {
	        	CategoriaResponse categoria = new CategoriaResponse(
	        		rs.getInt("id"), 
	    			rs.getString("nome")
    			);

	        	response.setCategoria(categoria);	        	
	        	response.setStatus(201);
	        	response.setMensagem("Categoria encontrada com sucesso!");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Categoria não encontrada.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ListarCategoriaResponse listarCategorias() throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT * FROM categoria WHERE status");
	        
	        rs = st.executeQuery();
	        
	        ListarCategoriaResponse response = new ListarCategoriaResponse();

	        while (rs.next()) {
	        	CategoriaResponse categoria = new CategoriaResponse(
        			rs.getInt("id"), 
	    			rs.getString("nome")
    			);

	        	response.adicionarCategoria(categoria);	        	
	        }
	        
	        response.setStatus(201);
	        response.setMensagem("Categorias encontradas com sucesso!");
	      	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ListarCategoriaResponse listarCategorias(String ra) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT c.* FROM public.categoria_usuario AS cu " +
                    "JOIN public.categoria AS c ON c.id = cu.idCategoria " +
                    "JOIN public.usuario AS u ON u.id = cu.idusuario " +
                    "WHERE u.ra = ?");
	        
	        st.setString(1, ra);
	        
	        rs = st.executeQuery();
	        
	        ListarCategoriaResponse response = new ListarCategoriaResponse();

	        while (rs.next()) {
	        	CategoriaResponse categoria = new CategoriaResponse(
        			rs.getInt("id"), 
	    			rs.getString("nome")
    			);

	        	response.adicionarCategoria(categoria);	        	
	        }
	        
	        response.setStatus(201);
	        response.setMensagem("Categorias encontradas com sucesso!");
	      	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase cadastrarCategoria(String nome) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	    	st = conn.prepareStatement("INSERT INTO categoria (nome) VALUES (?)");
	        
	        st.setString(1, nome);
	        
	        st.executeUpdate();

	        ModelBase response = new ModelBase(Operacao.salvarCategoria);
	        
	        response.setStatus(201);
	        response.setMensagem("Categoria salva com sucesso.");
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase cadastrarCategoriaUsuario(String ra, int idCategoria) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	    	st = conn.prepareStatement("SELECT * FROM categoria WHERE id = ? AND status");
	        
	        st.setInt(1, idCategoria);
	        
	        rs = st.executeQuery();
	        
	        ModelBase response = new ModelBase(Operacao.cadastrarUsuarioCategoria);
	        
	        if(rs.next()) {
	        	
	        	st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND status");
		        
		        st.setString(1, ra);
		        
		        rs = st.executeQuery();
		        
		        if(rs.next()) {
		        	int idUsuario = rs.getInt("id");
		        	
		        	st = conn.prepareStatement("SELECT * FROM categoria_usuario WHERE idCategoria = ? AND idUsuario = ? AND status");
			        
			        st.setInt(1, idCategoria);
			        st.setInt(2, idUsuario);
			        
			        rs = st.executeQuery();
			        
			        if(rs.next()) {
			        	response.setStatus(401);
			        	response.setMensagem("Usuario já associado nessa categoria.");	        	
			        } else {
			        	st = conn.prepareStatement("INSERT INTO categoria_usuario (idUsuario, idCategoria) VALUES (?, ?)");
				        
				        st.setInt(1, idUsuario);
				        st.setInt(2, idCategoria);

						st.executeUpdate();
			        	
			        	response.setStatus(201);
			        	response.setMensagem("Cadastro em categoria realizado com sucesso.");
			        }
		        } else {
		        	response.setStatus(401);
		        	response.setMensagem("Usuario não encontrada.");	        	
		        }
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Categoria não encontrada.");	        	
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase editarCategoria(int id, String nome) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM categoria WHERE id = ? AND status");
	        
	        st.setInt(1, id);
	        
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.salvarCategoria);
	        
	        if (rs.next()) {		
				st = conn.prepareStatement("UPDATE categoria SET nome = ?, \"dataModificacao\" = ? WHERE id = ?");
				st.setString(1, nome);
				st.setDate(2, Date.valueOf(LocalDate.now()));
				st.setInt(3, id);
				st.executeUpdate();

	            response.setStatus(201);
	            response.setMensagem("Edição realizada com sucesso.");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Categoria não encontrada.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase excluirCategoria(int id) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM categoria WHERE id = ? AND status");
	        
	        st.setInt(1, id);
	        
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.excluirCategoria);
	        
	        if (rs.next()) {
				st = conn.prepareStatement("UPDATE categoria SET status = false, \"dataModificacao\" = ? WHERE id = ?");
				st.setDate(1, Date.valueOf(LocalDate.now()));
		        st.setInt(2, id);
				st.executeUpdate();

	            response.setStatus(201);
	            response.setMensagem("Exclusão realizada com sucesso.");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Categoria não encontrada.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase excluirCategoriaUsuario(String ra, int idCategoria) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	    	st = conn.prepareStatement("SELECT * FROM categoria WHERE id = ? AND status");
	        
	        st.setInt(1, idCategoria);
	        
	        rs = st.executeQuery();
	        
	        ModelBase response = new ModelBase(Operacao.descadastrarUsuarioCategoria);
	        
	        if(rs.next()) {
	        	st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND status");
		        
		        st.setString(1, ra);
		        
		        rs = st.executeQuery();
		        
		        if(rs.next()) {
		        	int idUsuario = rs.getInt("id");
		        	
		        	st = conn.prepareStatement("SELECT * FROM categoria_usuario WHERE idCategoria = ? AND idUsuario = ? AND status");
			        
			        st.setInt(1, idCategoria);
			        st.setInt(2, idUsuario);
			        
			        rs = st.executeQuery();
			        
			        if(rs.next()) {
			        	st = conn.prepareStatement("UPDATE categoria_usuario SET status = false, \"dataModificacao\" = ? WHERE idCategoria = ? AND idUsuario = ?");
			        	
			        	st.setInt(1, idCategoria);
			        	st.setInt(2, idUsuario);
			        	
			        	st.executeUpdate();
			        	
			        	response.setStatus(201);
			        	response.setMensagem("Descadastro realizado com sucesso.");
			        } else {
			        	response.setStatus(401);
			        	response.setMensagem("Usuario não esta associado a essa categoria.");	        	
			        }
		        } else {
		        	response.setStatus(401);
		        	response.setMensagem("Usuario não encontrada.");	        	
		        }
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Categoria não encontrada.");	        	
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
}
