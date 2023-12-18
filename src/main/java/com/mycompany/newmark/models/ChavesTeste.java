package com.mycompany.newmark.models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChavesTeste {
    WebDriver driver;
    WebDriverWait wait;

    boolean isTeste;

    String etiquetaResultado;
    String filtro;
    String Modo;


    public String getModo() {
        return Modo;
    }

    public void setModo(String modo) {
        Modo = modo;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getEtiquetaResultado() {
        return etiquetaResultado;
    }

    public void setEtiquetaResultado(String etiquetaResultado) {
        this.etiquetaResultado = etiquetaResultado;
    }

    public boolean isTeste() {
        return isTeste;
    }

    public void setTeste(boolean teste) {
        isTeste = teste;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public void setWait(WebDriverWait wait) {
        this.wait = wait;
    }
}
