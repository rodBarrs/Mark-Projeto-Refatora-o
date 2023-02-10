/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 * Classe controladora das Janela de inserção de palavras chaves dentro do banco
 */
package com.mycompany.newmark;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller_Informacao {
    
    @FXML
    JFXButton voltar;
    
    @FXML
    void retornaMenu(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        }
        catch (IOException erro) {
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
    
}