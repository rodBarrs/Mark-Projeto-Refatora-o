/**
 * 
 */
package com.mycompany.newmark;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Controller_TagConfirmacaoEtiqueta {
    Chaves_Banco chave = new Chaves_Banco();
    
    public Controller_TagConfirmacaoEtiqueta(Chaves_Banco chave){
        this.chave = chave;
    }
    
    @FXML
    JFXButton ok, cancelar;
    
    @FXML
    public void ok(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        stage.close();
        Banco banco = new Banco();
        banco.excluirEtiquetas(chave);
    }
    
    @FXML
    public void cancelar(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        stage.close();
    }
}