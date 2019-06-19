package br.com.maispratos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import br.com.maispratos.util.GerenciadorJDBC;
import br.com.meuspratos.model.Ingrediente;
import br.com.meuspratos.model.Usuario;

public class PratoDAO {
	private IngredienteDAO ingredienteDAO = new IngredienteDAO();

	public boolean setIngredienteByUsuario(Usuario usuario) throws SQLException{
		Ingrediente ingrediente = new IngredienteDAO().getIngredienteByCodigoBarras(usuario.getIngrediente().getCodigoBarras());
		if(ingrediente == null){
			usuario.setIngrediente(ingredienteDAO.setIngrediente(usuario.getIngrediente()));
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
}
