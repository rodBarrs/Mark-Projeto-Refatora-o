package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.models.Chaves_Condicao;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class IdentificadorPeticaoInicialDAO {
	public List<Chaves_Condicao> getTabelaIdentificadorPeticaoInicial() {
		
		List<Chaves_Condicao> listaIdentificadoresPeticaoInicial = new ArrayList<>();
		
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PET' ORDER BY texto";
		
		try(Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Chaves_Condicao chave = new Chaves_Condicao();
				chave.setTEXTO(rs.getString("texto"));
				listaIdentificadoresPeticaoInicial.add(chave);
			}
			
		} catch (Exception e) {
			new Aviso().aviso(e.getMessage());
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
			new Aviso().aviso("Item inserido");
		} catch (Exception e) {
			new Aviso().aviso("Item não inserido\n" + e.getMessage());
		}
		
	}

	public void removerIdentificadorPeticaoInicial(String texto) {
		
		final String SQL = "DELETE FROM condicao WHERE texto = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			new Aviso().aviso("Item removido");
		} catch (Exception e) {
			new Aviso().aviso("Item não removido\n" + e.getMessage());
		}
		
	}

	public List<Chaves_Condicao> buscarIdentificadorPeticao(String textoBusca) {
		
		List<Chaves_Condicao> chaves = new ArrayList<>();
		
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PET' AND texto LIKE ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, '%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Chaves_Condicao chave = new Chaves_Condicao();
				chave.setTEXTO(rs.getString("texto"));
				chaves.add(chave);
			}
		} catch (Exception e) {
			new Aviso().aviso(e.getMessage());
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
			new Aviso().aviso("Item atualizado");
		} catch (Exception e) {
			new Aviso().aviso("Item não atualizado\n" + e.getMessage());
		}
		
	}
}
