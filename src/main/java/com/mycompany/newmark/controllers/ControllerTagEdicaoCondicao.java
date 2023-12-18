/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 * Classe controladora das Janela de edição de cabeçalho e providência juridica
 */
package com.mycompany.newmark.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.banco.DAO.IdentificadorPeticaoInicialDAO;
import com.mycompany.newmark.banco.DAO.TipoMovimentacaoDAO;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.newmark.models.ChavesCondicao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

    public class ControllerTagEdicaoCondicao implements Initializable{
    private ChavesCondicao chave;
    
    public ControllerTagEdicaoCondicao(ChavesCondicao chave){
        this.chave = chave;
    }
    
    @FXML
    JFXTextField texto;
    @FXML
    JFXButton salvar, cancelar, limpar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        texto.setText(chave.getTEXTO());
    }
    
    @FXML
    public void alterar(javafx.event.ActionEvent event) {
        String novoTexto = texto.getText().toUpperCase().replace("'", "").replace("´", "");
        ControllerAviso controllerAviso = new ControllerAviso();
        String textoAviso = "";
        if((novoTexto.equals(null)) || novoTexto.equals("") || novoTexto.equals(" ")) {
            textoAviso = "O campo \"Condição\" não pode ser vazio!";
            controllerAviso.exibir(textoAviso);
            
        } else {
        	if(chave.getTIPO().contains("PET")) {
        		new IdentificadorPeticaoInicialDAO().atualizarIdentificadorPeticao(chave.getTEXTO(), novoTexto);
        	} else if (chave.getTIPO().contains("PRO")) {
        		new TipoMovimentacaoDAO().atualizarTipoMovimento(chave.getTEXTO(), novoTexto);
        	}
            
           
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = null;
            stage.close();
        }
    }
    
    @FXML
    public void cancelar(javafx.event.ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        stage.close();
    }
    
    @FXML
    public void limpar() {
        texto.clear();
    }
}
