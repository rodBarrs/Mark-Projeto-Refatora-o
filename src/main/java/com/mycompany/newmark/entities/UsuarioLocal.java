package com.mycompany.newmark.entities;

public class UsuarioLocal {
	private static Boolean estaLogado = false;
	private String nome;
	private String senha;
	
	public UsuarioLocal(String nome, String senha) {
		this.nome = nome;
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public static Boolean getEstaLogado() {
		return estaLogado;
	}

	public static void setEstaLogado(Boolean estaLogado) {
		UsuarioLocal.estaLogado = estaLogado;
	}
	
	
	
	
}
