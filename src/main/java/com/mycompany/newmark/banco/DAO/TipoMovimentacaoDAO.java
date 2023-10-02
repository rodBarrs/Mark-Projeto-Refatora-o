package com.mycompany.newmark.banco.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.models.ChavesCondicao;
import com.mycompany.newmark.banco.ConnectionFactory;
import com.mycompany.newmark.models.ChavesPastaCondicao;

public class TipoMovimentacaoDAO {
	ControllerAviso controllerAviso = new ControllerAviso();

	public List<ChavesCondicao> getTabelaTipoMovimentacao() {
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PRO' ORDER BY texto";
		
		List<ChavesCondicao> chaves = new ArrayList<>();
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ChavesCondicao key = new ChavesCondicao();
				key.setTIPO("PRO");
				key.setTEXTO(resultSet.getString("texto"));
				key.setBANCO(resultSet.getString("banco"));
				chaves.add(key);
			}
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		return chaves;
	}

	public List<ChavesCondicao> buscarTipoMovimentacao(String textoBusca) {
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PRO' AND texto LIKE ?";
		
		List<ChavesCondicao> chaves = new ArrayList<>();
		
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, '%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ChavesCondicao chave = new ChavesCondicao();
				chave.setTEXTO(rs.getString("texto"));
				chaves.add(chave);
			}
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		
		return chaves;
	}

	public void removerTipoMovimentacao(String texto) {
		final String SQL = "DELETE FROM condicao WHERE tipo = 'PRO' AND texto = ?";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			controllerAviso.exibir("Item removido");
		} catch (Exception e) {
			controllerAviso.exibir("Item não removido\n" + e.getMessage());
		}
		
	}

	public List<ChavesPastaCondicao> getTabelaPastas(){
		final String SQL = "SELECT * FROM nome_Pasta";

		List<ChavesPastaCondicao> chavesPasta = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
			 PreparedStatement stmt = connection.prepareStatement(SQL)){
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ChavesPastaCondicao key = new ChavesPastaCondicao();
				key.setId(resultSet.getInt("id"));
				key.setNome(resultSet.getString("nome"));
				chavesPasta.add((key));
			}
		} catch (Exception e) {
			controllerAviso.exibir("Item não removido\n" + e.getMessage());
		}
		return chavesPasta;
	}

	public void inserirTipoMovimentacao(String texto, String banco) {
		final String SQL = "INSERT INTO condicao (texto, tipo, banco) VALUES (?, 'PRO', ?)";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.setString(2, banco);
			stmt.execute();
			controllerAviso.exibir("Item inserido");
		} catch (Exception e) {
			controllerAviso.exibir("Item não inserido\n" + e.getMessage());
		}
		
	}
	public List <ChavesPastaCondicao> buscarPasta (String textoBusca) {
		final String SQL = "SELECT * FROM nome_Pasta WHERE nome LIkE ? ";

		List<ChavesPastaCondicao> chavesPastas = new ArrayList<>();

		try(Connection connection = new ConnectionFactory().obterConexao();
			PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1,'%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ChavesPastaCondicao chavePasta = new ChavesPastaCondicao();
				chavePasta.setNome(rs.getString("nome"));
				chavesPastas.add(chavePasta);
			}
		} catch (Exception e){
			controllerAviso.exibir("Item não inserido\n" + e.getMessage());
		}
		return chavesPastas;
	}
	public void removerPasta(String texto) {
		final String SQL = "DELETE FROM nome_Pasta WHERE nome = ?";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			controllerAviso.exibir("Item removido");
		} catch (Exception e) {
			controllerAviso.exibir("Item não inserido\n" + e.getMessage());
		}

	}
	public void inserirPasta(String texto) {
		final String SQL = "INSERT INTO nome_Pasta (nome) VALUES (?)";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			controllerAviso.exibir("Item inserido");
		} catch (Exception e) {
			controllerAviso.exibir("Item não inserido\n" + e.getMessage());
		}

	}

	public void atualizarTipoMovimento(String texto, String novoTexto) {
		final String SQL = "UPDATE condicao SET texto = ? WHERE texto = ?";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, novoTexto);
			stmt.setString(2, texto);
			stmt.execute();
			controllerAviso.exibir("Item atualizado");
		} catch (Exception e) {
			controllerAviso.exibir("Item não atualizado\n" + e.getMessage());
		}
		
	}

}
