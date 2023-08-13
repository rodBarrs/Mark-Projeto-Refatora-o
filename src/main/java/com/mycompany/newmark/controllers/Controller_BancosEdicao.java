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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Controller_BancosEdicao implements Initializable {
    
    @FXML
    public JFXTextField textoSigla, textoBanco;
    @FXML
    public TableView<Chaves_GrupoEtiquetas> tabelaBancos;
    @FXML
    public TableColumn<Chaves_GrupoEtiquetas, String> colunaSigla, colunaNome, colunaNetiquetas;
    @FXML
    public JFXButton botaoVoltar, botaoInserir, botaoExcluir, botaoLimpar;

    public String bancoSelecionado = "";

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            atualizar();
            limpar();
        } catch (Exception ex) {
            Logger.getLogger(Controller_BancosEdicao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void retornaMenu(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        }
        catch (IOException erro)
        {
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
    private boolean inserir() throws SQLException {
        String textoAviso = "";
        Aviso aviso = new Aviso();
        String sigla = textoSigla.getText().toUpperCase();
        String nome = textoBanco.getText().toUpperCase();
        Banco banco = new Banco();
        Chaves_Condicao chave = new Chaves_Condicao();
        if (sigla == null || sigla.equals("") || sigla.equals(" ")) {
            textoAviso = "O campo \"Sigla\" não pode ser vazio!";
            aviso.aviso(textoAviso);
            return false;
        } else if(nome == null || nome.equals("") || nome.equals(" ")) {
            textoAviso = "O campo \"Nome do banco\" não pode ser vazio!";
            aviso.aviso(textoAviso);
            return false;
        } else if(sigla.length()>3) {
            textoAviso = "O campo \"Sigla\" não pode conter mais que 3 caracteres!";
            aviso.aviso(textoAviso);
            return false;
        } else {
            chave.setTIPO(textoSigla.getText().toUpperCase().trim().replace("'", "").replace("´", ""));
            chave.setTEXTO(textoBanco.getText().toUpperCase().trim().replace("'", "").replace("´", ""));
            banco.inserirBancos(chave);
            atualizar();
            limpar();
        }
        return true;
    }

    @FXML
    public void excluir() throws SQLException, IOException {
        if(textoSigla.getText().equals("") || textoBanco.getText().equals("")){
            //Não faz nada
        } else {
            
            Chaves_Condicao chave = new Chaves_Condicao();
            chave.setTIPO(textoSigla.getText());
            chave.setTEXTO(textoBanco.getText());
            Aviso aviso = new Aviso();
            aviso.confirmacaoBanco(chave);
            limpar();
        }
    }

    public void atualizar() throws SQLException {
        try {
            List<Chaves_GrupoEtiquetas> chaves = new ArrayList<>();
            Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BANCOS ORDER BY NOME");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Chaves_GrupoEtiquetas key = new Chaves_GrupoEtiquetas();
                key.setSigla(resultSet.getString("SIGLA"));
                key.setNome(resultSet.getString("NOME"));
                key.setQntEtiquetas(contarEtiquetas(resultSet.getString("SIGLA")));
                chaves.add(key);
            }
            colunaSigla.setCellValueFactory(new PropertyValueFactory<Chaves_GrupoEtiquetas, String>("sigla"));
            colunaNome.setCellValueFactory(new PropertyValueFactory<Chaves_GrupoEtiquetas, String>("nome"));
            colunaNetiquetas.setCellValueFactory(new PropertyValueFactory<Chaves_GrupoEtiquetas, String>("qntEtiquetas"));
            ObservableList<Chaves_GrupoEtiquetas> genericos = FXCollections.observableArrayList(chaves);
            tabelaBancos.setItems(genericos);
            connection.close();
        } catch (SQLException erro) {
            Aviso aviso = new Aviso();
            String textoAviso = "" + erro.getMessage();
            aviso.aviso(textoAviso);
        }
    }
    
    public int contarEtiquetas(String sigla) {
        int i = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE BANCO = '" + sigla + "'");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                i++;
            }
            connection.close();
            
        } catch (SQLException erro) {
            Aviso aviso = new Aviso();
            String textoAviso = "" + erro.getMessage();
            aviso.aviso(textoAviso);
        }
        return i;
    }
    
    public void editarEtiquetas(ActionEvent event){
        Controller_Login abrir = new Controller_Login();
        abrir.editarEtiquetas(event);
    }
    
    public void limpar() {
        textoSigla.clear();
        textoBanco.clear();
    }

    public void selecionar() {
        limpar();
        textoSigla.setText(tabelaBancos.getSelectionModel().getSelectedItem().getSigla());
        textoBanco.setText(tabelaBancos.getSelectionModel().getSelectedItem().getNome());
    }
}*/