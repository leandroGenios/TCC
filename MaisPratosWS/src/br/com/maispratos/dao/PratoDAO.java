package br.com.maispratos.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.maispratos.util.GerenciadorJDBC;
import br.com.meuspratos.model.Ingrediente;
import br.com.meuspratos.model.Prato;
import br.com.meuspratos.model.UnidadeMedida;
import br.com.meuspratos.model.Usuario;

public class PratoDAO {
	private IngredienteDAO ingredienteDAO = new IngredienteDAO();
	
	
	public boolean setPrato(Usuario usuario) throws Exception{
		usuario.getPrato().setIngredientes(verificarIngredientesExistem(usuario.getPrato().getIngredientes()));
		usuario.setPrato(addPrato(usuario.getPrato()));
		addIngredientesPrato(usuario.getPrato());
		vincularPratoUsuario(usuario);
		return true;
	}
	
	private List<Ingrediente> verificarIngredientesExistem(List<Ingrediente> ingredientes) throws SQLException {
		for (Ingrediente ingrediente : ingredientes) {
			Ingrediente ingredienteBase = null;
			ingredienteBase = ingredienteDAO.getIngredienteByCodigoBarras(ingrediente.getCodigoBarras());
			if(ingredienteBase == null){
				ingrediente = ingredienteDAO.setIngrediente(ingrediente);
			}else {
				ingrediente.setId(ingredienteBase.getId());
			}
		}
		
		return ingredientes;
	}
	
	private Prato addPrato(Prato prato) throws Exception{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO prato (nome, modo_preparo, tempo_preparo, imagem) VALUES (?,?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, prato.getNome());
			stmt.setString(2, prato.getModoPreparo());
			stmt.setInt(3, prato.getTempoPreparo());
			
			InputStream myInputStream = new ByteArrayInputStream(prato.getImagem()); 

			stmt.setBinaryStream(4, myInputStream);
			
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			prato.setId(rs.getInt(1));
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return prato;
	}
	
	private boolean addIngredientesPrato(Prato prato) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		for (Ingrediente ingrediente : prato.getIngredientes()) {
			try {
				conn = GerenciadorJDBC.getConnection();
				
				String sql = "INSERT INTO prato_ingrediente (prato_id, ingrediente_id, unidade_medida_id, quantidade) VALUES (?,?,?,?)";
				stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				stmt.setInt(1, prato.getId());
				stmt.setInt(2, ingrediente.getId());
				stmt.setInt(3, ingrediente.getUnidadeMedida().getId());
				stmt.setFloat(4, ingrediente.getQuantidade());
				
				stmt.executeUpdate();
			}
			finally {
				GerenciadorJDBC.close(conn, stmt);
			}
		}
		return true;
	}
	
	private boolean vincularPratoUsuario(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO prato_usuario (prato_id, usuario_id) VALUES (?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getId());
			stmt.setInt(2, usuario.getId());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
	
	public List<Prato> listPratos(List<Ingrediente> ingredientes) throws SQLException{
		List<Prato> pratos = new ArrayList<Prato>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT P.id, "
					   + "		 P.nome NOME_PRATO, "
					   + "		 P.modo_preparo, "
					   + "		 P.tempo_preparo,"
					   + "       P.imagem, "
					   + "		 I.codigo_barras, "
					   + "		 I.nome, "
					   + "		 PI.quantidade, "
					   + "		 U.sigla "
					   + "	FROM prato P "
					   + " INNER JOIN prato_ingrediente PI "
					   + "	  ON PI.prato_id = P.id "
					   + " INNER JOIN ingrediente I "
					   + "	  ON I.id = PI.ingrediente_id "
					   + " INNER JOIN unidade_medida U "
					   + "	  ON U.id = PI.unidade_medida_id "
					   + " WHERE TRUE";
			for (Ingrediente ingrediente : ingredientes) {
				sql += "	  OR PI.ingrediente_id = " + ingrediente.getId();
			}
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = stmt.executeQuery();
			int pratoId = 0;
			Prato prato = null;
			List<Ingrediente> ingredientesPrato;
			while (rs.next()) {
				if(pratoId != rs.getInt("ID")) {
					pratoId = rs.getInt("ID");
					prato = new Prato();
					prato.setId(rs.getInt("ID"));
					prato.setNome(rs.getString("NOME_PRATO"));
					prato.setModoPreparo(rs.getString("MODO_PREPARO"));
					prato.setTempoPreparo(rs.getInt("TEMPO_PREPARO"));
					prato.setImagem(rs.getBytes("IMAGEM"));
					ingredientesPrato = new ArrayList<>();
					prato.setIngredientes(ingredientesPrato);
					pratos.add(prato);
				}
				
				Ingrediente ingrediente = new Ingrediente();
				ingrediente.setCodigoBarras(rs.getDouble("CODIGO_BARRAS"));
				ingrediente.setNome(rs.getString("NOME"));
				ingrediente.setQuantidade(rs.getFloat("QUANTIDADE"));
				UnidadeMedida unidadeMedida = new UnidadeMedida();
				unidadeMedida.setSigla(rs.getString("SIGLA"));
				ingrediente.setUnidadeMedida(unidadeMedida);
				
				prato.getIngredientes().add(ingrediente);
			}
			
			for (Prato p : pratos) {
				for (Ingrediente ingredientePrato : p.getIngredientes()) {
					for (Ingrediente ingrediente : ingredientes) {
						if(ingrediente.getNome().toUpperCase().contains(ingredientePrato.getNome().toUpperCase()) || ingredientePrato.getNome().toUpperCase().contains(ingrediente.getNome().toUpperCase())) {
							p.setIngredientesCompativeis(p.getIngredientesCompativeis() + 1);
						}
					}
				}
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return pratos;
	}
}
