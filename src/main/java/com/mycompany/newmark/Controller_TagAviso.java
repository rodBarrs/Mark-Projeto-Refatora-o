/**
 * 
 */
package com.mycompany.newmark;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Controller_TagAviso implements Initializable {
    private String textoAviso;
    
    public Controller_TagAviso(String tetoAviso){
        this.textoAviso = tetoAviso;
    }
    
    @FXML
    JFXTextArea textoSaida;
    @FXML
    JFXButton ok;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textoSaida.setText(textoAviso);
    }
    
    @FXML
    public void ok(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        stage.close();
    }
    
    public void apertouEnter(KeyEvent event) throws InterruptedException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = null;
            stage.close();
        }
    }
}