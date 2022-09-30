package com.mycompany.newmark;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.DAO.IdentificadorMateriaDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class Controller_TagEdicaoMateria implements Initializable {
	
    private Chaves_Banco chave;

    public Controller_TagEdicaoMateria(Chaves_Banco chave)
    {
        this.chave = chave;
    }
    
    @FXML
    JFXTextField PalavraChave, Complemento, Etiqueta;
    @FXML
    JFXComboBox<String> ComboBoxNucleo;
    @FXML
    JFXButton salvar, cancelar, limparCampos;
    @FXML
    RadioButton P1, P2, P3, P4;
    
    final ToggleGroup grupoPeso = new ToggleGroup();
    final ToggleGroup grupoTipo = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PalavraChave.setText(chave.getPALAVRACHAVE());
        Complemento.setText(chave.getCOMPLEMENTO());
        Etiqueta.setText(chave.getETIQUETA());
        
        switch (chave.getPRIORIDADE()) {
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
        
        ObservableList<String> items = FXCollections.observableArrayList(itemComboBox());
        ComboBoxNucleo.setItems(items);
        ComboBoxNucleo.getSelectionModel().select(chave.getSubnucleo());
    }
    
    @FXML
    public boolean alterar(ActionEvent event) throws SQLException {
    	
    	Integer id = chave.getID();
    	System.out.println(id);
        String palavraChave = PalavraChave.getText().toUpperCase().replace("'", "").replace("´", "");
        String complemento = Complemento.getText().toUpperCase().replace("'", "").replace("´", "");
        String etiqueta = Etiqueta.getText().toUpperCase().replace("'", "").replace("´", "");
        String subnucleo = ComboBoxNucleo.getSelectionModel().getSelectedItem().toString();
        String prioridade = "";
        
        if(P1.isSelected()) prioridade = "1"; 
		if(P2.isSelected()) prioridade = "2"; 
		if(P3.isSelected()) prioridade = "3"; 
		if(P4.isSelected()) prioridade = "4"; 
		
        String textoAviso = "";
        Aviso aviso = new Aviso();
        if((palavraChave.equals(null)) || palavraChave.equals("") || palavraChave.equals(" ")){
            textoAviso = "O campo \"Frase Chave\" não pode ser vazio!";
            aviso.aviso(textoAviso);
            return false;
        } else if((subnucleo.equals(null)) || subnucleo.equals("") || subnucleo.equals(" ")){
            textoAviso = "O campo \"Etiqueta\" não pode ser vazio!";
            aviso.aviso(textoAviso);
            return false;
        } else {         
            //Armazena a qual banco de dados pertence a etiqueta alterada
        	new IdentificadorMateriaDAO().atualizarIndetificadorMateria(id, palavraChave, complemento, etiqueta, subnucleo, prioridade);
            
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = null; 
            stage.close();
        }
        return true;
    }

    @FXML
    public void cancelar(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        stage.close();
    }

    @FXML
    public void limpar() {
        PalavraChave.clear();
        Complemento.clear();
        ComboBoxNucleo.getSelectionModel().select(0);
    }
    
    public ObservableList<String> itemComboBox() {
    	ObservableList<String> listinha = null;
		List<String> lista = new ArrayList<>();
		lista.add("ER-SEAS");
		lista.add("ETR-BI");
		lista.add("ER-TRU");
		listinha = FXCollections.observableArrayList(lista);
		return listinha;
    }
}
