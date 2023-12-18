/**
 *
 * @author Felipe
 */
package com.mycompany.newmark.controllers;

import com.mycompany.newmark.models.ChavesBanco;
import com.mycompany.newmark.models.ChavesCondicao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerAviso {
    public void exibir(String textoAviso) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagAviso.fxml"));
            loader.setController(new ControllerTagAviso(textoAviso));
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
    
    public void confirmacaoBanco(ChavesCondicao chave) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagConfirmacao.fxml"));
            loader.setController(new ControllerTagConfirmacaoBanco(chave));
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
    
    public void confirmacaoCondicao(ChavesCondicao chave) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagConfirmacao.fxml"));
            loader.setController(new ControllerTagConfirmacaoCondicao(chave));
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
    
    public void confirmacaoEtiqueta(ChavesBanco chave) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagConfirmacao.fxml"));
            loader.setController(new ControllerTagConfirmacaoEtiqueta(chave));
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