/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 * Classe controladora das Janela de edição de etiquetas
 */

package com.mycompany.newmark;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.DAO.EtiquetaDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class Controller_TagEdicaoEtiqueta implements Initializable {
    private Chaves_Banco chave;

    public Controller_TagEdicaoEtiqueta(Chaves_Banco chave)
    {
        this.chave = chave;
    }
    
    @FXML
    JFXTextField PalavraChave, Complemento, Etiqueta;
    @FXML
    RadioButton LerMov, LerDoc;
    @FXML
    RadioButton P1, P2, P3, P4;
    @FXML
    JFXButton salvar, cancelar, limparCampos;
    
    final ToggleGroup grupoPeso = new ToggleGroup();
    final ToggleGroup grupoTipo = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PalavraChave.setText(chave.getPALAVRACHAVE());
        Complemento.setText(chave.getCOMPLEMENTO());
        Etiqueta.setText(chave.getETIQUETA());
        if(chave.getTIPO().equals("MOV")){
            LerMov.setSelected(true);
        } else {
            LerDoc.setSelected(true);
        } 
        switch (chave.getPRIORIDADE()){
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
    }
    
    @FXML
    public boolean alterar(ActionEvent event) throws SQLException {
        String palavraChave = PalavraChave.getText().toUpperCase().replace("'", "").replace("´", "");
        String complemento = Complemento.getText().toUpperCase().replace("'", "").replace("´", "");
        String etiqueta = Etiqueta.getText().toUpperCase().replace("'", "").replace("´", "");
        String peso = identificarPeso();
        
        String textoAviso = "";
        Aviso aviso = new Aviso();
        if((palavraChave.equals(null)) || palavraChave.equals("") || palavraChave.equals(" ")){
            textoAviso = "O campo \"Frase Chave\" não pode ser vazio!";
            aviso.aviso(textoAviso);
            return false;
        } else if((etiqueta.equals(null)) || etiqueta.equals("") || etiqueta.equals(" ")){
            textoAviso = "O campo \"Etiqueta\" não pode ser vazio!";
            aviso.aviso(textoAviso);
            return false;
        } else if (!LerDoc.isSelected() && !LerMov.isSelected()) {
            textoAviso = "Selecione o local de triagem desta etiqueta (Movimentação, Documento ou Petição)!";
            aviso.aviso(textoAviso);
            return false;
        } else if (!P1.isSelected() && !P2.isSelected() && !P3.isSelected() && !P4.isSelected()) {
            textoAviso = "Selecione um peso para a etiqueta!";
            aviso.aviso(textoAviso);
            return false;
        } else {
            String tipo;
            if (LerDoc.isSelected()) {
                tipo = "DOC";
            } else {
                tipo = "MOV";
            } 
            
            //Armazena a qual banco de dados pertence a etiqueta alterada
            new EtiquetaDAO().atualizarEtiqueta(chave.getID(), palavraChave, complemento, etiqueta, peso, tipo);

            
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
        Etiqueta.clear();
        LerDoc.setSelected(true);
        P1.setSelected(true);
    }
    
    public String identificarPeso(){
        if(P1.isSelected()){
            return "1";
        } else if(P2.isSelected()){
            return "2";
        } else if(P3.isSelected()){
            return "3";
        } else if(P4.isSelected()){
            return "4";
        }
        return null;
    }
}
