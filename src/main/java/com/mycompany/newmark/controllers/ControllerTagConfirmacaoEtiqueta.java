/**
 * 
 */
package com.mycompany.newmark.controllers;

import com.jfoenix.controls.JFXButton;
import com.mycompany.newmark.models.ChavesBanco;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class ControllerTagConfirmacaoEtiqueta {
    ChavesBanco chave = new ChavesBanco();
    
    public ControllerTagConfirmacaoEtiqueta(ChavesBanco chave){
        this.chave = chave;
    }
    
    @FXML
    JFXButton ok, cancelar;
    

    
    @FXML
    public void cancelar(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        stage.close();
    }
}