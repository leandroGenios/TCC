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

public class IngredienteDAO {

	public List<Ingrediente> getIngredientes(int idUsuario) throws SQLException{
		List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT I.id ID_INGREDIENTE,\r\n" + 
					"	   I.codigo_barras CODIGO_BARRAS,\r\n" + 
					"       I.nome NOME_INGREDIENTE,\r\n" + 
					"       UM.id ID_UNIDADE_MEDIDA,\r\n" + 
					"       UM.sigla SIGRA_UNIDADE_MEDIDA,\r\n" + 
					"       UI.quantidade QUANTIDADE\r\n" + 
					"  FROM usuario_ingrediente UI\r\n" + 
					" INNER JOIN ingrediente I\r\n" + 
					" 	ON I.id = UI.ingrediente_id\r\n" + 
					" INNER JOIN unidade_medida UM\r\n" + 
					"    ON UM.id = UI.unidade_medida_id\r\n" + 
					" WHERE UI.usuario_id = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				UnidadeMedida unidadeMedida = new UnidadeMedida();
				unidadeMedida.setId(rs.getInt("ID_UNIDADE_MEDIDA"));
				unidadeMedida.setSigla(rs.getString("SIGRA_UNIDADE_MEDIDA"));

				Ingrediente ingrediente = new Ingrediente();
				ingrediente.setId(rs.getInt("ID_INGREDIENTE"));
				ingrediente.setCodigoBarras(rs.getInt("CODIGO_BARRAS"));
				ingrediente.setNome(rs.getString("NOME_INGREDIENTE"));
				ingrediente.setQuantidade(rs.getInt("QUANTIDADE"));
				ingrediente.setUnidadeMedida(unidadeMedida);
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return ingredientes;
	}
}
