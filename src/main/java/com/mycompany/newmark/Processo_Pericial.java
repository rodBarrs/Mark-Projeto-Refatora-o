/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
package com.mycompany.newmark;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Processo_Pericial {

    public Chaves_Resultado pericial(WebDriver driver, WebDriverWait wait, String bancos) throws InterruptedException, UnsupportedFlavorException, IOException, SQLException {
        Chaves_Resultado resultado = new Chaves_Resultado();
        Tratamento tratamento = new Tratamento();
        Triagem_Etiquetas triagem = new Triagem_Etiquetas();
        Actions action = new Actions(driver);
        String localTriagem = "DOC";
        String linhaMovimentacao = "";

        WebElement TabelaTref = null;
        boolean teste = false;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
            TabelaTref = driver.findElement(By.id("treeview-1015"));
        } catch (Exception e) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
                        TabelaTref = driver.findElement(By.id("treeview-1015"));
                        teste = true;
                        break;
                    } catch (Exception erro) {
                    }
                }
                if (teste = true) {
                    break;
                } else {
                    driver.navigate().refresh();
                }
            }
        }

        //Identifica as linhas da tabela de movimentação processual <rr> 
        List listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));

        resultado.setLocal("LAUDO PERICIAL");
        resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR LAUDO PERICIAL");
        resultado.setPalavraChave("");

        //FOR - Enquantou houve elementos na tabela, do último para o primeiro
        for (int i = listaMovimentacao.size(); i > 0; i--) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/span/span[1]")));
            //IF - Busca pelas expressões descritas, dentro das <tr> da movimentação
            if (driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[1]")).getText().toUpperCase().contains("LAUDO PERICIAL")
                    || driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[1]")).getText().toUpperCase().contains("CERTIDÃO")) {
                //Clica no <tr> identificado
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td/div")));
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ext-gen1020")));
                driver.findElement(By.xpath("//tr[" + i + "]/td/div")).click();

                //Armazena o span que contém o texto onde diz se o item é um PDF ou HTML
                String spanText = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[2]")).getText();

                // Verificação se o documento é um pdf para tratamento apropriado
                if (spanText.contains("PDF") || spanText.contains("pdf")) {
                    //Espera até que o iframe onde se carrega o PDF esteja visível
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
                    //Armazena o iframe
                    WebElement iframe = driver.findElement(By.id("iframe-myiframe"));
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
                    //Envia o driver para dentro do Switch
                    //driver.switchTo().frame(iframe);
                    //Aguarda até que os dois itens "Viewer" e "page" estejam visíveis, os dois estarem visíveis significa que o PDF carregou
                    //Esta verificação só é funcional ao utilizar o Firefox
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewer")));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("page")));
                    driver.findElement(By.id("viewerContainer")).click();
                    //Retorna o Driver para a página pai
                    driver.switchTo().defaultContent();
                } else {
                    //
                }

                //Envia o driver para o iframe e verifca os itens internos para confirmação do carregamento
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
                WebElement iframe = driver.findElement(By.id("iframe-myiframe"));
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                wait.until(ExpectedConditions.elementToBeClickable(By.tagName("body")));

                //Garante o clique no iframe
                boolean flag = true;
                do {
                    try {
                        driver.findElement(By.tagName("html")).click();
                        flag = false;
                    } catch (Exception e) {
                        // Nothing to do at all
                    }

                } while (flag);

                action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
                action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();

                driver.switchTo().defaultContent();
                
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                DataFlavor flavor = DataFlavor.stringFlavor;
                String BuscaPericial = "";
                BuscaPericial = clipboard.getData(flavor).toString();
                BuscaPericial = tratamento.tratamento(BuscaPericial);

                //If - Verifica se existe o termo "Pericial" na var BuscaPericial para seguir a tragem especifica
                if (BuscaPericial.contains("PERICIAL")
                        || BuscaPericial.contains("PARECER")
                        || BuscaPericial.contains("SIMPLIFICADA")) { //CADASTRAR POSSIVEIS VERIFICAÇÕES
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td/div")));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + (i + 1) + "]/td/div")));
                    int LinhaAtual = Integer.parseInt(driver.findElement(By.xpath("//tr[" + i + "]/td/div")).getText()); //Armazena a linha do FRONT em que está a movimentação
                    int LinhaEsperada = LinhaAtual + 1; //Armazena o valor que DEVERIA ser o seguite da linha no FRONT
                    int LinhaProxima = Integer.parseInt(driver.findElement(By.xpath("//tr[" + (i + 1) + "]/td/div")).getText()); //Armazena o valor da PROXIMA linha do FRONT
                    if (LinhaEsperada != LinhaProxima) {
                        //Identifica e clica na SETA da movimentação para expandir os documentos
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")));
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td/div/img[1]")));
                        driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")).click();
                        //Reconta a tabela de movimentação para considerar os arquivos expandidos na MESMA var que já estava sendo utilizada
                        listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));
                        //FOR - Recontar a lista de movimentação até onde foi localizado a var i
                        for (int j = listaMovimentacao.size(); j >= 0; j--) {
                            if (i == j) {
                                //Var aqui para garantir o armazenamento da informação
                                int aqui = i + 1;//Armazena a proxima LINHA a que deveria aparecer após a expansão da pasta
                                //Tempo de espera para garantir que a lista de movimentação será carregada
                                //Clica no elemento abaixo do identifiado no IF anterior
                                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + aqui + "]/td[2]/div/span/span[2]/span")));
                                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + aqui + "]/td[2]/div/span/span[2]/span")));
                                
                                driver.findElement(By.xpath("//tr[" + aqui + "]/td/div")).click();

                                //Armazena o span que contém o texto onde diz se o item é um PDF ou HTML
                                String spanText2 = driver.findElement(By.xpath("//tr[" + aqui + "]/td[2]/div/span/span[2]/span")).getText();
                                
                                //Tratamento adequado do PDF
                                if (spanText2.contains("PDF") || spanText2.contains("pdf")) {
                                    //Espera até que o iframe onde se carrega o PDF esteja visível
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
                                    //Armazena o iframe
                                    WebElement iframe2 = driver.findElement(By.id("iframe-myiframe"));
                                    //Envia o driver para dentro do Switch
                                    driver.switchTo().frame(iframe2);
                                    //Aguarda até que os dois itens "Viewer" e "page" estejam visíveis, os dois estarem visíveis significa que o PDF carregou
                                    //Esta verificação só é funcional ao utilizar o Firefox
                                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewer")));
                                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("page")));
                                    driver.findElement(By.id("viewerContainer")).click();
                                    //Retorna o Driver para a página pai
                                    driver.switchTo().defaultContent();
                                } 

                                try {
                                    //Clica no HTML onde é exibido o documento no Sapiens (TELA 2)
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
                                    WebElement iframe2 = driver.findElement(By.id("iframe-myiframe"));
                                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe2));
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                                    wait.until(ExpectedConditions.elementToBeClickable(By.tagName("body")));
                                    boolean flag2 = true;
                                    do {
                                        try {
                                            driver.findElement(By.tagName("html")).click();
                                            flag2 = false;
                                        } catch (Exception e) {
                                            // Nothing to do at all
                                        }

                                    } while (flag2);

                                    action = new Actions(driver);
                                    action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
                                    action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();

                                    driver.switchTo().defaultContent();
                                    
                                    Clipboard clipboard1 = Toolkit.getDefaultToolkit().getSystemClipboard();
                                    DataFlavor flavor1 = DataFlavor.stringFlavor;
                                    String processo = "";
                                    processo = clipboard1.getData(flavor1).toString();
                                    processo = tratamento.tratamento(processo);
                                    Boolean identificadoDePeticao = false;
                                    resultado = triagem.triarBanco(processo, bancos, localTriagem, "PERICIAL", identificadoDePeticao);
                                    linhaMovimentacao = driver.findElement(By.xpath("//tr[" + aqui + "]/td/div")).getText();
                                    resultado.setLocal("Documento (" + linhaMovimentacao + ")");
                                    resultado.setDriver(driver);
                                    return resultado;
                                } catch (UnsupportedFlavorException erro) {
                                } catch (IOException erro) {
                                }
                            }

                        }
                    }
                }
            }
        }
        resultado.setDriver(driver);
        return resultado;
    }
}