package com.mycompany.newmark.banco.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.models.ChavesCondicao;
import com.mycompany.newmark.banco.ConnectionFactory;

public class IdentificadorPeticaoInicialDAO {
	ControllerAviso controllerAviso = new ControllerAviso();
	public List<ChavesCondicao> getTabelaIdentificadorPeticaoInicial() {

		
		List<ChavesCondicao> listaIdentificadoresPeticaoInicial = new ArrayList<>();
		
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PET' ORDER BY texto";
		
		try(Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet rs = stmt.executeQuery();
			listaIdentificadoresPeticaoInicial = extrairChavePeticaoInicial(rs);

		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		
		return listaIdentificadoresPeticaoInicial;
	}

	public void inserirIdentificadorPeticaoInicial(String identificadorPeticao) {
		
		final String SQL = "INSERT INTO condicao (texto, tipo) VALUES (?, ?)";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, identificadorPeticao);
			stmt.setString(2, "PET");
			stmt.execute();
			controllerAviso.exibir("Item inserido");
		} catch (Exception e) {
			new ControllerAviso().exibir("Item não inserido\n" + e.getMessage());
		}
		
	}

	public void removerIdentificadorPeticaoInicial(String texto) {
		
		final String SQL = "DELETE FROM condicao WHERE texto = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			controllerAviso.exibir("Item removido");
		} catch (Exception e) {
			controllerAviso.exibir("Item não removido\n" + e.getMessage());
		}
		
	}

	public List<ChavesCondicao> buscarIdentificadorPeticao(String textoBusca) {
		
		List<ChavesCondicao> chaves = new ArrayList<>();
		
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PET' AND texto LIKE ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, '%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			chaves = extrairChavePeticaoInicial (rs);
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		
		return chaves;
		
	}

	public void atualizarIdentificadorPeticao(String texto, String novoTexto) {
		
		final String SQL = "UPDATE condicao SET texto = ? WHERE texto = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, novoTexto);
			stmt.setString(2, texto);
			stmt.executeUpdate();
			controllerAviso.exibir("Item atualizado");
		} catch (Exception e) {
			controllerAviso.exibir("Item não atualizado\n" + e.getMessage());
		}
		
	}

	private static List<ChavesCondicao> extrairChavePeticaoInicial(ResultSet rs) throws SQLException {
		List<ChavesCondicao> listaIdentificadoresPeticaoInicial = new ArrayList<>();
		while(rs.next()) {
			ChavesCondicao chave = new ChavesCondicao();
			chave.setTEXTO(rs.getString("texto"));
			listaIdentificadoresPeticaoInicial.add(chave);
		}
		return listaIdentificadoresPeticaoInicial;
	}
}
