package com.mycompany.newmark.triagem.strategy.triagemConcrete;

import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;
import com.mycompany.newmark.auxiliares.pdf.LeituraPDF;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemAuxilioGeral;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemCopiarConteudo;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemPasta;
import com.mycompany.newmark.banco.BuscarCondicaoBanco;
import com.mycompany.newmark.banco.BuscarEtiquetaBanco;
import com.mycompany.newmark.auxiliares.data.VerificarData;
import com.mycompany.newmark.triagem.strategy.triagemAbstract.TriagemStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mycompany.newmark.triagem.triagemAuxiliares.PDF.PDFTratamento.tratarPDF;
import static com.mycompany.newmark.triagem.triagemAuxiliares.PDF.PDFTratamento.verificarPDF;
import static com.mycompany.newmark.triagem.triagemAuxiliares.TriagemAuxilioGeral.carregaMovimentacaoAtual;
import static com.mycompany.newmark.triagem.triagemAuxiliares.TriagemAuxilioGeral.tratarHMTL;
import static com.mycompany.newmark.triagem.triagemAuxiliares.TriagemPasta.verificarPasta;


public class TriagemDocumento implements TriagemStrategy {

    @Override
    public ChavesResultado realizarTriagem(WebDriver driver, WebDriverWait wait, ChavesConfiguracao configuracao, String bancos) throws SQLException, IOException, InterruptedException, UnsupportedFlavorException {
        LeituraPDF pdf = new LeituraPDF();
        ChavesResultado resultado = new ChavesResultado();
        //resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA");
        BuscarEtiquetaBanco triagem = new BuscarEtiquetaBanco();
        VerificarData verificarData = new VerificarData();
        BuscarCondicaoBanco condicao = new BuscarCondicaoBanco();
        TriagemAuxilioGeral geral = new TriagemAuxilioGeral();
        TriagemPasta triagemPasta = new TriagemPasta();
        String linhaMovimentacao = "";
        String condicaoProv = "PRO";
        String processo = "";

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));

        WebElement TabelaTref = driver.findElement(By.id("treeview-10" +
                "15"));
        List<WebElement> listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));

        int limite = 10;
        WebElement movimentacaoAtual;
        for (int i = listaMovimentacao.size(); i > 0 && limite > 0; i--) {
            driver.switchTo().defaultContent();
            movimentacaoAtual = carregaMovimentacaoAtual(driver, wait, i);
            boolean estaNoLimiteData = verificarData.verificar(movimentacaoAtual.getText(), configuracao.getIntervaloDias());
            boolean PossueMovimentacaoAtual = condicao.verificaCondicao(movimentacaoAtual.getText(), condicaoProv, bancos);
            if (estaNoLimiteData && PossueMovimentacaoAtual) {
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td/div")));
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ext-gen1020")));

                        Boolean isPDF = verificarPDF(driver, i);
                        Boolean existePasta = verificarPasta(driver, i);

                        if (existePasta){
                            limite--;
                            resultado = triagemPasta.documentoPasta(driver, wait, configuracao, bancos,i, resultado, pdf);
                            boolean encontrouEtiquetaNaPasta = !resultado.getEtiqueta().equals("") && !resultado.getEtiqueta().contains("NÃO FOI POSSÍVEL");
                            if (encontrouEtiquetaNaPasta){
                                return resultado;
                            }
                        }

                         if (isPDF) {
                            processo = tratarPDF(driver, wait, pdf, processo, i);
                        } else {
                            processo = tratarHMTL(driver, wait, i);
                        }

                        if(!existePasta) {
                            if (processo.length() > 1) {
                                limite--;
                                try {
                                    resultado = triagem.triarBanco(processo, bancos, resultado, configuracao);
                                    boolean encontrouEtiqueta = !resultado.getEtiqueta()
                                            .contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")
                                            && !resultado.getEtiqueta().contains("ERRO EM TRIAGEM: PDF NÃO PESQUISÁVEL");
                                    if (encontrouEtiqueta) {
                                        driver.switchTo().defaultContent();
                                        linhaMovimentacao = driver.findElement(By.xpath("//tr[" + i + "]/td/div"))
                                                .getText();
                                        resultado.setLocal("DOC " + linhaMovimentacao);
                                        resultado.setDriver(driver);
                                        return resultado;
                                    }
                                } catch (Exception erro) {
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-1005-btnEl")));
                                    driver.findElement(By.id("button-1005-btnEl")).click();
                                    erro.printStackTrace();
                                }

                            } else {
                                resultado.setEtiqueta("ERRO EM TRIAGEM: INSTABILIDADE NO SAPIENS");
                            }
                        }
                    }


            // Volta pro FOR
        }
        resultado.setDriver(driver);
        return resultado;
    }





}
