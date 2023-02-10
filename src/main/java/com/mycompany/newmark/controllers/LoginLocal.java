package com.mycompany.newmark.controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Controller_Login;
import com.mycompany.newmark.DAO.UsuarioLocalDAO;
import com.mycompany.newmark.entities.UsuarioLocal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginLocal{

	@FXML
	private SVGPath btnExibirSenha, btnEsconderSenha;
	@FXML
	private JFXTextField labelUsuario, passwordUsuarioVisivel;
	@FXML
	private JFXPasswordField passwordUsuario;
	@FXML
	private JFXButton btnLogar;
	@FXML
	private JFXCheckBox radialExibirSenha;

	
	public void abrirPopupLogin() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginLocal.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("/fxml/Imagens/iconeMark.png"));
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
	}
	
	@FXML
	private void logarUsuario(ActionEvent event) throws IOException {
		String usuario = labelUsuario.getText();
		String senha = "";
		if(radialExibirSenha.isSelected()) {
			senha = passwordUsuarioVisivel.getText();
		} else {
			senha = passwordUsuario.getText();
		}
		Boolean credencialCorreta = new UsuarioLocalDAO().logarUsuario(usuario, senha);
		Stage stage = (Stage) btnLogar.getScene().getWindow();
        stage.close();
        if(credencialCorreta) {
        	UsuarioLocal.setEstaLogado(true);
        	new Controller_Login().abrirJanelaAdministracao();
        } else {
        	new Aviso().aviso("Credenciais inválidas");
        }
	}
	
	@FXML
	public void toggleExibicaoSenha() {
		if(passwordUsuarioVisivel.isVisible()) {
			passwordUsuario.setText(passwordUsuarioVisivel.getText());
			passwordUsuario.setVisible(true);
			passwordUsuarioVisivel.setVisible(false);
		} else {
			passwordUsuario.setVisible(false);
			passwordUsuarioVisivel.setVisible(true);
			passwordUsuarioVisivel.setText(passwordUsuario.getText());
		}
		
		
		
	}

	

}
