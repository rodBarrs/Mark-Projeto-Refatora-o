package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Chaves_GrupoEtiquetas;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class BancosDAO {

	public List<Chaves_GrupoEtiquetas> getTabelaBancoDeDados() {

		final String SQL = "SELECT * FROM bancos ORDER BY nome";

		List<Chaves_GrupoEtiquetas> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Chaves_GrupoEtiquetas key = new Chaves_GrupoEtiquetas();
				key.setSigla(resultSet.getString("sigla"));
				key.setNome(resultSet.getString("nome"));
				key.setQntEtiquetas(getNumeroEtiquetasBanco(key.getSigla()));
				chaves.add(key);
			}

		} catch (SQLException erro) {
			Aviso aviso = new Aviso();
			String textoAviso = "" + erro.getMessage();
			aviso.aviso(textoAviso);
		}

		return chaves;
	}

	private Integer getNumeroEtiquetasBanco(String siglaBanco) {
		final String SQL = "SELECT * FROM etiquetas WHERE banco = '" + siglaBanco + "'";
		Integer numeroEtiquetas = 0;
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				numeroEtiquetas++;
			}
		} catch (Exception e) {
			Aviso aviso = new Aviso();
			String textoAviso = "" + e.getMessage();
			aviso.aviso(textoAviso);
		}
		return numeroEtiquetas;
	}

	public void inserirBanco(String sigla, String banco) {
		final String SQL = "INSERT INTO bancos (sigla, nome) VALUES (?, ?)";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, sigla);
			stmt.setString(2, banco);
			stmt.execute();
			new Aviso().aviso("Banco inserido");
		} catch (Exception e) {
			if (e.getMessage().contains("UNIQUE")) {
				new Aviso().aviso("Banco já existente!");
			} else {
				new Aviso().aviso("Não foi possível inserir o banco\n" + e.getMessage());
			}
		}

	}

	public void removerBanco(String sigla) {
		final String SQL = "DELETE FROM bancos WHERE sigla = ?";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, sigla);
			stmt.execute();
			new Aviso().aviso("Banco removido");
		} catch (Exception e) {
			new Aviso().aviso("Não foi possível remover o banco\n" + e.getMessage());
		}

	}
}
