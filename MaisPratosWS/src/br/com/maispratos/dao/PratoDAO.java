package br.com.maispratos.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.maispratos.util.GerenciadorJDBC;
import br.com.meuspratos.model.Classificacao;
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
			
			if(ingrediente.getCodigoBarras() != 0)
				ingredienteBase = ingredienteDAO.getIngredienteByCodigoBarras(ingrediente.getCodigoBarras());
			else
				ingredienteBase = ingredienteDAO.getIngredienteByNome(ingrediente.getNome());
			
			if(ingredienteBase == null){
				ingrediente = ingredienteDAO.setIngrediente(ingrediente);
			}else{
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
			
			if(prato.getImagem() != null){
				InputStream myInputStream = new ByteArrayInputStream(prato.getImagem()); 
				stmt.setBinaryStream(4, myInputStream);				
			}else{
				stmt.setNull(4, Types.BLOB);
			}
			
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
	
	public List<Prato> listPratos(List<Ingrediente> ingredientes, int idUsuario) throws SQLException{
		List<Prato> pratos = new ArrayList<Prato>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			String ingredientesNome = "";
			for (Ingrediente ingrediente : ingredientes) {
				ingredientesNome += ingrediente.getNome();
				if(ingrediente != ingredientes.get(ingredientes.size() - 1)) {
					ingredientesNome += ",";
				}
			}
			
			String sql = "SELECT 	P.id,\r\n" + 
					"		P.nome NOME_PRATO,\r\n" + 
					"		P.modo_preparo,\r\n" + 
					"		P.tempo_preparo,\r\n" + 
					"		P.imagem,\r\n" + 
					"		I.codigo_barras,\r\n" + 
					"		I.nome,\r\n" + 
					"		FIND_IN_SET(I.NOME,'" + ingredientesNome + "') COMPATIVEL,\r\n" + 
					"		PI.quantidade,\r\n" + 
					"		UM.sigla,\r\n" + 
					"		U.nome NOME_USUARIO,\r\n" + 
					"		C.descricao,\r\n" + 
					"		PA.avaliacao,\r\n" + 
					"		PA2.avaliacao,\r\n" + 
					"		AVG(PA2.avaliacao) AS media\r\n" + 
					"   FROM prato P\r\n" + 
					"  INNER JOIN prato_ingrediente PI\r\n" + 
					"     ON PI.prato_id = P.id\r\n" + 
					"  INNER JOIN ingrediente I\r\n" + 
					"	 ON I.id = PI.ingrediente_id\r\n" + 
					"  INNER JOIN unidade_medida UM\r\n" + 
					"	 ON UM.id = PI.unidade_medida_id\r\n" + 
					"  INNER JOIN prato_usuario PU\r\n" + 
					"	 ON PU.prato_id = P.id\r\n" + 
					"  INNER JOIN usuario U\r\n" + 
					"	 ON U.id = PU.usuario_id\r\n" + 
					"  INNER JOIN usuario_classificacao UC\r\n" + 
					"	 ON UC.usuario_id = U.id\r\n" + 
					"  INNER JOIN classificacao C\r\n" + 
					"	 ON C.id = UC.classificacao_id\r\n" + 
					"  LEFT JOIN prato_avaliacao PA\r\n" + 
					"     ON PA.prato_id = P.ID \r\n" + 
					"	AND PA.usuario_id = ?\r\n" + 
					"   LEFT JOIN prato_avaliacao PA2\r\n" + 
					"     ON PA2.prato_id = P.ID \r\n" + 
					"  WHERE TRUE\r\n" + 
					"  GROUP BY I.nome, PA.avaliacao\r\n" + 
					"  ORDER BY P.nome";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			
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
					
					Usuario usuario = new Usuario();
					usuario.setNome(rs.getString("NOME_USUARIO"));
					
					Classificacao classificacao = new Classificacao();
					classificacao.setDescricao(rs.getString("DESCRICAO"));
					usuario.setClassificacao(classificacao);
					
					prato.setCriador(usuario);
					
					Blob blob = rs.getBlob("IMAGEM");
					if(blob != null){
						String base64String = Base64.getEncoder().encodeToString(blob.getBytes(1, (int)blob.length()));
						prato.setImagemBase64(base64String);						
					}
					ingredientesPrato = new ArrayList<>();
					prato.setIngredientes(ingredientesPrato);
					prato.setNota(rs.getInt("MEDIA"));
					prato.setAvaliacao(rs.getInt("AVALIACAO"));
					
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
				if(rs.getInt("COMPATIVEL") > 0) {
					prato.setIngredientesCompativeis(prato.getIngredientesCompativeis() + 1);
				}
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return pratos;
	}
	
	public boolean setAvaliacaoPrato(Usuario usuario) throws SQLException{
		if(getAvaliacaoPratoByUsuario(usuario)) {
			return updateAvaliacao(usuario);
		}else {
			return insertAvaliacao(usuario);
		}
	}
	
	public boolean getAvaliacaoPratoByUsuario(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean retorno = false;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT * FROM prato_avaliacao WHERE prato_id = ? and usuario_id = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getId());
			stmt.setInt(2, usuario.getId());
			
			rs = stmt.executeQuery();
			retorno = rs.next();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return retorno;
	}
	
	public boolean insertAvaliacao(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO prato_avaliacao (prato_id, usuario_id, avaliacao) values (?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getId());
			stmt.setInt(2, usuario.getId());
			stmt.setInt(3, usuario.getPrato().getAvaliacao());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
	
	public boolean updateAvaliacao(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "UPDATE prato_avaliacao SET avaliacao = ? WHERE PRATO_ID = ? AND USUARIO_ID = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getAvaliacao());
			stmt.setInt(2, usuario.getPrato().getId());
			stmt.setInt(3, usuario.getId());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
	
	public boolean setPreparo(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO prato_preparo (prato_id, usuario_id, inicio_preparo) values (?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getId());
			stmt.setInt(2, usuario.getId());
			stmt.setDate(3, (Date) usuario.getPrato().getHoraPreparo());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
}
