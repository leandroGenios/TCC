package br.com.maispratos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.maispratos.util.GerenciadorJDBC;
import br.com.meuspratos.model.UnidadeMedida;

public class UnidadeMedidaDAO {
	public List<UnidadeMedida> getUnidadesMedida() throws SQLException{
		List<UnidadeMedida> unidadesMedida = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT * FROM unidade_medida";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				UnidadeMedida unidadeMedida = new UnidadeMedida();
				unidadeMedida.setId(rs.getInt("ID"));
				unidadeMedida.setSigla(rs.getString("SIGLA"));
				
				unidadesMedida.add(unidadeMedida);
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return unidadesMedida;
	}
}
