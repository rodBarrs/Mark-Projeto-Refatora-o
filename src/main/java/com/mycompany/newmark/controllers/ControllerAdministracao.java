package com.mycompany.newmark.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.banco.Banco;

import com.mycompany.newmark.models.*;
import com.mycompany.newmark.banco.DAO.BancosDAO;
import com.mycompany.newmark.banco.DAO.EtiquetaDAO;
import com.mycompany.newmark.banco.DAO.IdentificadorMateriaDAO;
import com.mycompany.newmark.banco.DAO.IdentificadorPeticaoInicialDAO;
import com.mycompany.newmark.banco.DAO.TipoMovimentacaoDAO;
import com.mycompany.newmark.banco.DAO.UsuarioLocalDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerAdministracao implements Initializable {


	@FXML
	private JFXTextField identificadorEtiqueta, numeroEtiquetas, numeroTipoMovimentacao, numeroIdentificadorMateria,
			numeroPeticaoInicial, pedido, complementoPedido, buscaIdentificadorMateriaID, buscaIdentificadorMateria,
			identificadorPeticaoInicial, pesquisaIdentificador, pesquisaEtiqueta, pesquisaEtiquetaId, palavraChave,
			complemento, etiqueta, buscaTipoMovimentacao,buscaPasta, nomePasta, tipoMovimentacao, textoSigla, textoBanco, textoNomeUsuario,
			textoSenhaUsuarioVisivel;
	@FXML
	private JFXPasswordField textoSenhaUsuario;
	@FXML
	private JFXComboBox<String> comboBoxBancos, comboBoxNucleo, comboBoxBancosMov, comboBoxBancosMovPet;
	@FXML
	private TableView<ChavesGrupoEtiquetas> tabelaBancos;
	@FXML
	private TableView<ChavesBanco> tabelaIdentificadorMateria, tabelaEtiquetas;
	@FXML
	private TableView<ChavesPastaCondicao> tabelaPasta;
	@FXML
	private TableView<ChavesCondicao> tabelaTipoMovimento, tabelaIdentificadorPeticao;
	@FXML
	private TableView<UsuarioLocal> tabelaUsuarios;
	@FXML
	private TableColumn<UsuarioLocal, String> nomeUsuario;
	@FXML
	private TableColumn<ChavesPastaCondicao, String> colunaPasta;

	@FXML
	private TableColumn<ChavesBanco, String> identificadorMateriaID, identificadorMateriaPedido,
			identificadorMateriaComplemento, identificadorMateriaSubnucleo, identificadorMateriaPeso, etiquetaID,
			etiquetaFraseChave, etiquetaComplemento, etiquetaEtiqueta, etiquetaPeso, etiquetaTipo,
			identificadorMateriaEtiqueta;
	@FXML
	private TableColumn<ChavesCondicao, String> colunaTipoMovimento, colunaBanco ,colunaIdentificadorPeticao,
			colunaIdentificadorPeticaoInicial;
	@FXML
	private TableColumn<ChavesGrupoEtiquetas, String> bancoSigla, bancoNome, bancoNumeroDeEtiquetas;
	@FXML
	private RadioButton etiquetaP1, etiquetaP2, etiquetaP3, etiquetaP4, identificadorMateriaP1, identificadorMateriaP2,
			identificadorMateriaP3, identificadorMateriaP4, documento, movimentacao;
	@FXML
	private JFXButton inserirIdentificadorMateria, btnAtualizarUsuario;
	@FXML
	private JFXCheckBox exibirSenha;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inicializarMenuPeticaoInicial();
		inicializarMenuTriagemPadrao();
		inicializarBancoDeDados();
		inicializarUsuarios();
	}

	/* Inicializações */

	public void inicializarMenuPeticaoInicial() {
		inicializarTabelaIdentificadorMateria();
		inicializarTabelaIdentificadorPeticaoInicial();
	}


	private void inicializarTabelaIdentificadorPeticaoInicial() {
		List<ChavesCondicao> listaIdentificadorPeticao = new IdentificadorPeticaoInicialDAO()
				.getTabelaIdentificadorPeticaoInicial();
		comboBoxBancosMovPet.setItems(new Banco().setarBanco());
		try {
			comboBoxBancosMovPet.getItems().remove(0);
			comboBoxBancosMovPet.getSelectionModel().selectFirst();
		} catch (Exception e) {
		}
		colunaIdentificadorPeticaoInicial
				.setCellValueFactory(new PropertyValueFactory<ChavesCondicao, String>("TEXTO"));
		ObservableList<ChavesCondicao> identPeticao = FXCollections.observableArrayList(listaIdentificadorPeticao);
		tabelaIdentificadorPeticao.setItems(identPeticao);
		numeroPeticaoInicial.setText(String.valueOf(tabelaIdentificadorPeticao.getItems().size()));

	}

	private void inicializarTabelaIdentificadorMateria() {

		ObservableList<String> subnucleos = FXCollections.observableArrayList("ER-SEAS", "ETR-BI", "ER-TRU");
		comboBoxNucleo.setItems(subnucleos);

		List<ChavesBanco> listaIdentificadoresMateria = new IdentificadorMateriaDAO().getTabelaIdentificadorMateria();

		identificadorMateriaID.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ID"));
		identificadorMateriaPedido.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PALAVRACHAVE"));
		identificadorMateriaComplemento
				.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("COMPLEMENTO"));
		identificadorMateriaPeso.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PRIORIDADE"));
		identificadorMateriaSubnucleo.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("subnucleo"));
		identificadorMateriaEtiqueta.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ETIQUETA"));
		ObservableList<ChavesBanco> identificadoresDeMateria = FXCollections
				.observableArrayList(listaIdentificadoresMateria);
		tabelaIdentificadorMateria.setItems(identificadoresDeMateria);
		numeroIdentificadorMateria.setText(String.valueOf(tabelaIdentificadorMateria.getItems().size()));
	}

	public void inicializarMenuTriagemPadrao() {
		inicializarComboBoxBancos(comboBoxBancos);
		inicializarTabelaEtiquetas();
		inicializarTabelaTiposMovimentacao();
	}


	private void inicializarTabelaTiposMovimentacao() {
	//	inicializarComboBoxBancos(comboBoxBancosMov);
		comboBoxBancosMov.setItems(new Banco().setarBanco());
		try {
			comboBoxBancosMov.getItems().remove(0);
			comboBoxBancosMov.getSelectionModel().selectFirst();
		} catch (Exception e) {
		}
		List<ChavesCondicao> listaTiposMovimentacao = new TipoMovimentacaoDAO().getTabelaTipoMovimentacao();
		colunaTipoMovimento.setCellValueFactory(new PropertyValueFactory<ChavesCondicao, String>("TEXTO"));
		ObservableList<ChavesCondicao> tiposMovimentacao = FXCollections.observableArrayList(listaTiposMovimentacao);
		tabelaTipoMovimento.setItems(tiposMovimentacao);
		numeroTipoMovimentacao.setText(String.valueOf(tabelaTipoMovimento.getItems().size()));

		List<ChavesPastaCondicao> listaPasta = new TipoMovimentacaoDAO().getTabelaPastas();
		colunaPasta.setCellValueFactory(new PropertyValueFactory<ChavesPastaCondicao, String>("nome"));
		ObservableList<ChavesPastaCondicao> tabelaPastas = FXCollections.observableArrayList(listaPasta);
		tabelaPasta.setItems(tabelaPastas);
		numeroTipoMovimentacao.setText(String.valueOf(tabelaTipoMovimento.getItems().size()));
	}

	private void inicializarTabelaEtiquetas() {
		String bancoSelecionado = "";
		try {
			bancoSelecionado = comboBoxBancos.getSelectionModel().getSelectedItem().substring(0, 3);
		} catch (Exception e) {

		}
		List<ChavesBanco> listaTabelaEtiquetas = new EtiquetaDAO().getTabelaEtiqueta(bancoSelecionado);

		etiquetaID.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ID"));
		etiquetaFraseChave.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PALAVRACHAVE"));
		etiquetaComplemento.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("COMPLEMENTO"));
		etiquetaEtiqueta.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ETIQUETA"));
		etiquetaPeso.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PRIORIDADE"));
		etiquetaTipo.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("TIPO"));
		ObservableList<ChavesBanco> genericos = FXCollections.observableArrayList(listaTabelaEtiquetas);

		tabelaEtiquetas.setItems(genericos);
		numeroEtiquetas.setText(String.valueOf(tabelaEtiquetas.getItems().size()));
	}

	public void inicializarUsuarios() {
		List<UsuarioLocal> usuarios = new UsuarioLocalDAO().getTabelaUsuarios();
		nomeUsuario.setCellValueFactory(new PropertyValueFactory<UsuarioLocal, String>("nome"));
		ObservableList<UsuarioLocal> users = FXCollections.observableArrayList(usuarios);
		tabelaUsuarios.setItems(users);
	}

	private void inicializarComboBoxBancos(JFXComboBox<String> comboBoxBancos) {
		comboBoxBancos.setItems(new Banco().setarBanco());
		try {
			comboBoxBancos.getItems().remove(0);
			comboBoxBancos.getSelectionModel().selectFirst();
		} catch (Exception e) {
		}
	}

	public void inicializarBancoDeDados() {
		/* Inicializa a tabela Banco de Dados */
		List<ChavesGrupoEtiquetas> chaves = new BancosDAO().getTabelaBancoDeDados();
		bancoSigla.setCellValueFactory(new PropertyValueFactory<ChavesGrupoEtiquetas, String>("sigla"));
		bancoNome.setCellValueFactory(new PropertyValueFactory<ChavesGrupoEtiquetas, String>("nome"));
		bancoNumeroDeEtiquetas
				.setCellValueFactory(new PropertyValueFactory<ChavesGrupoEtiquetas, String>("qntEtiquetas"));
		ObservableList<ChavesGrupoEtiquetas> genericos = FXCollections.observableArrayList(chaves);
		tabelaBancos.setItems(genericos);
	}



	/* Global */
	@FXML
	public void retornaMenu(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
	}

	/* Identificador de Matéria */

	@FXML
	public void inserirIdentificadorMateria() {
		String pedidoTexto = pedido.getText().trim();
		String complementoPedidoTexto = complementoPedido.getText().trim();
		String subnucleoSelecionado = comboBoxNucleo.getSelectionModel().getSelectedItem();
		String identificadorEtiquetaTexto = identificadorEtiqueta.getText().trim();

		boolean pedidoNaoEstaVazio = !pedidoTexto.isEmpty();
		boolean subnucleoFoiSelecionado = subnucleoSelecionado != null && !subnucleoSelecionado.isEmpty();

		int pesoSelecionado = 4; // Padrão

		if (identificadorMateriaP1.isSelected()) {
			pesoSelecionado = 1;
		} else if (identificadorMateriaP2.isSelected()) {
			pesoSelecionado = 2;
		} else if (identificadorMateriaP3.isSelected()) {
			pesoSelecionado = 3;
		}

		if (pedidoNaoEstaVazio && subnucleoFoiSelecionado) {
			IdentificadorMateriaDAO dao = new IdentificadorMateriaDAO();
			String pedidoFormatado = pedidoTexto.toUpperCase().replace("'", "").replace("´", "");
			String complementoPedidoFormatado = complementoPedidoTexto.toUpperCase().replace("'", "").replace("´", "");
			String identificadorEtiquetaFormatado = identificadorEtiquetaTexto.toUpperCase().replace("'", "").replace("´", "");
			dao.inserirIdentificadorMateria(pedidoFormatado, complementoPedidoFormatado, subnucleoSelecionado, pesoSelecionado, identificadorEtiquetaFormatado);
			inicializarMenuPeticaoInicial();
		} else {
			new ControllerAviso().exibir("Revise os campos preenchidos");
		}

		inicializarMenuTriagemPadrao();
	}
	@FXML
	public void alterarIdentificadorMateria() throws IOException {
		ChavesBanco chave = new ChavesBanco();
		chave.setID(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getID());
		chave.setPALAVRACHAVE(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getPALAVRACHAVE());
		chave.setCOMPLEMENTO(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getCOMPLEMENTO());
		chave.setETIQUETA(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getETIQUETA());
		chave.setTIPO(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getTIPO());
		chave.setPRIORIDADE(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getPRIORIDADE());
		chave.setBANCO("PET");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagEdicaoMateria.fxml"));
		loader.setController(new ControllerTagEdicaoMateria(chave));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setTitle("Editar Etiqueta");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(new Scene(root));
		stage.show();

	}

	@FXML
	public void excluirIdentificadorMateria() {
		Integer idSelecionado = tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getID();
		new IdentificadorMateriaDAO().removerIdentificadorMateria(idSelecionado);
		inicializarMenuPeticaoInicial();
	}

	public void limparIdentificadorMateria() {
		buscaIdentificadorMateriaID.clear();
		pedido.clear();
		complementoPedido.clear();
		comboBoxNucleo.getSelectionModel().clearSelection();
	}

	public void selecionarIdentificadorMateria() {
		pedido.setText(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getPALAVRACHAVE());
		complementoPedido.setText(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getCOMPLEMENTO());
		comboBoxNucleo.getSelectionModel()
				.select(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getSubnucleo());
	}

	public void buscaIDIdentificadorMateria() {
		String id = buscaIdentificadorMateriaID.getText();
		if (id.isEmpty()) {
			inicializarMenuPeticaoInicial();
		} else {
			List<ChavesBanco> listaIdentificadoresMateria = new IdentificadorMateriaDAO().buscaIdentificadorPorID(id);
			identificadorMateriaID.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ID"));
			identificadorMateriaPedido
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PALAVRACHAVE"));
			identificadorMateriaComplemento
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("COMPLEMENTO"));
			identificadorMateriaSubnucleo
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("subnucleo"));
			identificadorMateriaEtiqueta
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ETIQUETA"));
			identificadorMateriaPeso.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PRIORIDADE"));
			ObservableList<ChavesBanco> identificadoresDeMateria = FXCollections
					.observableArrayList(listaIdentificadoresMateria);
			tabelaIdentificadorMateria.setItems(identificadoresDeMateria);
			numeroIdentificadorMateria.setText(String.valueOf(tabelaIdentificadorMateria.getItems().size()));
		}
	}

	public void buscaIdentificadorMateria() {
		String textoBusca = buscaIdentificadorMateria.getText().toUpperCase();
		if (textoBusca.isEmpty()) {
			inicializarMenuPeticaoInicial();
		} else {
			List<ChavesBanco> listaIdentificadoresMateria = new IdentificadorMateriaDAO()
					.buscaIdentificador(textoBusca);

			identificadorMateriaID.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ID"));
			identificadorMateriaPedido
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PALAVRACHAVE"));
			identificadorMateriaComplemento
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("COMPLEMENTO"));
			identificadorMateriaPeso.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PRIORIDADE"));
			identificadorMateriaSubnucleo
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("subnucleo"));
			identificadorMateriaEtiqueta
					.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ETIQUETA"));
			ObservableList<ChavesBanco> identificadoresDeMateria = FXCollections
					.observableArrayList(listaIdentificadoresMateria);
			tabelaIdentificadorMateria.setItems(identificadoresDeMateria);
			numeroIdentificadorMateria.setText(String.valueOf(tabelaIdentificadorMateria.getItems().size()));
		}
	}

	/* Identificador de Petição Inicial */

	public void inserirIdentificadorPeticao() {
		String identificadorPeticao = identificadorPeticaoInicial.getText().toUpperCase();
		String banco = comboBoxBancosMovPet.getSelectionModel().getSelectedItem().substring(0, 3);
		if (identificadorPeticao.isEmpty()) {
			new ControllerAviso().exibir("Revise os campos");
		} else {
			new IdentificadorPeticaoInicialDAO().inserirIdentificadorPeticaoInicial(identificadorPeticao, banco);
		}
		inicializarMenuPeticaoInicial();

	}

	public void excluirIdentificadorPeticao() {
		String texto = tabelaIdentificadorPeticao.getSelectionModel().getSelectedItem().getTEXTO();
		new IdentificadorPeticaoInicialDAO().removerIdentificadorPeticaoInicial(texto);
		inicializarMenuPeticaoInicial();
	}

	@FXML
	public void buscaIdentificadorPeticao() {
		String textoBusca = pesquisaIdentificador.getText().toUpperCase();
		System.out.println(textoBusca);
		if (textoBusca.isEmpty()) {
			inicializarMenuPeticaoInicial();
		} else {
			List<ChavesCondicao> itensIdentificadoresPeticao = new IdentificadorPeticaoInicialDAO()
					.buscarIdentificadorPeticao(textoBusca);
			colunaIdentificadorPeticaoInicial
					.setCellValueFactory(new PropertyValueFactory<ChavesCondicao, String>("TEXTO"));
			ObservableList<ChavesCondicao> identPeticao = FXCollections
					.observableArrayList(itensIdentificadoresPeticao);
			tabelaIdentificadorPeticao.setItems(identPeticao);
			numeroPeticaoInicial.setText(String.valueOf(tabelaIdentificadorPeticao.getItems().size()));
		}
	}

	public void alterarIdentificadorPeticao() throws IOException {
		ChavesCondicao chave = new ChavesCondicao();
		chave.setTEXTO(tabelaIdentificadorPeticao.getSelectionModel().getSelectedItem().getTEXTO());

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagEdicaoConfiguracao.fxml"));
		loader.setController(new ControllerTagEdicaoCondicao(chave));
		Parent root = loader.load();
		Stage stage = new Stage();

		stage.setTitle("Editar Condição");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(new Scene(root));
		stage.show();
	}

	public void selecionarIdentificadorPeticao() {
		identificadorPeticaoInicial
				.setText(tabelaIdentificadorPeticao.getSelectionModel().getSelectedItem().getTEXTO());
	}

	/* Etiquetas */

	public void selecionarBancoEtiqueta() {
		String bancoSelecionado = comboBoxBancos.getSelectionModel().getSelectedItem().substring(0, 3);

		List<ChavesBanco> listaTabelaEtiquetas = new EtiquetaDAO().getTabelaEtiqueta(bancoSelecionado);

		etiquetaID.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ID"));
		etiquetaFraseChave.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PALAVRACHAVE"));
		etiquetaComplemento.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("COMPLEMENTO"));
		etiquetaEtiqueta.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ETIQUETA"));
		etiquetaPeso.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PRIORIDADE"));
		etiquetaTipo.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("TIPO"));
		ObservableList<ChavesBanco> genericos = FXCollections.observableArrayList(listaTabelaEtiquetas);

		tabelaEtiquetas.setItems(genericos);
		numeroEtiquetas.setText(String.valueOf(tabelaEtiquetas.getItems().size()));
	}

	public void buscaEtiqueta() {
		String textoEtiqueta = pesquisaEtiqueta.getText().toUpperCase();
		if (textoEtiqueta.isEmpty()) {
			inicializarMenuTriagemPadrao();
		} else {
			List<ChavesBanco> etiquetas = new EtiquetaDAO().buscaEtiqueta(textoEtiqueta);
			etiquetaID.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ID"));
			etiquetaFraseChave.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PALAVRACHAVE"));
			etiquetaComplemento.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("COMPLEMENTO"));
			etiquetaEtiqueta.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ETIQUETA"));
			etiquetaPeso.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PRIORIDADE"));
			etiquetaTipo.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("TIPO"));
			ObservableList<ChavesBanco> genericos = FXCollections.observableArrayList(etiquetas);

			tabelaEtiquetas.setItems(genericos);
			numeroEtiquetas.setText(String.valueOf(tabelaEtiquetas.getItems().size()));
		}
	}

	public void buscaEtiquetaID() {
		String id = pesquisaEtiquetaId.getText().toUpperCase();
		if (id.isEmpty()) {
			inicializarMenuTriagemPadrao();
		} else {
			List<ChavesBanco> etiquetas = new EtiquetaDAO().buscaEtiquetaPorID(id);
			etiquetaID.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ID"));
			etiquetaFraseChave.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PALAVRACHAVE"));
			etiquetaComplemento.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("COMPLEMENTO"));
			etiquetaEtiqueta.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("ETIQUETA"));
			etiquetaPeso.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("PRIORIDADE"));
			etiquetaTipo.setCellValueFactory(new PropertyValueFactory<ChavesBanco, String>("TIPO"));
			ObservableList<ChavesBanco> genericos = FXCollections.observableArrayList(etiquetas);

			tabelaEtiquetas.setItems(genericos);
			numeroEtiquetas.setText(String.valueOf(tabelaEtiquetas.getItems().size()));
		}
	}

	public void limparEtiqueta() {
		pesquisaEtiquetaId.clear();
		palavraChave.clear();
		complemento.clear();
		etiqueta.clear();
		documento.setSelected(true);
	}

	public void atualizarTabelaEtiqueta() {
		inicializarMenuTriagemPadrao();
	}

	public void inserirEtiqueta() {
		String palavra = this.palavraChave.getText().toUpperCase();
		String comp = this.complemento.getText().toUpperCase();
		String etiq = this.etiqueta.getText().toUpperCase();
		String banco = comboBoxBancos.getSelectionModel().getSelectedItem().substring(0, 3);

		String tipo;
		if (documento.isSelected()) {
			tipo = "DOC";
		} else {
			tipo = "MOV";
		}

		String peso = "1";
		if (etiquetaP1.isSelected())
			peso = "1";
		if (etiquetaP2.isSelected())
			peso = "2";
		if (etiquetaP3.isSelected())
			peso = "3";
		if (etiquetaP4.isSelected())
			peso = "4";

		if (palavra.isEmpty() || etiq.isEmpty()) {
			new ControllerAviso().exibir("Revise os campos");
		} else {
			new EtiquetaDAO().inserirEtiqueta(palavra, comp, etiq, tipo, peso, banco);
			inicializarMenuTriagemPadrao();
		}
	}

	public void excluirEtiqueta() {
		Integer id = tabelaEtiquetas.getSelectionModel().getSelectedItem().getID();
		new EtiquetaDAO().removerEtiqueta(id);
		inicializarMenuTriagemPadrao();
	}

	public void selecionarEtiqueta() {
		palavraChave.setText(tabelaEtiquetas.getSelectionModel().getSelectedItem().getPALAVRACHAVE());
		complemento.setText(tabelaEtiquetas.getSelectionModel().getSelectedItem().getCOMPLEMENTO());
		etiqueta.setText(tabelaEtiquetas.getSelectionModel().getSelectedItem().getETIQUETA());
		String peso = tabelaEtiquetas.getSelectionModel().getSelectedItem().getPRIORIDADE();
		String leitura = tabelaEtiquetas.getSelectionModel().getSelectedItem().getTIPO();

		switch (peso) {
		case "1":
			etiquetaP1.setSelected(true);
			break;
		case "2":
			etiquetaP2.setSelected(true);
			break;
		case "3":
			etiquetaP3.setSelected(true);
			break;
		case "4":
			etiquetaP4.setSelected(true);
			break;
		default:
			break;
		}

		switch (leitura) {
		case "MOV":
			movimentacao.setSelected(true);
			break;
		case "DOC":
			documento.setSelected(true);
			break;
		default:
			break;
		}
	}

	public void alterarEtiqueta() throws IOException {
		ChavesBanco chave = new ChavesBanco();
		String bancoSelecionado = comboBoxBancos.getSelectionModel().getSelectedItem().substring(0, 3);
		chave.setID(tabelaEtiquetas.getSelectionModel().getSelectedItem().getID());
		chave.setPALAVRACHAVE(tabelaEtiquetas.getSelectionModel().getSelectedItem().getPALAVRACHAVE().replace("'", "")
				.replace("´", ""));
		chave.setCOMPLEMENTO(tabelaEtiquetas.getSelectionModel().getSelectedItem().getCOMPLEMENTO().replace("'", "")
				.replace("´", ""));
		chave.setETIQUETA(
				tabelaEtiquetas.getSelectionModel().getSelectedItem().getETIQUETA().replace("'", "").replace("´", ""));
		chave.setTIPO(tabelaEtiquetas.getSelectionModel().getSelectedItem().getTIPO());
		chave.setPRIORIDADE(tabelaEtiquetas.getSelectionModel().getSelectedItem().getPRIORIDADE());
		chave.setBANCO(bancoSelecionado);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagEdicaoEtiqueta.fxml"));
		loader.setController(new ControllerTagEdicaoEtiqueta(chave));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setTitle("Editar Etiqueta");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(new Scene(root));
		stage.show();
	}

	/* Tipos de Movimentação */

	public void buscaTipoMovimentacao() {
		String textoBusca = buscaTipoMovimentacao.getText().toUpperCase();
		if (textoBusca.isEmpty()) {
			inicializarMenuTriagemPadrao();
		} else {
			List<ChavesCondicao> tipos = new TipoMovimentacaoDAO().buscarTipoMovimentacao(textoBusca);
			colunaTipoMovimento.setCellValueFactory(new PropertyValueFactory<ChavesCondicao, String>("TEXTO"));
			ObservableList<ChavesCondicao> tiposMovimentacao = FXCollections.observableArrayList(tipos);
			tabelaTipoMovimento.setItems(tiposMovimentacao);
			numeroTipoMovimentacao.setText(String.valueOf(tabelaTipoMovimento.getItems().size()));
		}
	}

	public void excluirTipoMovimentacao() {
		String texto = tabelaTipoMovimento.getSelectionModel().getSelectedItem().getTEXTO();
		new TipoMovimentacaoDAO().removerTipoMovimentacao(texto);
		inicializarMenuTriagemPadrao();
	}

	public void inserirTipoMovimentacao() {
		String texto = tipoMovimentacao.getText().toUpperCase();
		String banco = comboBoxBancosMov.getSelectionModel().getSelectedItem().substring(0, 3);
		new TipoMovimentacaoDAO().inserirTipoMovimentacao(texto, banco);
		inicializarMenuTriagemPadrao();
	}

	public void selecionarTipoMovimentacao() {
		tipoMovimentacao.setText(tabelaTipoMovimento.getSelectionModel().getSelectedItem().getTEXTO());
	}

	public void alterarTipoMovimentacao() throws IOException {
		ChavesCondicao chave = new ChavesCondicao();
		chave.setTIPO("PRO");
		chave.setTEXTO(
				tabelaTipoMovimento.getSelectionModel().getSelectedItem().getTEXTO().replace("'", "").replace("´", ""));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagEdicaoConfiguracao.fxml"));
		loader.setController(new ControllerTagEdicaoCondicao(chave));
		Parent root = loader.load();
		Stage stage = new Stage();

		stage.setTitle("Editar Condição");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(new Scene(root));
		stage.show();
	}
	public void buscaPasta() {
		String textoBusca = buscaPasta.getText().toUpperCase();
		if (textoBusca.isEmpty()) {
			inicializarMenuTriagemPadrao();
		} else {
			List<ChavesPastaCondicao> pastas = new TipoMovimentacaoDAO().buscarPasta(textoBusca);
			colunaPasta.setCellValueFactory(new PropertyValueFactory<ChavesPastaCondicao, String>("nome"));
			ObservableList<ChavesPastaCondicao> tabelaPastas = FXCollections.observableArrayList(pastas);
			tabelaPasta.setItems(tabelaPastas);
			numeroTipoMovimentacao.setText(String.valueOf(tabelaTipoMovimento.getItems().size()));
		}
	}

	public void selecionarPasta() {
		nomePasta.setText(tabelaPasta.getSelectionModel().getSelectedItem().getNome());
	}

	public void excluirPasta() {
		String texto = tabelaPasta.getSelectionModel().getSelectedItem().getNome();
		new TipoMovimentacaoDAO().removerPasta(texto);
		inicializarMenuTriagemPadrao();
	}

	public void inserirPasta() {
		String texto = nomePasta.getText().toUpperCase();
		new TipoMovimentacaoDAO().inserirPasta(texto);
		inicializarMenuTriagemPadrao();
	}

	/* Bancos de Dados */

	public void inserirBanco() {
		String sigla = textoSigla.getText();
		String banco = textoBanco.getText();
		if (sigla.isEmpty() || banco.isEmpty()) {
			new ControllerAviso().exibir("Revise os campos!");
		} else {
			if (sigla.length() > 3) {
				new ControllerAviso().exibir("Sigla não pode conter mais que 3 caracteres");
				return;
			}
			new BancosDAO().inserirBanco(sigla, banco);
			inicializarBancoDeDados();
			inicializarMenuTriagemPadrao();
			inicializarMenuPeticaoInicial();
		}
	}

	public void excluirBanco() {
		String sigla = tabelaBancos.getSelectionModel().getSelectedItem().getSigla();
		new BancosDAO().removerBanco(sigla);
		inicializarBancoDeDados();
		inicializarMenuTriagemPadrao();
		inicializarMenuPeticaoInicial();
	}

	public void selecionarBanco() {
		textoSigla.setText(tabelaBancos.getSelectionModel().getSelectedItem().getSigla());
		textoBanco.setText(tabelaBancos.getSelectionModel().getSelectedItem().getNome());
	}

	/* Usuários */

	public void atualizarUsuario() {
		String antigoNome = tabelaUsuarios.getSelectionModel().getSelectedItem().getNome();
		String nome = textoNomeUsuario.getText();
		String senha = "";

		if (exibirSenha.isSelected()) {
			senha = textoSenhaUsuarioVisivel.getText();
		} else {
			senha = textoSenhaUsuario.getText();
		}

		if (antigoNome.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
			new ControllerAviso().exibir("Selecione um usuário e não deixe nenhum campo vazio");
		} else {
			new UsuarioLocalDAO().atualizarUsuario(nome, senha, antigoNome);
		}
		inicializarUsuarios();
	}

	public void selecionarUsuario() {
		btnAtualizarUsuario.setDisable(false);
		textoNomeUsuario.setText(tabelaUsuarios.getSelectionModel().getSelectedItem().getNome());
		textoSenhaUsuario.setText(tabelaUsuarios.getSelectionModel().getSelectedItem().getSenha());
	}

	public void toggleSenha() {
		if (textoSenhaUsuario.isVisible()) {
			textoSenhaUsuarioVisivel.setVisible(true);
			textoSenhaUsuarioVisivel.setText(textoSenhaUsuario.getText());
			textoSenhaUsuario.setVisible(false);
		} else {
			textoSenhaUsuarioVisivel.setVisible(false);
			textoSenhaUsuario.setText(textoSenhaUsuarioVisivel.getText());
			textoSenhaUsuario.setVisible(true);
		}
	}

}
