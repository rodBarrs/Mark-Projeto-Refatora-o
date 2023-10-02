package com.mycompany.newmark.banco.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.models.ChavesGrupoEtiquetas;
import com.mycompany.newmark.banco.ConnectionFactory;

public class BancosDAO {

	private static final String AVISO_BANCO_INSERIDO = "Banco inserido";
	private static final String AVISO_BANCO_EXISTENTE = "Banco já existente!";
	private static final String AVISO_ERRO_INSERCAO_BANCO = "Não foi possível inserir o banco";
	private static final String AVISO_BANCO_REMOVIDO = "Banco removido";
	private static final String AVISO_ERRO_REMOCAO_BANCO = "Não foi possível remover o banco";


	public List<ChavesGrupoEtiquetas> getTabelaBancoDeDados() {

		final String SQL = "SELECT * FROM bancos ORDER BY nome";

		List<ChavesGrupoEtiquetas> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ChavesGrupoEtiquetas key = new ChavesGrupoEtiquetas();
				key.setSigla(resultSet.getString("sigla"));
				key.setNome(resultSet.getString("nome"));
				key.setQntEtiquetas(getNumeroEtiquetasBanco(key.getSigla()));
				chaves.add(key);
			}

		} catch (SQLException erro) {
			mostrarAviso(erro.getMessage());
		}

		return chaves;
	}

	private Integer getNumeroEtiquetasBanco(String siglaBanco) {
		final String SQL = "SELECT COUNT(*) AS count FROM etiquetas WHERE banco = ?";
		Integer numeroEtiquetas = 0;

		try (Connection connection = new ConnectionFactory().obterConexao();
			 PreparedStatement stmt = connection.prepareStatement(SQL)) {

			stmt.setString(1, siglaBanco);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				numeroEtiquetas = rs.getInt("count");
			}
		} catch (SQLException erro) {
			mostrarAviso(erro.getMessage());
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
			mostrarAviso(AVISO_BANCO_INSERIDO);
		} catch (SQLException e) {
			if (e.getMessage().contains("UNIQUE")) {
				mostrarAviso(AVISO_BANCO_EXISTENTE);
			} else {
				mostrarAviso(AVISO_ERRO_INSERCAO_BANCO + "\n" + e.getMessage());
			}
		}
	}
	public void removerBanco(String sigla) {
		final String SQL = "DELETE FROM bancos WHERE sigla = ?";

		try (Connection connection = new ConnectionFactory().obterConexao();
			 PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, sigla);
			stmt.execute();
			mostrarAviso(AVISO_BANCO_REMOVIDO);
		} catch (SQLException e) {
			mostrarAviso(AVISO_ERRO_REMOCAO_BANCO + "\n" + e.getMessage());
		}
	}

	private void mostrarAviso(String mensagem) {
		new ControllerAviso().exibir(mensagem);
	}
}
