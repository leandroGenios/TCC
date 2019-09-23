package br.com.maispratos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public Usuario updateUsuario(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String parametros = "NOME";
			String sql = "UPDATE usuario SET ";
			if(usuario.getEmail() != null && !usuario.getEmail().equals("")){
				sql += " EMAIL = ?,";
				parametros += "/EMAIL";
			}
			if(usuario.getSenha() != null && !usuario.getSenha().equals("")){
				sql += " SENHA = ?,";
				parametros += "/SENHA";
			}
			sql += " NOME = ?";
			sql += " WHERE ID = ?";
			System.out.println(sql);
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			switch (parametros) {
			case "NOME":
				System.out.println(usuario.getNome());
				System.out.println(usuario.getId());
				stmt.setString(1, usuario.getNome());
				stmt.setInt(2, usuario.getId());
				break;
			case "NOME/EMAIL":
				stmt.setString(1, usuario.getEmail());
				stmt.setString(2, usuario.getNome());
				stmt.setInt(3, usuario.getId());
				break;
			case "NOME/SENHA":
				stmt.setString(1, usuario.getSenha());
				stmt.setString(2, usuario.getNome());
				stmt.setInt(3, usuario.getId());
				break;
			case "NOME/EMAIL/SENHA":
				stmt.setString(1, usuario.getEmail());
				stmt.setString(2, usuario.getSenha());
				stmt.setString(3, usuario.getNome());
				stmt.setInt(4, usuario.getId());
				break;
			default:
				break;
			}
			
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
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
	
	public boolean nomeExiste(String nome) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT * FROM USUARIO WHERE LOWER(nome) = LOWER(?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, nome);
			
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
	
	public Classificacao getClassificacao(int qtde) throws SQLException{
		Classificacao classificacao = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT *"
					   + "  FROM classificacao c"
					   + " WHERE ? >= c.menor_valor"
					   + "   AND ? <= c.maior_valor";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, qtde);
			stmt.setInt(2, qtde);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				classificacao = new Classificacao();
				classificacao.setId(rs.getInt("ID"));
				classificacao.setDescricao(rs.getString("DESCRICAO"));
				classificacao.setMenorValor(rs.getInt("MENOR_VALOR"));
				classificacao.setMaiorValor(rs.getInt("MAIOR_VALOR"));
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}

		return classificacao;
	}
	
	public boolean addAmigo(int codUsuario, Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO usuario_amigo VALUES (?, ?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, codUsuario);
			stmt.setInt(2, usuario.getId());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
	
	public List<Usuario> listAmigos(int idUsuario) throws SQLException{
		List<Usuario> usuarios = new ArrayList<Usuario>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT u.id,\n"
					   + "		 u.nome\n"
					   + "  FROM usuario_amigo UA\n"
					   + " INNER JOIN usuario u\n"
					   + "	  ON u.id = ua.usuario_amigo_id\n" 
					   + " WHERE UA.usuario_id = ?"
					   + " ORDER BY U.NOME";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, idUsuario);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("ID"));
				usuario.setNome(rs.getString("NOME"));
				usuario.setAmigo(true);
				usuarios.add(usuario);
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}

		return usuarios;
	}
	
	public List<Usuario> listUsuarios(int idUsuario) throws SQLException{
		List<Usuario> usuarios = new ArrayList<Usuario>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT u.id,\n"
					   + "		 u.nome\n"
					   + "	FROM usuario U "
					   + " WHERE NOT EXISTS (SELECT * "
					   + "					   FROM usuario_amigo UA "
					   + "					  WHERE UA.usuario_amigo_id = U.id "
					   + "						AND UA.usuario_id = ?) "
					   + "   AND U.id <> ?"
					   + " ORDER BY U.NOME";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idUsuario);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("ID"));
				usuario.setNome(rs.getString("NOME"));
				usuario.setAmigo(false);
				usuarios.add(usuario);
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}

		return usuarios;
	}
}
