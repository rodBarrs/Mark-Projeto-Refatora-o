/**
 * @author Felipe Marques, João Paulo, Gabriel Ramos, Rafael Henrique
 * 
 * Classe de chaves para CABECALHO e PROVJURI
 */

package com.mycompany.newmark;

public class Chaves_GrupoEtiquetas {
    private String sigla = "";
    private String nome = "";
    private int qntEtiquetas = 0;

    public String getSigla() {
        return sigla;
    }
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQntEtiquetas() {
        return qntEtiquetas;
    }
    public void setQntEtiquetas(int qntEtiquetas) {
        this.qntEtiquetas = qntEtiquetas;
    }
    
}