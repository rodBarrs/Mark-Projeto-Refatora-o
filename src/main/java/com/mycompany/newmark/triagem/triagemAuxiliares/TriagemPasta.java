package com.mycompany.newmark.triagem.triagemAuxiliares;

import com.mycompany.newmark.auxiliares.pdf.LeituraPDF;
import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;
import com.mycompany.newmark.banco.BuscarEtiquetaBanco;
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

public class TriagemPasta {
    public ChavesResultado documentoPasta(WebDriver driver, WebDriverWait wait, ChavesConfiguracao config, String bancos, int i, ChavesResultado resultado, LeituraPDF pdf)
            throws InterruptedException, UnsupportedFlavorException, IOException, SQLException {
        TriagemAuxilioGeral geral = new TriagemAuxilioGeral();
        BuscarEtiquetaBanco triagem = new BuscarEtiquetaBanco();

        String linhaMovimentacao = "";
        String localTriagem = "DOC";
        String processo = "";
        int diferenca = 30;
        boolean existeSeguinte = false;
        int proximaLinha = 0;

        WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));



            int numeroMovimentacao = Integer.parseInt(driver.findElement(By.xpath(("/html/body/div[2]/div[2]/div/div[2]/div/div[3]/div/table/tbody/tr[" + i + "]/td[1]/div"))).getText());
            try {
                existeSeguinte = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div/div[3]/div/table/tbody/tr[" + (i + 1) + "]/td[1]/div")).getAttribute("class").contains("x-grid-cell");
                if (existeSeguinte) {
                    int numeroMovimentacaoSeguinte = Integer.parseInt(driver.findElement(By.xpath(("/html/body/div[2]/div[2]/div/div[2]/div/div[3]/div/table/tbody/tr[" + (i + 1) + "]/td[1]/div"))).getText());

                    diferenca = numeroMovimentacaoSeguinte - numeroMovimentacao;
                }
            } catch (Exception e) {

            }

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ext-gen1020")));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[\" + i + \"]/td/div/span/span[1]")));
            driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")).click();
            if (!existeSeguinte) {
                WebElement TabelaTref2 = driver.findElement(By.id("treeview-1015"));
                List<WebElement> listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));
                int numeroUltimaMovimentacao = Integer.parseInt(driver.findElement(By.xpath(("/html/body/div[2]/div[2]/div/div[2]/div/div[3]/div/table/tbody/tr[" + listaMovimentacao.size() + "]/td[1]/div"))).getText());
                diferenca = (numeroUltimaMovimentacao - numeroMovimentacao) + 1;

            }

            for (int j = i; j < diferenca; j++) {


                pdf.apagarPDF();
                wait.until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")));
                wait.until(ExpectedConditions
                        .elementToBeClickable(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")));

                Thread.sleep(500);

                wait.until(
                        ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"ext-gen1124\"]")));

                // Armazena o span que contém o texto onde diz se o item é um PDF ou HTML
                String spanText = driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]"))
                        .getText().toUpperCase();

                // Verifica se o documento é um pdf para tratamento apropriado
                if (spanText.contains("PDF")) {
                    processo = tratarPDF(driver, wait, pdf, processo, i);
                } else {
                    processo = geral.tratarHMTL(driver, wait, i);
                }

                if (processo.length() > 1) {

                    try {
                        resultado = triagem.triarBanco(processo, bancos, resultado, config);
                        if (!resultado.getEtiqueta()
                                .contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")
                                && !resultado.getEtiqueta().contains("ERRO EM TRIAGEM: PDF NÃO PESQUISÁVEL")) {
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

        return resultado;
    }

    public static boolean verificarPasta(WebDriver driver, int i) {
        return driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")).getAttribute("class")
                .contains("x-tree-expander");
    }



}
