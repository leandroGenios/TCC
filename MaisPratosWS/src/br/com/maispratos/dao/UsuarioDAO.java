package br.com.maispratos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import br.com.maispratos.util.GerenciadorJDBC;
import br.com.meuspratos.model.Classificacao;
import br.com.meuspratos.model.Usuario;

public class UsuarioDAO {
	public Usuario setUsuario(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO usuario VALUES (NULL, ?, ?, ?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			usuario.setId(rs.getInt(1));
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		setClassificacao(usuario);
		
		return usuario;
	}
	
	public Usuario setClassificacao(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO usuario_classificacao VALUES (?, ?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getId());
			stmt.setInt(2, 1);
			
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return usuario;
	}
	
	public boolean emailExiste(String email) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT * FROM USUARIO WHERE EMAIL = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, email);
			
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
	}
	
	public boolean usuarioExiste(String email, String senha) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT * FROM USUARIO WHERE EMAIL = ? AND SENHA = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, email);
			stmt.setString(2, senha);
			
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
	}
	
	public Usuario getUsuario(String email) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT u.id usuario_id,"
					   + "		 u.nome usuario_nome,"
					   + "		 u.email usuario_email,"
					   + "		 u.senha usuario_senha,"
					   + "		 c.descricao classificacao"
					   + "	FROM usuario u"
					   + " inner join usuario_classificacao uc"
					   + "	  on uc.usuario_id = u.id"
					   + " inner join classificacao c"
					   + "	  on c.id = uc.classificacao_id"
					   + " WHERE EMAIL = ?";
			System.out.println(sql);
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, email);
			
			ResultSet rs = stmt.executeQuery();
			
			Usuario usuario = null;
			while (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("USUARIO_ID"));
				usuario.setEmail(rs.getString("USUARIO_EMAIL"));
				usuario.setNome(rs.getString("USUARIO_NOME"));
				usuario.setSenha(rs.getString("USUARIO_SENHA"));
				Classificacao classificacao = new Classificacao();
				classificacao.setDescricao(rs.getString("CLASSIFICACAO"));
				usuario.setClassificacao(classificacao);
			}
			System.out.println(usuario.getClassificacao().getDescricao());
			return usuario;
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
	}
	
	public String getClassificacao(int qtde) throws SQLException{
		String classificacao = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT c.descricao"
					   + "  FROM classificacao c"
					   + " WHERE ? >= c.menor_valor"
					   + "   AND ? <= c.maior_valor";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, qtde);
			stmt.setInt(2, qtde);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				classificacao = rs.getString("DESCRICAO");
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}

		return classificacao;
	}
}
