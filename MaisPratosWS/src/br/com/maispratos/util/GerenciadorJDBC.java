package br.com.maispratos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GerenciadorJDBC {
	// ---------------------------------------
	// CONSTANTES
	// ---------------------------------------
	private static String USER_NAME = "root";
	private static String PASSWORD = "";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static String URL = "jdbc:mysql://localhost/mais_pratos";

	// ---------------------------------------
	// CARREGA O DRIVER
	// ---------------------------------------

	// Carrega o driver do banco de dados
	static {
		// Indica o nome do driver que vai ser carregado
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("Erro ao carregar o driver!");
			e.printStackTrace();
		}
	}

	// ----------------------------------
	// METODOS
	// ----------------------------------

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
	}
	
	public static void close(Connection conn, PreparedStatement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection conn, 
			PreparedStatement stmt, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close(conn, stmt);
		
	}
	
}
