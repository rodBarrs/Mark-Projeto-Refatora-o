package com.mycompany.newmark.banco.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.models.ChavesBanco;
import com.mycompany.newmark.banco.ConnectionFactory;

public class EtiquetaDAO {
	ControllerAviso controllerAviso = new ControllerAviso();

	public List<ChavesBanco> getTabelaEtiqueta(String banco) {
		final String SQL = "SELECT * FROM etiquetas WHERE banco = ? ORDER BY id";

		List<ChavesBanco> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
			 PreparedStatement stmt = connection.prepareStatement(SQL)) {

			stmt.setString(1, banco);
			ResultSet rs = stmt.executeQuery();
			chaves = extrairChavesEtiqueta(rs);
		} catch (SQLException e) {
			controllerAviso.exibir(e.getMessage());
		}

		return chaves;
	}

	public List<ChavesBanco> buscaEtiqueta(String textoEtiqueta) {
		final String SQL = "SELECT * FROM etiquetas WHERE palavrachave LIKE ? OR complemento LIKE ? OR etiqueta LIKE ?";
		List<ChavesBanco> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
			 PreparedStatement stmt = connection.prepareStatement(SQL)) {

			String likePattern = '%' + textoEtiqueta + '%';
			stmt.setString(1, likePattern);
			stmt.setString(2, likePattern);
			stmt.setString(3, likePattern);

			ResultSet rs = stmt.executeQuery();
			chaves = extrairChavesEtiqueta(rs);

		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}

		return chaves;

	}

	public List<ChavesBanco> buscaEtiquetaPorID(String id) {
		final String SQL = "SELECT * FROM etiquetas WHERE id = ?";
		List<ChavesBanco> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
			 PreparedStatement stmt = connection.prepareStatement(SQL)) {

			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			chaves = extrairChavesEtiqueta(rs);
		}catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}

		return chaves;
	}

	public void removerEtiqueta(Integer id) {
		final String SQL = "DELETE FROM etiquetas WHERE id = ?";
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
			controllerAviso.exibir("Item removido");
		} catch (Exception e) {
			controllerAviso.exibir("Não foi possível remover o item\n" + e.getMessage());
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
			controllerAviso.exibir("Item inserido");
		} catch (Exception e) {
			if (e.getMessage().contains("UNIQUE")) {
				controllerAviso.exibir("Item já existente!");
			} else {
				controllerAviso.exibir("Item não inserido" + e.getMessage());
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
			controllerAviso.exibir("Item atualizado");
		} catch (Exception e) {
			controllerAviso.exibir("Item não atualizado\n" + e.getMessage());
		}

	}

	private List<ChavesBanco> extrairChavesEtiqueta(ResultSet rs) throws SQLException {
		List<ChavesBanco> chaves = new ArrayList<>();
		while (rs.next()) {
			ChavesBanco chave = new ChavesBanco();
			chave.setID(rs.getInt("id"));
			chave.setPALAVRACHAVE(rs.getString("palavrachave"));
			chave.setCOMPLEMENTO(rs.getString("complemento"));
			chave.setETIQUETA(rs.getString("etiqueta"));
			chave.setTIPO(rs.getString("tipo"));
			chave.setPRIORIDADE(rs.getString("prioridade"));

			chaves.add(chave);
		}
		return chaves;
	}


}
