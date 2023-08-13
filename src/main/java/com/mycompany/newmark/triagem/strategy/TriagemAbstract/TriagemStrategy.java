package com.mycompany.newmark.triagem.strategy;
import com.mycompany.newmark.models.Chaves_Configuracao;
import com.mycompany.newmark.models.Chaves_Resultado;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.SQLException;

public interface TriagemStrategy {
    Chaves_Resultado realizarTriagem(WebDriver driver, WebDriverWait wait, Chaves_Configuracao configuracao, String bancos) throws Exception;


}
