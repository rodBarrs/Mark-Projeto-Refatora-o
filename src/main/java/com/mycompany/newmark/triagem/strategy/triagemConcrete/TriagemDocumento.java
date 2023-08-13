package com.mycompany.newmark.triagem.strategy;

import com.mycompany.newmark.models.Chaves_Configuracao;
import com.mycompany.newmark.models.Chaves_Resultado;
import com.mycompany.newmark.pdf.LeituraPDF;
import com.mycompany.newmark.triagem.TriagemPasta;
import com.mycompany.newmark.triagem.Triagem_Condicao;
import com.mycompany.newmark.triagem.Triagem_Etiquetas;
import com.mycompany.newmark.triagem.VerificarData;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TriagemDocumento implements TriagemStrategy {

    @Override
    public Chaves_Resultado realizarTriagem(WebDriver driver, WebDriverWait wait, Chaves_Configuracao configuracao, String bancos) throws SQLException, IOException, InterruptedException, UnsupportedFlavorException {
        LeituraPDF pdf = new LeituraPDF();
        Chaves_Resultado resultado = new Chaves_Resultado();
        //resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA");
        Triagem_Etiquetas triagem = new Triagem_Etiquetas();
        VerificarData verificarData = new VerificarData();
        Triagem_Condicao condicao = new Triagem_Condicao();
        TriagemPasta triagemPasta = new TriagemPasta();
        String linhaMovimentacao = "";
        String condicaoProv = "PRO";
        String condicaoCabecalho = "CAB";
        String localTriagem = "DOC";
        String processo = "";

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));

        WebElement TabelaTref = driver.findElement(By.id("treeview-10" +
                "15"));
        List<WebElement> listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));

        int limite = 10;
        WebElement movimentacaoAtual;
        for (int i = listaMovimentacao.size(); i > 0 && limite > 0; i--) {
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td[2]/div")));
            movimentacaoAtual = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div"));
            if (verificarData.verificar(movimentacaoAtual.getText(), configuracao.getIntervaloDias())) {
                if (condicao.verificaCondicao(movimentacaoAtual.getText(), condicaoProv, bancos)) {
                    if ((!configuracao.isJuntManual() == false && !movimentacaoAtual.getText().contains("PDF"))
                            || (configuracao.isJuntManual() == true)) {

                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td/div")));
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ext-gen1020")));

                        // Armazena o span que contém o texto onde diz se o item é um PDF ou HTML
                        String spanText = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[2]"))
                                .getText().toUpperCase();


                        Boolean existePasta = driver.findElement(By.xpath("//tr["+i+"]/td[2]/div/img[1]")).getAttribute("class")
                                .contains("x-tree-expander");
                        if (existePasta){
                            limite--;
                            resultado = triagemPasta.documentoPasta(driver, wait, configuracao, bancos,i, resultado, pdf);
                            if (!resultado.getEtiqueta().equals("") && !resultado.getEtiqueta().contains("NÃO FOI POSSÍVEL")){
                                return resultado;
                            }
                        }
                        // Verifica se o documento é um pdf para tratamento apropriado
                        else if (spanText.contains("PDF")) {
                            pdf.apagarPDF();
                            // Click na linha
                            driver.findElement(By.xpath("//tr[" + i + "]/td/div")).click();

                            int cont = 0;
                            while (cont <= 2) {

                                if (pdf.PDFBaixado()) {
                                    processo = pdf.lerPDF();
                                    Actions action = new Actions(driver);
                                    action.sendKeys(Keys.ESCAPE);
                                    break;
                                } else {
                                    pdf.apagarPDF();
                                    driver.findElement(By.xpath("//tr[" + i + "]/td/div")).click();
                                }
                                cont++;


                            }

                            List<String> janela = new ArrayList(driver.getWindowHandles());
                            driver.switchTo().window(janela.get(1)).close();
                            driver.switchTo().window(janela.get(0));
                            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[starts-with(@id,'edicaotarefawindow')]")));
                            driver.findElement(By.xpath("//tr[1]/td[3]/div/a")).click();
                            List<String> janela2 = new ArrayList(driver.getWindowHandles());
                            driver.switchTo().window(janela2.get(1));
                            boolean flag = false;
                            while (!flag) {
                                try {
                                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("treeview-1015")));
                                    wait.until(ExpectedConditions.elementToBeClickable(By.id("treeview-1015")));
                                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("treeview-1015-body")));
                                    wait.until(ExpectedConditions.elementToBeClickable(By.id("treeview-1015-body")));
                                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")));
                                    flag = true;
                                } catch (Exception e) {

                                }
                            }

                        } else {
                            WebElement ele = driver.findElement(By.xpath("//tr[" + i + "]/td/div"));
                            ele.click();

                            // Envia o driver para o iframe e verifca os itens internos para confirmação do
                            // carregamento
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
                            WebElement iframe = driver.findElement(By.id("iframe-myiframe"));
                            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
                            // Garante o clique no iframe
                            boolean flag = true;
                            do {
                                try {

                                    wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                                    driver.findElement(By.tagName("html")).click();


                                    flag = false;
                                } catch (Exception e) {
                                    // Nothing to do at all
                                }

                            } while (flag);

                            Actions action = new Actions(driver);
//
                            driver.findElement(By.tagName("html")).click();
                            driver.findElement(By.tagName("body")).click();
                            action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
                            action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();

                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            DataFlavor flavor = DataFlavor.stringFlavor;
                            Thread.sleep(500);
                            processo = clipboard.getData(flavor).toString();
                        }
                        if(!existePasta) {


                            if (processo.length() > 1) {

                                limite--;
                                try {
                                    Boolean identificadoDePeticao = false;
                                    resultado = triagem.triarBanco(processo, bancos, localTriagem, configuracao.getTipoTriagem(),
                                            identificadoDePeticao);
                                    if (!resultado.getEtiqueta()
                                            .contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")
                                            && !resultado.getEtiqueta().contains("ERRO EM TRIAGEM: PDF NÃO PESQUISÁVEL")) {
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
                }
            }
            // Volta pro FOR
        }
        resultado.setDriver(driver);
        return resultado;
    }
}