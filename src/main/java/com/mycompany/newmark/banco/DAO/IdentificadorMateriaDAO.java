package com.mycompany.newmark.banco.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.banco.Banco;
import com.mycompany.newmark.models.ChavesBanco;
import com.mycompany.newmark.banco.ConnectionFactory;

public class IdentificadorMateriaDAO {
	ControllerAviso controllerAviso = new ControllerAviso();

	public List<ChavesBanco> getTabelaIdentificadorMateria() {
		List<ChavesBanco> identificadoresMateria = new ArrayList<>();
		final String SQL = "SELECT * FROM identificador_materia ORDER BY id";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			identificadoresMateria = extrairChaveIdentificadorMateria(resultSet);
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage() + "aaaaaa");
		} 

		return identificadoresMateria;
	}

	public void inserirIdentificadorMateria(String pedido, String complementoPedido, String subnucleo,
			Integer pesoSelecionado, String etiqueta) {

		final String SQL = "INSERT INTO identificador_materia (palavrachave, complemento, subnucleo, prioridade, etiqueta) VALUES (?, ?, ?, ?, ?);";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, pedido);
			stmt.setString(2, complementoPedido);
			stmt.setString(3, subnucleo);
			stmt.setInt(4, pesoSelecionado);
			stmt.setString(5, etiqueta);

			stmt.execute();

			controllerAviso.exibir("Etiqueta inserida com sucesso!");

		} catch (SQLException erro) {
			if (erro.getMessage().contains("UNIQUE constraint")) {
				controllerAviso.exibir("Não foi possível inserir o registro:\n" + "O registro já esta cadastrado!");
				erro.printStackTrace();
			} else {
				Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
				controllerAviso.exibir(erro.getMessage());
			}
		}
	}

	public void removerIdentificadorMateria(Integer idSelecionado) {
		final String SQL = "DELETE FROM identificador_materia WHERE id = ?";
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setInt(1, idSelecionado);
			stmt.executeUpdate();
			controllerAviso.exibir("Item removido");
		} catch (Exception e) {
			controllerAviso.exibir("Item não removido\n" + e.getMessage());
		}

	}

	public List<ChavesBanco> buscaIdentificadorPorID(String id) {

		List<ChavesBanco> chaves = new ArrayList<>();

		final String SQL = "SELECT * FROM identificador_materia WHERE id = ?";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			chaves = extrairChaveIdentificadorMateria(rs);
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}

		return chaves;

	}

	public List<ChavesBanco> buscaIdentificador(String textoBusca) {
		final String SQL = "SELECT * FROM identificador_materia WHERE palavrachave LIKE ? OR complemento LIKE ? OR etiqueta LIKE ?";
		
		List<ChavesBanco> chaves = new ArrayList<>();
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
				stmt.setString(1, '%' + textoBusca + '%');
				stmt.setString(2, '%' + textoBusca + '%');
				stmt.setString(3, '%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			chaves = extrairChaveIdentificadorMateria(rs);
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		
		return chaves;
		
	}
	
	public void atualizarIndetificadorMateria(Integer id, String palavrachave, String complemento, String etiqueta, String subnucleo, String prioridade) {
		
		final String SQL = "UPDATE identificador_materia SET palavrachave = ?, complemento = ?, etiqueta = ?, subnucleo = ?, prioridade = ? WHERE id = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			
			stmt.setString(1, palavrachave);
			stmt.setString(2, complemento);
			stmt.setString(3, etiqueta);
			stmt.setString(4, subnucleo);
			stmt.setString(5, prioridade);			
			stmt.setInt(6, id);
			stmt.executeUpdate();
			controllerAviso.exibir("Item atualizado");
		} catch (Exception e) {
			controllerAviso.exibir("Não foi possível atualizar o item\n" + e.getMessage());
		}
	}

	private List<ChavesBanco> extrairChaveIdentificadorMateria(ResultSet resultSet) throws SQLException {
		List<ChavesBanco> identificadoresMateria = null;
		while (resultSet.next()) {
			identificadoresMateria = new ArrayList<>();
			ChavesBanco chave = new ChavesBanco();
			chave.setID(resultSet.getInt("id"));
			chave.setPALAVRACHAVE(resultSet.getString("palavrachave"));
			chave.setCOMPLEMENTO(resultSet.getString("complemento"));
			chave.setSubnucleo(resultSet.getString("subnucleo"));
			chave.setETIQUETA(resultSet.getString("etiqueta"));
			chave.setPRIORIDADE(resultSet.getString("prioridade"));
			identificadoresMateria.add(chave);
		}
		return identificadoresMateria;
	}

}
