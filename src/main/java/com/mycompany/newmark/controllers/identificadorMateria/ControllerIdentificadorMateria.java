package com.mycompany.newmark.controllers.identificadorMateria;

import com.jfoenix.controls.*;
import com.mycompany.newmark.banco.DAO.IdentificadorMateriaDAO;
import com.mycompany.newmark.controllers.ControllerAdministracao;
import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.controllers.ControllerTagEdicaoMateria;
import com.mycompany.newmark.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class ControllerIdentificadorMateria {
    ControllerAdministracao administracao = new ControllerAdministracao();
    @FXML
    private JFXTextField identificadorEtiqueta, numeroEtiquetas, numeroTipoMovimentacao, numeroIdentificadorMateria,
            numeroPeticaoInicial, pedido, complementoPedido, buscaIdentificadorMateriaID, buscaIdentificadorMateria,
            identificadorPeticaoInicial, pesquisaIdentificador, pesquisaEtiqueta, pesquisaEtiquetaId, palavraChave,
            complemento, etiqueta, buscaTipoMovimentacao,buscaPasta, nomePasta, tipoMovimentacao, textoSigla, textoBanco, textoNomeUsuario,
            textoSenhaUsuarioVisivel;
    @FXML
    private JFXPasswordField textoSenhaUsuario;
    @FXML
    private JFXComboBox<String> comboBoxBancos, comboBoxNucleo, comboBoxBancosMov;
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
            administracao.inicializarMenuPeticaoInicial();
        } else {
            new ControllerAviso().exibir("Revise os campos preenchidos");
        }

        administracao.inicializarMenuTriagemPadrao();
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
        administracao.inicializarMenuPeticaoInicial();
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
            administracao.inicializarMenuPeticaoInicial();
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
            administracao.inicializarMenuPeticaoInicial();
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
}
