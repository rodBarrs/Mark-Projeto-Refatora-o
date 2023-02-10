/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 * Classe controladora das Janela de edição de cabeçalho e providência juridica
 */
package com.mycompany.newmark;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.DAO.IdentificadorPeticaoInicialDAO;
import com.mycompany.newmark.DAO.TipoMovimentacaoDAO;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

    public class Controller_TagEdicaoCondicao implements Initializable{
    private Chaves_Condicao chave;
    
    public Controller_TagEdicaoCondicao(Chaves_Condicao chave){
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
        Aviso aviso = new Aviso();
        String textoAviso = "";
        if((novoTexto.equals(null)) || novoTexto.equals("") || novoTexto.equals(" ")) {
            textoAviso = "O campo \"Condição\" não pode ser vazio!";
            aviso.aviso(textoAviso);
            
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
