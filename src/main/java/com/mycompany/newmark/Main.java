/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
package com.mycompany.newmark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega o layout FXML
            Pane root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            // Cria a cena
            Scene scene = new Scene(root);
            // Define parâmetros para a janela
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(500);
            primaryStage.getIcons().add(new Image("/fxml/Imagens/iconeMark.png"));
            primaryStage.setTitle("Sistema de Triagem Mark");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception erro) {
            Aviso aviso = new Aviso();
            aviso.aviso(erro.getMessage());
            erro.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Banco iniciarBanco = new Banco();
        iniciarBanco.conectar();
        launch(args);
    }

}