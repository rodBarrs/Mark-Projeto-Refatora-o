package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Chaves_Banco;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class EtiquetaDAO {

	public List<Chaves_Banco> getTabelaEtiqueta(String banco) {
		final String SQL = "SELECT * FROM etiquetas WHERE banco = '" + banco + "' ORDER BY id";

		List<Chaves_Banco> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Chaves_Banco chave = new Chaves_Banco();
				chave.setID(rs.getInt("ID"));
				chave.setPALAVRACHAVE(rs.getString("palavrachave"));
				chave.setCOMPLEMENTO(rs.getString("complemento"));
				chave.setETIQUETA(rs.getString("etiqueta"));
				chave.setTIPO(rs.getString("tipo"));
				chave.setPRIORIDADE(rs.getString("prioridade"));

				chaves.add(chave);
			}
		} catch (Exception e) {
			new Aviso().aviso(e.getMessage());
		}

		return chaves;

	}

	public List<Chaves_Banco> buscaEtiqueta(String textoEtiqueta) {
		final String SQL = "SELECT * FROM etiquetas WHERE palavrachave LIKE ? OR complemento LIKE ? OR etiqueta LIKE ?";
		List<Chaves_Banco> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, '%' + textoEtiqueta + '%');
			stmt.setString(2, '%' + textoEtiqueta + '%');
			stmt.setString(3, '%' + textoEtiqueta + '%');
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Chaves_Banco chave = new Chaves_Banco();
				chave.setID(rs.getInt("id"));
				chave.setPALAVRACHAVE(rs.getString("palavrachave"));
				chave.setCOMPLEMENTO(rs.getString("complemento"));
				chave.setETIQUETA(rs.getString("etiqueta"));
				chave.setTIPO(rs.getString("tipo"));
				chave.setPRIORIDADE(rs.getString("prioridade"));

				chaves.add(chave);
			}
		} catch (Exception e) {
			new Aviso().aviso(e.getMessage());
		}

		return chaves;

	}

	public List<Chaves_Banco> buscaEtiquetaPorID(String id) {
		final String SQL = "SELECT * FROM etiquetas WHERE id = ?";
		List<Chaves_Banco> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Chaves_Banco chave = new Chaves_Banco();
				chave.setID(rs.getInt("id"));
				chave.setPALAVRACHAVE(rs.getString("palavrachave"));
				chave.setCOMPLEMENTO(rs.getString("complemento"));
				chave.setETIQUETA(rs.getString("etiqueta"));
				chave.setTIPO(rs.getString("tipo"));
				chave.setPRIORIDADE(rs.getString("prioridade"));

				chaves.add(chave);
			}
		} catch (Exception e) {
			new Aviso().aviso(e.getMessage());
		}

		return chaves;
	}

	public void removerEtiqueta(Integer id) {
		final String SQL = "DELETE FROM etiquetas WHERE id = ?";
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
			new Aviso().aviso("Item removido");
		} catch (Exception e) {
			new Aviso().aviso("Não foi possível remover o item\n" + e.getMessage());
		}

	}

	public void inserirEtiqueta(String palavraChave, String complemento, String etiqueta, String tipo, String peso,
			String banco) {
		final String SQL = "INSERT INTO etiquetas (palavrachave, complemento, etiqueta, tipo, prioridade, banco) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, palavraChave);
			stmt.setString(2, complemento);
			stmt.setString(3, etiqueta);
			stmt.setString(4, tipo);
			stmt.setString(5, peso);
			stmt.setString(6, banco);
			stmt.execute();
			new Aviso().aviso("Item inserido");
		} catch (Exception e) {
			if (e.getMessage().contains("UNIQUE")) {
				new Aviso().aviso("Item já existente!");
			} else {
				new Aviso().aviso("Item não inserido" + e.getMessage());
			}
		}

	}

	public void atualizarEtiqueta(Integer id, String palavraChave, String complemento, String etiqueta, String peso,
			String tipo) {
		final String SQL = "UPDATE etiquetas SET palavrachave = ?, complemento = ?, etiqueta = ?, prioridade = ?, tipo = ? WHERE id = ?";
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, palavraChave);
			stmt.setString(2, complemento);
			stmt.setString(3, etiqueta);
			stmt.setString(4, peso);
			stmt.setString(5, tipo);
			stmt.setInt(6, id);
			stmt.execute();
			new Aviso().aviso("Item atualizado");
		} catch (Exception e) {
			new Aviso().aviso("Item não atualizado\n" + e.getMessage());
		}

	}

}
