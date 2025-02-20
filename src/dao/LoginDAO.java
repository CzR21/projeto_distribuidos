package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import entities.Usuario;
import enums.Operacao;
import enums.Role;
import models.LoginResponse;
import models.ModelBase;

public class LoginDAO {
	
	private Connection conn;
	
	public LoginDAO(Connection conn) {
		this.conn = conn;
	}

	public LoginResponse login(String ra, String senha) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND senha = ? AND status");
	        
	        st.setString(1, ra);
	        st.setString(2, senha);
	        
	        rs = st.executeQuery();
	        
	        LoginResponse response = new LoginResponse(Operacao.login);

	        if (rs.next()) {
	        	st = conn.prepareStatement("UPDATE sessao SET status = false, \"dataModificacao\" = ? WHERE status AND ra = ?");
	            st.setDate(1, Date.valueOf(LocalDate.now()));
	            st.setString(2, ra);
	            st.executeUpdate();
	        	
	            st = conn.prepareStatement("INSERT INTO sessao (ra) VALUES (?)");
				st.setString(1, ra);
				st.executeUpdate();
	        		        	
	        	response.setStatus(200);
	        	response.setMensagem("Login realizado com sucesso");
	        	response.setToken(ra);
	        } else {
	        	response.setStatus(401);
	        	response.setMensagem("Credenciais incorretas.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase logout(String ra) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM sessao WHERE ra = ? AND status ORDER BY \"dataCriacao\" DESC");
	        
	        st.setString(1, ra);
	        
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.logout);
	        
	        if (rs.next()) {
	        	int idSessao = rs.getInt("id");
	        	
	            st = conn.prepareStatement("UPDATE sessao SET status = false, \"dataModificacao\" = ? WHERE id = ?");
	            st.setDate(1, Date.valueOf(LocalDate.now()));
	            st.setInt(2, idSessao);
	            st.executeUpdate();

	            response.setStatus(200);
	            response.setMensagem("Logout realizado com sucesso");
	        } else {
	            response.setStatus(401);
	            response.setMensagem("Não foi possível ler o json recebido.");
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public ModelBase cadastro(String ra, String nome, String senha) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	    	st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND status");
	        st.setString(1, ra);
	        rs = st.executeQuery();

	        ModelBase response = new ModelBase(Operacao.cadastrarUsuario);
	        
	        if (rs.next()) {
	        	response.setStatus(401);
	            response.setMensagem("Não foi cadastrar pois o usuario informado ja existe");
	        } else {
	        	st = conn.prepareStatement("INSERT INTO usuario (nome, ra, senha) VALUES (?, ?, ?)");
	        	st.setString(1, nome);
	        	st.setString(2, ra);
	        	st.setString(3, senha);
	        	st.executeUpdate();
	        	
	        	response.setStatus(201);
	        	response.setMensagem("Cadastro realizado com sucesso.");	            
	        }
	        
	        return response;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public Usuario validarSessao(String token) throws SQLException {
	    PreparedStatement st = null;    
	    ResultSet rs = null;    

	    try {
	        st = conn.prepareStatement("SELECT * FROM sessao WHERE ra = ? AND status ORDER BY \"dataCriacao\" DESC");
	        
	        st.setString(1, token);
	        
	        rs = st.executeQuery();
	        
	        if (rs.next()) {
	        	st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ? AND status");
		        
		        st.setString(1, token);
		        
		        rs = st.executeQuery();
	        	
		        if (rs.next()) {
		        	Usuario usuario = new Usuario(
	        			rs.getInt("id"), 
	        			rs.getString("nome"), 
	        			rs.getString("ra"), 
	        			Role.valueOf(rs.getString("role"))
        			);
			            
		            return usuario;
	        	}
	        } 
	        
	        return null;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
}
