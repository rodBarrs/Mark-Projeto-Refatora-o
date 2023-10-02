package com.mycompany.newmark.triagem.strategy.triagemAbstract;
import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface TriagemStrategy {
    ChavesResultado realizarTriagem(WebDriver driver, WebDriverWait wait, ChavesConfiguracao configuracao, String bancos) throws Exception;


}
