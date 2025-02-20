package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import enums.Operacao;
import models.BuscarUsuarioResponse;
import models.ListarUsuarioResponse;
import models.ModelBase;
import models.UsuarioResponse;

public class UsuarioDAO {

	private Connection conn;
	
	public UsuarioDAO(Connection conn) {
		this.conn = conn;
	}
	
	public BuscarUsuarioResponse localizarUsuario(String ra) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND status");

	        st.setString(1, ra);
	        
	        rs = st.executeQuery();
	        
	        BuscarUsuarioResponse response = new BuscarUsuarioResponse();

	        if(rs.next()) {
	        	UsuarioResponse usuario = new UsuarioResponse(
	    			rs.getString("nome"), 
	    			rs.getString("ra"), 
	    			rs.getString("senha")
    			);

	        	response.setUsuario(usuario);	        	
	        	response.setStatus(201);
	        	response.setMensagem("Usuario encontrado com sucesso!");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Usuario não encontrado.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ListarUsuarioResponse listarUsuarios() throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT * FROM usuario WHERE status");
	        
	        rs = st.executeQuery();
	        
	        ListarUsuarioResponse response = new ListarUsuarioResponse();

	        while (rs.next()) {
	        	UsuarioResponse usuario = new UsuarioResponse(
	    			rs.getString("nome"), 
	    			rs.getString("ra"), 
	    			rs.getString("senha")
    			);

	        	response.adicionarUsuario(usuario);	        	
	        }
	        
	        response.setStatus(201);
	        response.setMensagem("Usuarios encontrados com sucesso!");
	      	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase editarUsuario(String ra, String nome, String senha) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND status");
	        
	        st.setString(1, ra);
	        
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.editarUsuario);
	        
	        if (rs.next()) {		
				st = conn.prepareStatement("UPDATE usuario SET nome = ?, senha = ?, \"dataModificacao\" = ? WHERE ra = ?");
				st.setString(1, nome);
				st.setString(2, senha);
				st.setDate(3, Date.valueOf(LocalDate.now()));
				st.setString(4, ra);
				st.executeUpdate();

	            response.setStatus(201);
	            response.setMensagem("Edição realizada com sucesso.");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Usuario não encontrado.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase excluirUsuario(String ra) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND status");
	        
	        st.setString(1, ra);
	        
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.excluirUsuario);
	        
	        if (rs.next()) {
				st = conn.prepareStatement("UPDATE sessao SET status = false, \"dataModificacao\" = ? WHERE status AND ra = ?");
				st.setDate(1, Date.valueOf(LocalDate.now()));
				st.setString(2, ra);
				st.executeUpdate();
				
				st = conn.prepareStatement("UPDATE usuario SET status = false, \"dataModificacao\" = ? WHERE status AND ra = ?");
				st.setDate(1, Date.valueOf(LocalDate.now()));
				st.setString(2, ra);
				st.executeUpdate();

	            response.setStatus(201);
	            response.setMensagem("Exclusão realizada com sucesso.");
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Usuario não encontrado.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
}
