package com.mycompany.newmark.triagem.strategy.triagemConcrete;

import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;
import com.mycompany.newmark.banco.BuscarCondicaoBanco;
import com.mycompany.newmark.banco.BuscarEtiquetaBanco;
import com.mycompany.newmark.auxiliares.data.VerificarData;
import com.mycompany.newmark.triagem.strategy.triagemAbstract.TriagemStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TriagemMovimentacao implements TriagemStrategy {

    @Override
    public ChavesResultado realizarTriagem(WebDriver driver, WebDriverWait wait, ChavesConfiguracao configuracao, String bancos) throws SQLException {
        ChavesResultado resultado = new ChavesResultado();
        resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA");
        BuscarEtiquetaBanco triagem = new BuscarEtiquetaBanco();
        VerificarData verificarData = new VerificarData();
        BuscarCondicaoBanco condicao = new BuscarCondicaoBanco();
        String linhaMovimentacao = "";
        String condicaoProv = "PRO";
        String localTriagem = "MOV";

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));

        WebElement TabelaTref = driver.findElement(By.id("treeview-10" +
                "15"));
        List<WebElement> listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));

        int limite = 10;
        WebElement movimentacaoAtual;
        for (int i = listaMovimentacao.size(); i>0 && limite>0; i--) {
            movimentacaoAtual = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div"));
            if (verificarData.verificar(movimentacaoAtual.getText(), configuracao.getIntervaloDias()) /*|| config.isTriarAntigo()*/) {
                if (condicao.verificaCondicao(movimentacaoAtual.getText(),condicaoProv,bancos)){
                    limite--;
                    Boolean identificadoDePeticao = false;
                    resultado = triagem.triarBanco(movimentacaoAtual.getText(), bancos, resultado, configuracao);
                    if (!resultado.getEtiqueta().contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")
                            && !resultado.getEtiqueta().contains("ERRO EM TRIAGEM: PDF NÃO PESQUISÁVEL")) {
                        linhaMovimentacao = driver.findElement(By.xpath("//tr[" + i + "]/td/div")).getText();
                        resultado.setLocal("MOV " +  linhaMovimentacao);
                        resultado.setDriver(driver);
                        return resultado;
                    }
                }
            }
            //Volta pro FOR
        }
        resultado.setDriver(driver);
        return resultado;
    }


}
