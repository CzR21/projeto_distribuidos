package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import enums.Operacao;
import models.AvisoResponse;
import models.BuscarAvisoResponse;
import models.CategoriaResponse;
import models.ListarAvisoResponse;
import models.ModelBase;

public class AvisoDAO {

	private Connection conn;
	
	public AvisoDAO(Connection conn) {
		this.conn = conn;
	}
	
	public BuscarAvisoResponse localizarAviso(int id) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT a.id, a.titulo, a.descricao, a.idcategoria, c.nome FROM aviso AS a JOIN categoria AS c ON c.id = a.idcategoria WHERE a.id = ? AND a.status");

	        st.setInt(1, id);
	        
	        rs = st.executeQuery();
	        
	        BuscarAvisoResponse response = new BuscarAvisoResponse();

	        if(rs.next()) {
	        	AvisoResponse aviso = new AvisoResponse(
	        		rs.getInt("id"), 
	    			rs.getString("titulo"),
	    			rs.getString("descricao"),
	    			new CategoriaResponse(rs.getInt("idcategoria"), rs.getString("nome"))
    			);

	        	response.setAviso(aviso);	        	
	        	response.setStatus(201);
	        	response.setMensagem("Aviso encontrado com sucesso!");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Aviso não encontrada.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ListarAvisoResponse listarAvisos() throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	    	st = conn.prepareStatement("SELECT a.id, a.titulo, a.descricao, a.idcategoria, c.nome FROM aviso AS a JOIN categoria AS c ON c.id = a.idcategoria WHERE a.status");
	        
	        rs = st.executeQuery();
	        
	        ListarAvisoResponse response = new ListarAvisoResponse();

	        while (rs.next()) {
	        	AvisoResponse aviso = new AvisoResponse(
		        		rs.getInt("id"), 
		    			rs.getString("titulo"),
		    			rs.getString("descricao"),
		    			new CategoriaResponse(rs.getInt("idcategoria"), rs.getString("nome"))
	    			);

	        	response.adicionarAviso(aviso);	        	
	        }
	        
	        response.setStatus(201);
	        response.setMensagem("Avisos encontrados com sucesso!");
	      	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ListarAvisoResponse listarAvisos(int idCategoria) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT a.id, a.titulo, a.descricao, a.idcategoria, c.nome FROM aviso AS a JOIN categoria AS c ON c.id = a.idcategoria WHERE a.status AND a.idCategoria = ?");
	        
	        st.setInt(1, idCategoria);
	        
	        rs = st.executeQuery();
	        
	        ListarAvisoResponse response = new ListarAvisoResponse();

	        while (rs.next()) {
	        	AvisoResponse aviso = new AvisoResponse(
		        		rs.getInt("id"), 
		    			rs.getString("titulo"),
		    			rs.getString("descricao"),
		    			new CategoriaResponse(rs.getInt("id"), rs.getString("nome"))
	    			);

	        	response.adicionarAviso(aviso);	        	
	        }
	        
	        response.setStatus(201);
	        response.setMensagem("Avisos encontrados com sucesso!");
	      	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ListarAvisoResponse listarAvisosUsuario(int idUsuario) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT a.id, a.titulo, a.descricao, a.idcategoria, c.nome FROM aviso AS a " +
	        							"JOIN categoria AS c ON c.id = a.idcategoria " + 
					                    "JOIN categoria_usuario AS cu ON cu.idcategoria = a.idcategoria " +
					                    "WHERE cu.idusuario = ? AND a.status");

	        st.setInt(1, idUsuario);
	        
	        rs = st.executeQuery();
	        
	        ListarAvisoResponse response = new ListarAvisoResponse(Operacao.listarUsuarioAvisos);

	        while (rs.next()) {
	        	AvisoResponse aviso = new AvisoResponse(
		        		rs.getInt("id"), 
		    			rs.getString("titulo"),
		    			rs.getString("descricao"),
		    			new CategoriaResponse(rs.getInt("idCategoria"), rs.getString("nome"))
	    			);

	        	response.adicionarAviso(aviso);	        	
	        }
	        
	        response.setStatus(201);
	        response.setMensagem("Avisos encontrados com sucesso!");
	      	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase cadastrarAviso(String titulo, String descricao, int idCategoria) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	    	st = conn.prepareStatement("SELECT * FROM categoria WHERE id = ? AND status");
	        
	        st.setInt(1, idCategoria);
	        
	        rs = st.executeQuery();
	        
	        ModelBase response = new ModelBase(Operacao.salvarAviso);
	        
	        if(rs.next()) {
	        	st = conn.prepareStatement("INSERT INTO aviso (titulo, descricao, idCategoria) VALUES (?, ?, ?)");
		        
		        st.setString(1, titulo);
		        st.setString(2, descricao);
		        st.setInt(3, idCategoria);
		        
		        st.executeUpdate();
	        	
	        	response.setStatus(201);
	        	response.setMensagem("Aviso salvo com sucesso");
	        }else {	        	
	        	response.setStatus(401);
	        	response.setMensagem("Categoria não encontrada.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase editarAviso(int id, String titulo, String descricao, int idCategoria) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM aviso WHERE id = ? AND status");
	        
	        st.setInt(1, id);
	        
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.salvarAviso);
	        
	        if (rs.next()) {		
	        	st = conn.prepareStatement("SELECT * FROM categoria WHERE id = ? AND status");
		        
		        st.setInt(1, idCategoria);
		        
		        rs = st.executeQuery();
		        
		        if(rs.next()) {
		        	st = conn.prepareStatement("UPDATE aviso SET titulo = ?, descricao = ?, idCategoria = ?, \"dataModificacao\" = ? WHERE id = ?");
			        
			        st.setString(1, titulo);
			        st.setString(2, descricao);
			        st.setInt(3, idCategoria);
			        st.setDate(4, Date.valueOf(LocalDate.now()));
			        st.setInt(5, id);
			        
			        st.executeUpdate();
		        	
		        	response.setStatus(201);
		        	response.setMensagem("Aviso salvo com sucesso");
		        }else {	        	
		        	response.setStatus(401);
		        	response.setMensagem("Categoria não encontrada.");
		        }
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Aviso não encontrada.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase excluirAviso(int id) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM aviso WHERE id = ? AND status");
	        
	        st.setInt(1, id);
	        
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.excluirAviso);
	        
	        if (rs.next()) {
				st = conn.prepareStatement("UPDATE aviso SET status = false, \"dataModificacao\" = ? WHERE id = ?");
				st.setDate(1, Date.valueOf(LocalDate.now()));
		        st.setInt(2, id);
				st.executeUpdate();

	            response.setStatus(201);
	            response.setMensagem("Exclusão realizada com sucesso.");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Aviso não encontrado.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}	
}
