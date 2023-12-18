package com.mycompany.newmark.banco.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.controllers.ControllerAviso;
import com.mycompany.newmark.banco.ConnectionFactory;
import com.mycompany.newmark.models.UsuarioLocal;

public class UsuarioLocalDAO  {

	ControllerAviso controllerAviso = new ControllerAviso();

	public List<UsuarioLocal> getTabelaUsuarios() {
		final String SQL = "SELECT * FROM usuarios";
		List<UsuarioLocal> usuarios = new ArrayList<>();
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				usuarios.add(new UsuarioLocal(rs.getString("nome"), rs.getString("senha")));
			}
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		return usuarios;
	}

	public void atualizarUsuario(String nome, String senha, String antigoNome) {
		final String SQL = "UPDATE usuarios SET nome = ?, senha = ? where nome = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, nome);
			stmt.setString(2, senha);
			stmt.setString(3, antigoNome);
			stmt.execute();
			controllerAviso.exibir("Usuário atualizado");
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		
	}

	public Boolean logarUsuario(String usuario, String senha) {
		final String SQL = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";
		Boolean resultado = false;
		try(Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, usuario);
			stmt.setString(2, senha);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				UsuarioLocal usuariolocal = new UsuarioLocal(rs.getString("nome"), rs.getString("senha"));
				if(usuariolocal.getNome().equals(usuario) && usuariolocal.getSenha().equals(senha)) {
					resultado = true;
				}
			}
		} catch (Exception e) {
			controllerAviso.exibir(e.getMessage());
		}
		return resultado;
	}


}
