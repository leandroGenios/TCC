package br.com.maispratos.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.maispratos.util.GerenciadorJDBC;
import br.com.meuspratos.model.Classificacao;
import br.com.meuspratos.model.Comentario;
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
				
				System.out.println(ingrediente.getId());
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
	
	public List<Prato> listPratos(List<Ingrediente> ingredientes, int idUsuario, String where) throws SQLException{
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
			
			String sql = "SELECT P.id,\r\n" + 
					"		P.nome NOME_PRATO,\r\n" + 
					"		P.modo_preparo,\r\n" + 
					"		P.tempo_preparo,\r\n" + 
					"		P.imagem,\r\n" + 
					"		I.ID INGREDIENTE_ID,\r\n" + 
					"		I.codigo_barras,\r\n" + 
					"		I.nome,\r\n" + 
					"		FIND_IN_SET(I.NOME,'" + ingredientesNome + "') COMPATIVEL,\r\n" + 
					"		PI.quantidade,\r\n" + 
					"		UM.sigla,\r\n" + 
					"		UM.id UNIDADE_MEDIDA_ID,\r\n" + 
					"		U.nome NOME_USUARIO,\r\n" + 
					"		U.id usuario_id,\r\n" + 
					"		C.descricao,\r\n" + 
					"		PA.avaliacao,\r\n" + 
					"		PA2.avaliacao,\r\n" + 
					"		AVG(PA2.avaliacao) AS media,\r\n" +
					"		(SELECT inicio_preparo\r\n"+
					"		   FROM prato_preparo\r\n"+
					"		  WHERE prato_id = P.id\r\n"+
					"		  ORDER BY inicio_preparo DESC\r\n"+
					"		  LIMIT 1)inicio_preparo,\r\n" + 
					"		(SELECT preparo_sem_ingredientes \r\n" +
					"		   FROM prato_preparo \r\n" +
					"		  WHERE prato_id = P.id \r\n" +
					"		  ORDER BY inicio_preparo DESC \r\n" +
					"		  LIMIT 1)preparo_sem_ingredientes, \r\n" +
					"		 PF.favorito \r\n" +
					"   FROM prato P\r\n" + 
					"  INNER JOIN prato_ingrediente PI\r\n" + 
					"     ON PI.prato_id = P.id\r\n" + 
					"  INNER JOIN ingrediente I\r\n" + 
					"	  ON I.id = PI.ingrediente_id\r\n" + 
					"  INNER JOIN unidade_medida UM\r\n" + 
					"	  ON UM.id = PI.unidade_medida_id\r\n" + 
					"  INNER JOIN prato_usuario PU\r\n" + 
					"	  ON PU.prato_id = P.id\r\n" + 
					"  INNER JOIN usuario U\r\n" + 
					"	  ON U.id = PU.usuario_id\r\n" + 
					"  INNER JOIN usuario_classificacao UC\r\n" + 
					"	  ON UC.usuario_id = U.id\r\n" + 
					"  INNER JOIN classificacao C\r\n" + 
					"	  ON C.id = UC.classificacao_id\r\n" + 
					"   LEFT JOIN prato_avaliacao PA\r\n" + 
					"     ON PA.prato_id = P.ID \r\n" + 
					"	 AND PA.usuario_id = ?\r\n" + 
					"   LEFT JOIN prato_avaliacao PA2\r\n" + 
					"     ON PA2.prato_id = P.ID \r\n" + 
					"	LEFT JOIN prato_favorito PF \r\n" +
				    "	  ON PF.prato_id = P.id \r\n" +
				    "	 AND PF.usuario_id = ? \r\n" +
					"  WHERE TRUE\r\n";
			sql +=  where;
			sql +=  "  GROUP BY P.nome, I.nome, PA.avaliacao\r\n" + 
					"  ORDER BY P.nome";
			
			System.out.println(sql);
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idUsuario);
			
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
					usuario.setId(rs.getInt("USUARIO_ID"));
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
					prato.setUltimoPreparo(rs.getLong("inicio_preparo"));
					prato.setPreparadoSemIngredientes(rs.getBoolean("PREPARO_SEM_INGREDIENTES"));
					prato.setFavorito(rs.getString("FAVORITO") == null ? null : rs.getBoolean("FAVORITO"));
					
					prato.setIngredientesCompativeisString("0");
					
					pratos.add(prato);
				}
				
				Ingrediente ingrediente = new Ingrediente();
				ingrediente.setId(rs.getInt("INGREDIENTE_ID"));
				ingrediente.setCodigoBarras(rs.getDouble("CODIGO_BARRAS"));
				ingrediente.setNome(rs.getString("NOME"));
				ingrediente.setQuantidade(rs.getFloat("QUANTIDADE"));
				UnidadeMedida unidadeMedida = new UnidadeMedida();
				unidadeMedida.setSigla(rs.getString("SIGLA"));
				unidadeMedida.setId(rs.getInt("UNIDADE_MEDIDA_ID"));
				ingrediente.setUnidadeMedida(unidadeMedida);
				
				prato.getIngredientes().add(ingrediente);
				if(rs.getInt("COMPATIVEL") > 0) {
					prato.setIngredientesCompativeis(prato.getIngredientesCompativeis() + 1);
					prato.setIngredientesCompativeisString("" + prato.getIngredientesCompativeis());
				}
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		Comparator<Prato> compareByConpativeis = (Prato o1, Prato o2) -> o1.getIngredientesCompativeisString().compareTo( o2.getIngredientesCompativeisString());
		
		Collections.sort(pratos, compareByConpativeis.reversed());
		return pratos;
	}
	
	public List<Prato> listMeusPratos(List<Ingrediente> ingredientes, int idUsuario) throws SQLException{
		String where = "  AND PU.USUARIO_ID = " + idUsuario;
		return listPratos(ingredientes, idUsuario, where);
	}
	
	public List<Prato> listMeusFavoritos(List<Ingrediente> ingredientes, int idUsuario) throws SQLException{
		String where = "  AND PF.FAVORITO = true";
		return listPratos(ingredientes, idUsuario, where);
	}
	
	public List<Prato> listPratosPreparados(List<Ingrediente> ingredientes, int idUsuario) throws SQLException{
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
			
			String sql = "SELECT P.id,\r\n" +
						 "		 P.nome NOME_PRATO, \r\n" +
					     "		 P.modo_preparo, \r\n" +
					     "		 P.tempo_preparo, \r\n" +
					     "		 P.imagem, \r\n" +
					     "		 I.codigo_barras, \r\n" +
					     "		 I.nome, \r\n" +
					     "		 FIND_IN_SET(I.NOME,'" + ingredientesNome + "') COMPATIVEL, \r\n" +
					     "		 PI.quantidade, \r\n" +
					     "		 UM.sigla, \r\n" +
					     "		 UM.id UNIDADE_MEDIDA_ID,\r\n" +
					     "		 U.nome NOME_USUARIO, \r\n" +
					     "		 U.id usuario_id,\r\n" + 
					     "		 C.descricao, \r\n" +
					     "		 PA.avaliacao, \r\n" +
					     "		 pp.inicio_preparo, \r\n" +
					     "		 pp.preparo_sem_ingredientes, \r\n" +
					     "		 (select AVG(PA2.avaliacao) AS media from prato_avaliacao PA2 where pa2.prato_id = P.id AND PA2.usuario_id = U.id) MEDIA, \r\n" + 
					     "		 PF.favorito \r\n" +
					     "	FROM prato_preparo PP \r\n" +
					     " INNER JOIN prato P \r\n" +
					     "	  ON P.id = PP.prato_id \r\n" +
					     " INNER JOIN prato_ingrediente PI \r\n" +
					     "	  ON PI.prato_id =P.id \r\n" +
					     " INNER JOIN ingrediente I \r\n" +
					     "    ON I.id = PI.ingrediente_id \r\n" +
					     " INNER JOIN unidade_medida UM \r\n" +
					     "	  ON UM.id = PI.unidade_medida_id \r\n" +
					     " INNER JOIN prato_usuario PU \r\n" +
					     " ON PU.prato_id = P.id \r\n" +
					     " INNER JOIN usuario U \r\n" +
					     "	  ON U.id = PU.usuario_id \r\n" +
					     " INNER JOIN usuario_classificacao UC \r\n" +
					     "    ON UC.usuario_id = U.id \r\n" +
					     " INNER JOIN classificacao C \r\n" +
					     "	  ON C.id = UC.classificacao_id \r\n" +
					     "  LEFT JOIN prato_avaliacao PA \r\n" +
					     "    ON PA.prato_id = P.ID \r\n" + 
						 "	LEFT JOIN prato_favorito PF \r\n" +
					     "	  ON PF.prato_id = P.id \r\n" +
					     "	 AND PF.usuario_id = ? \r\n" +
					     " WHERE PP.usuario_id = ? \r\n" +
					     " ORDER BY pp.inicio_preparo DESC \r\n";
			
			System.out.println(sql);
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idUsuario);
			
			ResultSet rs = stmt.executeQuery();
			long ultimoPreparo = 0;
			Prato prato = null;
			List<Ingrediente> ingredientesPrato;
			while (rs.next()) {
				if(ultimoPreparo != rs.getLong("inicio_preparo")) {
					ultimoPreparo = rs.getLong("inicio_preparo");
					prato = new Prato();
					prato.setId(rs.getInt("ID"));
					prato.setNome(rs.getString("NOME_PRATO"));
					prato.setModoPreparo(rs.getString("MODO_PREPARO"));
					prato.setTempoPreparo(rs.getInt("TEMPO_PREPARO"));
					
					Usuario usuario = new Usuario();
					usuario.setId(rs.getInt("USUARIO_ID"));
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
					prato.setAvaliacao(rs.getInt("AVALIACAO"));
					prato.setNota(rs.getInt("MEDIA"));
					prato.setUltimoPreparo(rs.getLong("inicio_preparo"));
					prato.setPreparadoSemIngredientes(rs.getBoolean("PREPARO_SEM_INGREDIENTES"));
					prato.setFavorito(rs.getBoolean("FAVORITO"));
					
					pratos.add(prato);
				}
				
				Ingrediente ingrediente = new Ingrediente();
				ingrediente.setCodigoBarras(rs.getDouble("CODIGO_BARRAS"));
				ingrediente.setNome(rs.getString("NOME"));
				ingrediente.setQuantidade(rs.getFloat("QUANTIDADE"));
				UnidadeMedida unidadeMedida = new UnidadeMedida();
				unidadeMedida.setSigla(rs.getString("SIGLA"));
				unidadeMedida.setId(rs.getInt("UNIDADE_MEDIDA_ID"));
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
			
			String sql = "INSERT INTO prato_preparo (prato_id, usuario_id, inicio_preparo, preparo_sem_ingredientes) values (?,?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getId());
			stmt.setInt(2, usuario.getId());
			stmt.setLong(3, usuario.getPrato().getHoraPreparo());
			stmt.setBoolean(4, usuario.getPrato().isPreparadoSemIngredientes());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
	
	public boolean setComentario(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO comentario (prato_id, usuario_id, descricao) values (?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getId());
			stmt.setInt(2, usuario.getId());
			stmt.setString(3, usuario.getPrato().getComentario());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}
	
	public List<Comentario> listComentarios(int pratoId) throws SQLException{
		List<Comentario> comentarios = new ArrayList<Comentario>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "SELECT C.ID, C.DESCRICAO, U.NOME FROM comentario C INNER JOIN USUARIO U ON U.ID = USUARIO_ID WHERE prato_id = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, pratoId);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				Comentario comentario = new Comentario();
				comentario.setId(rs.getInt("ID"));
				comentario.setTexto(rs.getString("DESCRICAO"));
				
				Usuario usuario = new Usuario();
				usuario.setNome(rs.getString("NOME"));
				comentario.setUsuario(usuario);
				
				comentarios.add(comentario);
			}
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return comentarios;
	}

	public boolean removerPreparo(int idUsuario, int idPrato, long data) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "DELETE FROM prato_preparo "
					   + " WHERE usuario_id = ? "
					   + "   AND prato_id = ?"
					   + "   AND inicio_preparo = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idPrato);
			stmt.setLong(3, data);
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}

	public boolean setFavorito(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "INSERT INTO prato_favorito (prato_id, usuario_id, favorito) values (?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, usuario.getPrato().getId());
			stmt.setInt(2, usuario.getId());
			stmt.setBoolean(3, true);
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		return true;
	}

	public boolean updateFavorito(Usuario usuario) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "UPDATE prato_favorito SET FAVORITO = ? WHERE prato_id = ? AND usuario_id = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setBoolean(1, !usuario.getPrato().getFavorito());
			stmt.setInt(2, usuario.getPrato().getId());
			stmt.setInt(3, usuario.getId());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return !usuario.getPrato().getFavorito();
	}

	public Object updatePrato(Usuario usuario) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = GerenciadorJDBC.getConnection();
			
			String sql = "UPDATE prato SET nome = ?, modo_preparo = ?, tempo_preparo = ?, imagem = ? WHERE ID = ?";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, usuario.getPrato().getNome());
			stmt.setString(2, usuario.getPrato().getModoPreparo());
			stmt.setInt(3, usuario.getPrato().getTempoPreparo());
			
			if(usuario.getPrato().getImagem() != null){
				InputStream myInputStream = new ByteArrayInputStream(usuario.getPrato().getImagem()); 
				stmt.setBinaryStream(4, myInputStream);				
			}else{
				stmt.setNull(4, Types.BLOB);
			}
			
			stmt.setInt(5, usuario.getPrato().getId());
			
			stmt.executeUpdate();
		}
		finally {
			GerenciadorJDBC.close(conn, stmt);
		}
		
		IngredienteDAO ingDao = new IngredienteDAO();
		System.out.println(usuario.getPrato().getId());
		if(ingDao.deleteIngredienteByPrato(usuario.getPrato().getId())){
			addIngredientesPrato(usuario.getPrato());
		}
		return true;
	}
	
	public Integer countPratosAvaliados(int idUsuario) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		int count = 0;
		try {
			conn = GerenciadorJDBC.getConnection();
			String sql = "SELECT count(p.id) qtde "
					   + "  FROM prato p"
					   + " inner join prato_avaliacao pa"
					   + "    on pa.usuario_id = ?"
					   + "   and pa.prato_id = p.id";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt("QTDE");
			}
		}finally{
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return count;
	}
	
	public Integer countMeusPratos(int idUsuario) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		int count = 0;
		try {
			conn = GerenciadorJDBC.getConnection();
			String sql = "SELECT count(pu.prato_id) qtde "
					   + "  FROM prato_usuario pu"
					   + " where pu.usuario_id = ?";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt("QTDE");
			}
		}finally{
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return count;
	}
	
	public List<Prato> listPratosBoaAvaliacao(int idUsuario) throws SQLException {
		List<Prato> pratos = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = GerenciadorJDBC.getConnection();
			String sql = "SELECT *, "
					   + "		 AVG(PA.avaliacao) AS media"
					   + "	FROM prato_usuario PU"
					   + " INNER JOIN prato_avaliacao PA"
					   + "	  ON PA.prato_id = PU.prato_id"
					   + " WHERE PU.usuario_id = ?"
					   + " GROUP BY PU.prato_id "
					   + "HAVING AVG(PA.avaliacao) > 3";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, idUsuario);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Prato prato = new Prato();
				prato.setId(rs.getInt("PRATO_ID"));
				prato.setAvaliacao(rs.getInt("MEDIA"));
				
				pratos.add(prato);
			}
		}finally{
			GerenciadorJDBC.close(conn, stmt);
		}
		
		return pratos;
	}
}
