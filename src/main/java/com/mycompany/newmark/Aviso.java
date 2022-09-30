/**
 *
 * @author Felipe
 */
package com.mycompany.newmark;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Aviso {
    public void aviso(String textoAviso) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagAviso.fxml"));
            loader.setController(new Controller_TagAviso(textoAviso));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Aviso");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception erro) {
            erro.printStackTrace();
        }
    }
    
    public void confirmacaoBanco(Chaves_Condicao chave) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagConfirmacao.fxml"));
            loader.setController(new Controller_TagConfirmacaoBanco(chave));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Aviso");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception erro) {
            erro.printStackTrace();
        }
    }
    
    public void confirmacaoCondicao(Chaves_Condicao chave) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagConfirmacao.fxml"));
            loader.setController(new Controller_TagConfirmacaoCondicao(chave));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Aviso");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception erro) {
            erro.printStackTrace();
        }
    }
    
    public void confirmacaoEtiqueta(Chaves_Banco chave) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagConfirmacao.fxml"));
            loader.setController(new Controller_TagConfirmacaoEtiqueta(chave));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Aviso");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception erro) {
            erro.printStackTrace();
        }
    }
}