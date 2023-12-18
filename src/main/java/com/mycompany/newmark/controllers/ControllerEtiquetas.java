/*
package com.mycompany.newmark;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerEtiquetas implements Initializable {

	@FXML
	public RadioButton LerMov, LerDoc;
	@FXML
	public JFXTextField PalavraChave, Complemento, Etiqueta;
	@FXML
	public TableView<Chaves_Banco> tabelaEtiquetas;
	@FXML
	public TableColumn<Chaves_Banco, String> colunaTipo, colunaFraseChave, colunaComplemento, colunaEtiqueta,
			colunaPeso, colunaId;
	@FXML
	public JFXTextField pesquisa, pesquisaId, numeroEtiquetas;
	@FXML
	public RadioButton P1, P2, P3, P4;
	@FXML
	public ComboBox<String> Bancos;
	public ObservableList<String> Lista;
	@FXML
	public ImageView logoMark;
	@FXML
	public JFXButton botaoVoltar, botaoInserir, botaoExcluir, botaoLimpar;

	final ToggleGroup grupoTipo = new ToggleGroup();
	final ToggleGroup grupoPeso = new ToggleGroup();

	public String bancoSelecionado = "";

	@Override
	@SuppressWarnings("ConvertToTryWithResources")
	public void initialize(URL location, ResourceBundle resources) {
		Banco banco = new Banco();
		Bancos.setItems(banco.setarBanco());
		Bancos.getItems().remove(0);
		tabelaEtiquetas.setVisible(false);
		logoMark.setVisible(true);
	}

	public void selecionarBanco() throws SQLException {
		Banco banco = new Banco();
		this.bancoSelecionado = banco.selecionarBanco(Bancos.getSelectionModel().getSelectedItem());
		tabelaEtiquetas.setVisible(true);
		logoMark.setVisible(false);
		limpar();
		atualizar();
	}

	@FXML
	void retornaMenu(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
		} catch (IOException erro) {
			erro.getMessage();
		}
		Scene scene = new Scene(root);
		stage.setMinWidth(900);
		stage.setMinHeight(500);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		stage.setTitle("Sistema de Triagem Mark");
	}

	@FXML
	@SuppressWarnings("ConvertToTryWithResources")
	private boolean inserir() throws SQLException {
		String palavraChave = PalavraChave.getText().toUpperCase();
		String complemento = Complemento.getText().toUpperCase();
		String etiqueta = Etiqueta.getText().toUpperCase();
		Banco banco = new Banco();
		String textoAviso = "";
		Aviso aviso = new Aviso();
		if (bancoSelecionado.equals("")) {
			textoAviso = "Primeiro selecione um Banco na barra de seleção no canto superior direito!";
			aviso.aviso(textoAviso);
			return false;
		} else {
			Chaves_Banco chave = new Chaves_Banco();
			if (palavraChave == null || palavraChave.equals("") || palavraChave.equals(" ")) {
				textoAviso = "O campo \"Palavra Chave\" não pode ser vazio!";
				aviso.aviso(textoAviso);
				return false;
			} else if (etiqueta == null || etiqueta.equals("") || etiqueta.equals(" ")) {
				textoAviso = "O campo \"Etiqueta\" não pode ser vazio!";
				aviso.aviso(textoAviso);
				return false;
			} else {
				chave.setPALAVRACHAVE(PalavraChave.getText().toUpperCase().trim().replace("'", "").replace("´", ""));
				chave.setCOMPLEMENTO(Complemento.getText().toUpperCase().trim().replace("'", "").replace("´", ""));
				chave.setETIQUETA(Etiqueta.getText().toUpperCase().trim().replace("'", "").replace("´", ""));
				chave.setPRIORIDADE(Peso());

				if (LerMov.isSelected()) {
					chave.setTIPO("MOV");
				}

				if (LerDoc.isSelected()) {
					chave.setTIPO("DOC");
				}

				chave.setBANCO(bancoSelecionado);
				banco.inserirEtiquetas(chave);
				atualizar();
				limpar();
			}
		}
		return true;
	}

	@FXML
	public void busca() throws SQLException {
		List<Chaves_Banco> chaves = new ArrayList<>();
		Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE BANCO = '"
				+ bancoSelecionado + "' " + "AND TIPO != 'PET' " + "AND (ETIQUETA LIKE '%"
				+ pesquisa.getText().toUpperCase().trim().replace("'", "").replace("´", "") + "%' "
				+ "OR PALAVRACHAVE LIKE '%" + pesquisa.getText().toUpperCase().trim().replace("'", "").replace("´", "")
				+ "%' " + "OR COMPLEMENTO LIKE '%"
				+ pesquisa.getText().toUpperCase().trim().replace("'", "").replace("´", "") + "%' " + "OR TIPO LIKE '%"
				+ pesquisa.getText().toUpperCase().trim().replace("'", "").replace("´", "") + "%') "
				+ "ORDER BY PALAVRACHAVE");
		ResultSet resultSet = stmt.executeQuery();

		int i = 0;
		while (resultSet.next()) {
			Chaves_Banco key = new Chaves_Banco();
			key.setID(resultSet.getInt("ID"));
			key.setPALAVRACHAVE(resultSet.getString("PALAVRACHAVE"));
			key.setCOMPLEMENTO(resultSet.getString("COMPLEMENTO"));
			key.setETIQUETA(resultSet.getString("ETIQUETA"));
			key.setTIPO(resultSet.getString("TIPO"));
			key.setPRIORIDADE((resultSet.getString("PRIORIDADE")));
			chaves.add(key);
			i++;
		}
		colunaId.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
		colunaFraseChave.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
		colunaComplemento.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
		colunaEtiqueta.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
		colunaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
		colunaTipo.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("TIPO"));
		ObservableList<Chaves_Banco> genericos = FXCollections.observableArrayList(chaves);
		numeroEtiquetas.setText(i + "");
		tabelaEtiquetas.setItems(genericos);
		connection.close();
	}

	@FXML
	public void buscaId() throws SQLException {
		List<Chaves_Banco> chaves = new ArrayList<>();
		Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
		PreparedStatement stmt = null;
		if (pesquisaId.getText().isEmpty()) {
			stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE BANCO = '" + bancoSelecionado + "' "
					+ "AND TIPO != 'PET' ORDER BY ID");
		} else {
			stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE BANCO = '" + bancoSelecionado + "' "
					+ "AND TIPO != 'PET' " + "AND ID = '" + pesquisaId.getText() + "' ORDER BY ID");
		}

		ResultSet resultSet = stmt.executeQuery();

		int i = 0;
		while (resultSet.next()) {
			Chaves_Banco key = new Chaves_Banco();
			key.setID(resultSet.getInt("ID"));
			key.setPALAVRACHAVE(resultSet.getString("PALAVRACHAVE"));
			key.setCOMPLEMENTO(resultSet.getString("COMPLEMENTO"));
			key.setETIQUETA(resultSet.getString("ETIQUETA"));
			key.setTIPO(resultSet.getString("TIPO"));
			key.setPRIORIDADE((resultSet.getString("PRIORIDADE")));
			chaves.add(key);
			i++;
		}

		colunaId.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
		colunaFraseChave.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
		colunaComplemento.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
		colunaEtiqueta.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
		colunaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
		colunaTipo.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("TIPO"));
		ObservableList<Chaves_Banco> genericos = FXCollections.observableArrayList(chaves);
		numeroEtiquetas.setText(i + "");
		tabelaEtiquetas.setItems(genericos);
		connection.close();
	}

	@FXML
	@SuppressWarnings("ConvertToTryWithResources")
	public void excluir() throws SQLException {
		if (bancoSelecionado.equals("")) {
			Aviso aviso = new Aviso();
			String textoAviso = "Primeiro selecione um Banco na barra de seleção no canto superior direito!";
			aviso.aviso(textoAviso);
		} else if (PalavraChave.getText().equals("")) {
			// Não faz nada
		} else {
			Chaves_Banco chave = new Chaves_Banco();
			chave.setPALAVRACHAVE(PalavraChave.getText());
			chave.setCOMPLEMENTO(Complemento.getText());
			chave.setBANCO(bancoSelecionado);
			Aviso aviso = new Aviso();
			aviso.confirmacaoEtiqueta(chave);
			limpar();
			atualizar();
		}
	}

	@SuppressWarnings("ConvertToTryWithResources")
	public void atualizar() throws SQLException {
		try {
			List<Chaves_Banco> chaves = new ArrayList<>();
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			PreparedStatement stmt;
			if (bancoSelecionado.contains("TODOS OS BANCOS")) {
				stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE TIPO != 'PET' ORDER BY PALAVRACHAVE");
			} else {
				stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE BANCO = '" + bancoSelecionado
						+ "' AND TIPO != 'PET' ORDER BY ID");
			}
			ResultSet resultSet = stmt.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				Chaves_Banco key = new Chaves_Banco();
				key.setID(resultSet.getInt("ID"));
				key.setPALAVRACHAVE(resultSet.getString("PALAVRACHAVE"));
				key.setCOMPLEMENTO(resultSet.getString("COMPLEMENTO"));
				key.setETIQUETA(resultSet.getString("ETIQUETA"));
				key.setTIPO(resultSet.getString("TIPO"));
				key.setPRIORIDADE(resultSet.getString("PRIORIDADE"));

				chaves.add(key);
				i++;
			}

			colunaId.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
			colunaFraseChave.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
			colunaComplemento.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
			colunaEtiqueta.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
			colunaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
			colunaTipo.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("TIPO"));
			ObservableList<Chaves_Banco> genericos = FXCollections.observableArrayList(chaves);
			numeroEtiquetas.setText(i + "");
			tabelaEtiquetas.setItems(genericos);
			connection.close();
		} catch (SQLException erro) {
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage());
		}
	}

	public void alterarEtiquetas() throws IOException, SQLException {
		Chaves_Banco chave = new Chaves_Banco();
		chave.setPALAVRACHAVE(tabelaEtiquetas.getSelectionModel().getSelectedItem().getPALAVRACHAVE().replace("'", "")
				.replace("´", ""));
		chave.setCOMPLEMENTO(tabelaEtiquetas.getSelectionModel().getSelectedItem().getCOMPLEMENTO().replace("'", "")
				.replace("´", ""));
		chave.setETIQUETA(
				tabelaEtiquetas.getSelectionModel().getSelectedItem().getETIQUETA().replace("'", "").replace("´", ""));
		chave.setTIPO(tabelaEtiquetas.getSelectionModel().getSelectedItem().getTIPO());
		chave.setPRIORIDADE(tabelaEtiquetas.getSelectionModel().getSelectedItem().getPRIORIDADE());
		chave.setBANCO(bancoSelecionado);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagEdicaoEtiqueta.fxml"));
		loader.setController(new Controller_TagEdicaoEtiqueta(chave));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setTitle("Editar Etiqueta");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(new Scene(root));
		stage.show();
	}

	public void limpar() {
		PalavraChave.clear();
		Complemento.clear();
		Etiqueta.clear();
		LerDoc.setSelected(true);
		P1.setSelected(true);
	}

	public void selecionar() {
		limpar();
		PalavraChave.setText(tabelaEtiquetas.getSelectionModel().getSelectedItem().getPALAVRACHAVE());
		Complemento.setText(tabelaEtiquetas.getSelectionModel().getSelectedItem().getCOMPLEMENTO());
		Etiqueta.setText(tabelaEtiquetas.getSelectionModel().getSelectedItem().getETIQUETA());
		String Tipo = tabelaEtiquetas.getSelectionModel().getSelectedItem().getTIPO();
		if (Tipo.equals("MOV")) {
			LerMov.setSelected(true);
		} else {
			LerDoc.setSelected(true);
		}
		String peso = tabelaEtiquetas.getSelectionModel().getSelectedItem().getPRIORIDADE();
		switch (peso) {
		case "1":
			P1.setSelected(true);
			break;
		case "2":
			P2.setSelected(true);
			break;
		case "3":
			P3.setSelected(true);
			break;
		case "4":
			P4.setSelected(true);
			break;
		}

	}

	public String Peso() {
		if (P1.isSelected()) {
			return "1";
		} else if (P2.isSelected()) {
			return "2";
		} else if (P3.isSelected()) {
			return "3";
		} else if (P4.isSelected()) {
			return "4";
		}
		return null;
	}
}*/