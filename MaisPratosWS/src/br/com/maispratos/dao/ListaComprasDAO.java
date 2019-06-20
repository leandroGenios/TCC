package br.com.maispratos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.maispratos.util.GerenciadorJDBC;
import br.com.meuspratos.model.Ingrediente;
import br.com.meuspratos.model.UnidadeMedida;
import br.com.meuspratos.model.Usuario;

public class ListaComprasDAO {

	public List<Ingrediente> getListaCompras(int idUsuario) throws SQLException{
		List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT I.id ID_INGREDIENTE," + 
					"	         I.codigo_barras CODIGO_BARRAS," + 
					"            I.nome NOME_INGREDIENTE," + 
					"            UM.id ID_UNIDADE_MEDIDA," + 
					"            UM.sigla SIGLA_UNIDADE_MEDIDA," + 
					"            LC.quantidade QUANTIDADE" + 
					"       FROM lista_compra LC" + 
					"      INNER JOIN ingrediente I" + 
					" 	      ON I.id = LC.ingrediente_id" + 
					"      INNER JOIN unidade_medida UM" + 
					"         ON UM.id = LC.unidade_medida_id" + 
					"      WHERE LC.usuario_id = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				UnidadeMedida unidadeMedida = new UnidadeMedida();
				unidadeMedida.setId(rs.getInt("ID_UNIDADE_MEDIDA"));
				unidadeMedida.setSigla(rs.getString("SIGLA_UNIDADE_MEDIDA"));

				Ingrediente ingrediente = new Ingrediente();
				ingrediente.setId(rs.getInt("ID_INGREDIENTE"));
				ingrediente.setCodigoBarras(rs.getDouble("CODIGO_BARRAS"));
				ingrediente.setNome(rs.getString("NOME_INGREDIENTE"));
				ingrediente.setQuantidade(rs.getFloat("QUANTIDADE"));
				ingrediente.setUnidadeMedida(unidadeMedida);
				
				ingredientes.add(ingrediente);
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return ingredientes;
	}
	
	public boolean setIngredienteByUsuario(Usuario usuario) throws SQLException{
		
		Ingrediente ingrediente = new IngredienteDAO().getIngredienteByCodigoBarras(usuario.getIngrediente().getCodigoBarras());
		if(ingrediente == null){
			usuario.setIngrediente(setIngrediente(usuario.getIngrediente()));
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO lista_compra (usuario_id, ingrediente_id, unidade_medida_id, quantidade) VALUES (?,?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getId());
			stmt.setInt(2, usuario.getIngrediente().getId());
			stmt.setInt(3, usuario.getIngrediente().getUnidadeMedida().getId());
			stmt.setFloat(4, usuario.getIngrediente().getQuantidade());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
	
	public Ingrediente setIngrediente(Ingrediente ingrediente) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO ingrediente VALUES (NULL, ?, ?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setDouble(1, ingrediente.getCodigoBarras());
			stmt.setString(2, ingrediente.getNome());
			
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			ingrediente.setId(rs.getInt(1));
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return ingrediente;
	}
	
	public boolean updateIngredienteByUsuario(Usuario usuario) throws SQLException{
		if(deleteIngredienteByUsuario(usuario.getId(), usuario.getIngrediente().getId())) {
			return setIngredienteByUsuario(usuario);
		}
		return false;
	}
	
	public boolean deleteIngredienteByUsuario(int codUsuario, int codIngrediente) throws SQLException{
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "DELETE FROM lista_compra "
					   + " WHERE usuario_id = ? "
					   + "   AND ingrediente_id = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, codUsuario);
			stmt.setInt(2, codIngrediente);
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
}