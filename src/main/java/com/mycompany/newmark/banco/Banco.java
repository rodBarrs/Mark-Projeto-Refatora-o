/**
 * @author Felipe Marques, João Paulo, Gabriel Ramos, Rafael Henrique
 * 
 * Classe responsável por identificar e, caso não exista, criar, o Banco de dados além de inserir, alterar e excluir os dados lá armazenados 
 */

package com.mycompany.newmark.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Banco {
	ConnectionFactory connectionFactory = new ConnectionFactory();
	public void conectar() {
		try {
			// Estabelece a conexão com o banco de dados
			Connection connection = connectionFactory.obterConexao();

			criarTabelas(connection);
			inserirConfiguracaoPadrao(connection);

			// Desconecta com o banco de dados, garantindo assim a integridade dos dados
			connection.close();
		} catch (SQLException erro) {
			erro.printStackTrace();
		}
	}
	private void criarTabelas(Connection connection) throws SQLException {
		String[] createTableSQLs = {
				"CREATE TABLE IF NOT EXISTS bancos (" +
						"sigla        VARCHAR (3)         NOT NULL, " +
						"nome         STRING              NOT NULL, " +
						"PRIMARY KEY (sigla), " +
						" UNIQUE (sigla, nome));",

				"CREATE TABLE IF NOT EXISTS condicao (" +
						"texto        VARCHAR (100)       NOT NULL, " +
						"tipo         VARCHAR (3)         NOT NULL, " +
						"banco        VARCHAR (20)        NOT NULL, " +
						"PRIMARY KEY (texto, tipo));",

				"CREATE TABLE IF NOT EXISTS configuracao (" +
						"id           INT                 NOT NULL    DEFAULT (1997), " +
						"TriarAntigo  INTEGER             NOT NULL    DEFAULT (30), " +
						"TipoTriagem  VARCHAR (3)         NOT NULL    DEFAULT ('COM'), " +
						"JuntManual   BOOLEAN             NOT NULL    DEFAULT (false), " +
						"LaudoPericial BOOLEAN            NOT NULL    DEFAULT (false), " +
						"PeticaoInicial BOOLEAN           NOT NULL    DEFAULT (true), " +
						"Concatenacao BOOLEAN           NOT NULL    DEFAULT (false), " +
						"Login        STRING                          DEFAULT (''), " +
						"Senha        STRING                          DEFAULT (''), " +
						"PRIMARY KEY (id));",

				"CREATE TABLE IF NOT EXISTS contador (" +
						"id           INT                 NOT NULL    DEFAULT (1997), " +
						"ContTotal    INT                 NOT NULL    DEFAULT (0), " +
						"ContNao      INT                 NOT NULL    DEFAULT (0), " +
						"ContDoc      INT                 NOT NULL    DEFAULT (0), " +
						"ContSeq      INT                 NOT NULL    DEFAULT (0), " +
						"ContErro     INT                 NOT NULL    DEFAULT (0), " +
						"PRIMARY KEY (id));",

				"CREATE TABLE IF NOT EXISTS etiquetas (" +
						"id           INTEGER       PRIMARY KEY AUTOINCREMENT, " +
						"palavrachave VARCHAR (45)  NOT NULL, " +
						"complemento  VARCHAR (100) NOT NULL, " +
						"etiqueta     VARCHAR (100) NOT NULL, " +
						"tipo         STRING        NOT NULL DEFAULT ('MOV'), " +
						"prioridade   VARCHAR (100) NOT NULL DEFAULT (0), " +
						"banco        VARCHAR (3)   NOT NULL);",

				"CREATE TABLE IF NOT EXISTS identificador_materia (" +
						"id           INTEGER       PRIMARY KEY AUTOINCREMENT " +
						"                               NOT NULL, " +
						"palavrachave VARCHAR (255) NOT NULL, " +
						"complemento  VARCHAR (255), " +
						"etiqueta     VARCHAR (255) NOT NULL, " +
						"prioridade   VARCHAR (1)   NOT NULL " +
						"                               DEFAULT ('1'));"
		};

		try (Statement statement = connection.createStatement()) {
			for (String createTableSQL : createTableSQLs) {
				statement.execute(createTableSQL);
			}
		}
	}
	private void inserirConfiguracaoPadrao(Connection connection) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM configuracao");
			 ResultSet resultSet = stmt.executeQuery()) {
			int linhasBanco = 0;
			while (resultSet.next()) {
				linhasBanco++;
			}
			if (linhasBanco == 0) {
				try (Statement statement = connection.createStatement()) {
					statement.execute("INSERT INTO contador (id,ContTotal, ContNao, ContDoc, ContSeq, ContErro) ... ");
					statement.execute("INSERT INTO configuracao (id,TriarAntigo, TipoTriagem, JuntManual, LaudoPericial, PeticaoInicial,Concatenacao, Login, Senha) ... ");
				}
			}
		}
	}


	private void atualizarConfiguracao(String updateQuery) {
		try (Connection connection = connectionFactory.obterConexao();
			 Statement comandoSql = connection.createStatement()) {

			comandoSql.executeUpdate(updateQuery);
			System.out.println("Configurações salvas com sucesso.");
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			ControllerAviso controllerAviso = new ControllerAviso();
			controllerAviso.exibir(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	public void salvarAvancadas(Integer periodoData, String triagem, boolean juntadamanual) {
		String updateQuery = "UPDATE configuracao SET TriarAntigo = '" + periodoData + "', TipoTriagem = '" + triagem +
				"', JuntManual = '" + juntadamanual + "' WHERE id = 1997";
		atualizarConfiguracao(updateQuery);
	}

	public void salvarEspecificas(boolean laudoPericial, boolean peticaoInicial, boolean concatenacao) {
		String updateQuery = "UPDATE configuracao SET LaudoPericial = '" + laudoPericial + "', PeticaoInicial = '" + peticaoInicial +
				"', Concatenacao = '" + concatenacao + "' WHERE id = 1997";
		atualizarConfiguracao(updateQuery);
	}

	public void salvarSenha(Usuario usuario) {
		String updateQuery = "UPDATE configuracao SET Login = ?, Senha = ? WHERE id = 1997";

		try (Connection connection = connectionFactory.obterConexao();
			 PreparedStatement comandoSql = connection.prepareStatement(updateQuery)) {

			comandoSql.setString(1, usuario.getLogin());
			comandoSql.setString(2, usuario.getSenha());
			comandoSql.executeUpdate();

			System.out.println("Senha salva com sucesso.");
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			ControllerAviso controllerAviso = new ControllerAviso();
			controllerAviso.exibir(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}



	//Metodos para realizar a incrementação dos contadores
	private void updateContador(String columnToUpdate) throws SQLException {
		String updateQuery = "UPDATE contador SET " + columnToUpdate + " = " + columnToUpdate + " + 1, ContTotal = ContTotal + 1 WHERE id = 1997";

		try (Connection connection = connectionFactory.obterConexao();
			 Statement comandoSql = connection.createStatement()) {
			comandoSql.execute(updateQuery);
		}
	}

	public void contarNao() throws SQLException {
		updateContador("ContNao");
	}

	public void contarDoc() throws SQLException {
		updateContador("ContDoc");
	}

	public void contarMov() throws SQLException {
		updateContador("ContSeq");
	}
	public ChavesConfiguracao pegarConfiguracao(ChavesConfiguracao config) {
		try {
			Connection connection = connectionFactory.obterConexao();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM configuracao WHERE id = 1997");
			ResultSet resultadoBanco = stmt.executeQuery();

			if (resultadoBanco.next()) {
				config.setIntervaloDias(resultadoBanco.getInt("TriarAntigo"));
				config.setTipoTriagem(resultadoBanco.getString("TipoTriagem"));
				config.setJuntManual(resultadoBanco.getBoolean("JuntManual"));
				config.setLaudoPericial(resultadoBanco.getBoolean("LaudoPericial"));
				config.setPeticaoInicial(resultadoBanco.getBoolean("PeticaoInicial"));
				config.setConcatenacao(resultadoBanco.getBoolean("Concatenacao"));
			}

			connection.close();
		} catch (SQLException erro) {
			ControllerAviso controllerAviso = new ControllerAviso();
			controllerAviso.exibir(erro.getMessage());
			erro.printStackTrace();
			Logger.getLogger(ChavesConfiguracao.class.getName()).log(Level.SEVERE, null, erro);
		}
		return config;
	}




	public ObservableList<String> setarBanco() {
		ObservableList<String> listaBancos = FXCollections.observableArrayList("TODOS OS BANCOS");

		try (Connection connection = connectionFactory.obterConexao();
			 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bancos ORDER BY nome");
			 ResultSet resultSet = stmt.executeQuery()) {

			while (resultSet.next()) {
				String siglaNome = resultSet.getString("sigla") + " - " + resultSet.getString("nome");
				listaBancos.add(siglaNome);
			}

		} catch (SQLException erro) {
			ControllerAviso controllerAviso = new ControllerAviso();
			controllerAviso.exibir(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}

		return listaBancos;
	}


	public String selecionarBanco(String bancoSelecionadoLocal) throws SQLException {
		try (Connection connection =connectionFactory.obterConexao();
			 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bancos ORDER BY nome");
			 ResultSet resultSet = stmt.executeQuery()) {

			while (resultSet.next()) {
				String sigla = resultSet.getString("sigla");
				String nome = resultSet.getString("nome");

				if (bancoSelecionadoLocal.contains(sigla) && bancoSelecionadoLocal.contains(nome)) {
					return sigla;
				}
			}
		} catch (SQLException erro) {
			ControllerAviso controllerAviso = new ControllerAviso();
			controllerAviso.exibir(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}

		return bancoSelecionadoLocal;
	}

}