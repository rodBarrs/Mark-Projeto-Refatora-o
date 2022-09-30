/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
package com.mycompany.newmark;

import org.openqa.selenium.WebDriver;

public class Chaves_Resultado {
	private static String seqPeticao;
	private static String palavraChavePeticao;
    private String PalavraChave = "";
    private String Etiqueta = "";
    private String subnucleo = "";
    private String Local = "";
    private Integer id;
    private WebDriver driver;
    private boolean grid;
    
    public static String getSeqPeticao() {
		return seqPeticao;
	}

	public static void setSeqPeticao(String seqPeticao) {
		Chaves_Resultado.seqPeticao = seqPeticao;
	}

	public static String getPalavraChavePeticao() {
		return palavraChavePeticao;
	}

	public static void setPalavraChavePeticao(String palavraChavePeticao) {
		Chaves_Resultado.palavraChavePeticao = palavraChavePeticao;
	}

	public String getPalavraChave() {
        return PalavraChave;
    }

    public void setPalavraChave(String PalavraChave) {
        this.PalavraChave = PalavraChave;
    }

    public String getEtiqueta() {
        return Etiqueta;
    }

    public void setEtiqueta(String Etiqueta) {
        this.Etiqueta = Etiqueta;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String Local) {
        this.Local = Local;
    } 

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isGrid() {
        return grid;
    }

    public void setGrid(boolean grid) {
        this.grid = grid;
    }

	public String getSubnucleo() {
		return subnucleo;
	}

	public void setSubnucleo(String subnucleo) {
		this.subnucleo = subnucleo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    
}