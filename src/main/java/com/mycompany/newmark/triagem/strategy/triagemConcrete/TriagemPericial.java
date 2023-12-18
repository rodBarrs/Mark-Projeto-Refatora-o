package com.mycompany.newmark.triagem.strategy.triagemConcrete;

import com.mycompany.newmark.auxiliares.Tratamento;
import com.mycompany.newmark.auxiliares.pdf.LeituraPDF;
import com.mycompany.newmark.banco.BuscarEtiquetaBanco;
import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;
import com.mycompany.newmark.triagem.strategy.triagemAbstract.TriagemStrategy;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemAuxilioGeral;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mycompany.newmark.triagem.triagemAuxiliares.PDF.PDFTratamento.tratarPDF;

public class TriagemPericial implements TriagemStrategy {

    @Override
    public ChavesResultado realizarTriagem(WebDriver driver, WebDriverWait wait, ChavesConfiguracao configuracao, String bancos) throws Exception {
        ChavesResultado resultado = new ChavesResultado();
        Tratamento tratamento = new Tratamento();
        BuscarEtiquetaBanco triagem = new BuscarEtiquetaBanco();
        LeituraPDF pdf = new LeituraPDF();
        TriagemAuxilioGeral geral = new TriagemAuxilioGeral();
        String linhaMovimentacao = "";
        String processo = "";
        resultado.setLocal("LAUDO PERICIAL");
        resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR LAUDO PERICIAL");
        resultado.setPalavraChave("");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));

        WebElement TabelaTref = driver.findElement(By.id("treeview-10" +
                "15"));
        List<WebElement> listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));
        


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
                    processo = tratarPDF(driver, wait, pdf, processo, i);
                } else {
                    processo = geral.tratarHMTL(driver, wait, i);
                }



                processo = geral.copiarConteudo(driver);

                processo = tratamento.tratamento(processo);

                //If - Verifica se existe o termo "Pericial" na var processo para seguir a tragem especifica
                if (processo.contains("PERICIAL")
                        || processo.contains("PARECER")
                        || processo.contains("SIMPLIFICADA")) { //CADASTRAR POSSIVEIS VERIFICAÇÕES
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

                                    processo = geral.copiarConteudo(driver);
                                    processo = tratamento.tratamento(processo);
                                    Boolean identificadoDePeticao = false;
                                    resultado = triagem.triarBanco(processo, bancos, resultado, configuracao);
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
