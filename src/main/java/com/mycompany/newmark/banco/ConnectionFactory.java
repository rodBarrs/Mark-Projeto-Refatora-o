package com.mycompany.newmark.connectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	
	private final String URL = "jdbc:sqlite:BancoEtiquetasMark.db";

	public Connection obterConexao() {
		try {
			return DriverManager.getConnection(URL);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
