package com.mycompany.newmark.triagem.strategy.triagemConcrete;

import com.mycompany.newmark.auxiliares.Tratamento;
import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;
import com.mycompany.newmark.auxiliares.pdf.LeituraPDF;
import com.mycompany.newmark.banco.BuscarCondicaoBanco;
import com.mycompany.newmark.banco.BuscarEtiquetaBanco;
import com.mycompany.newmark.triagem.strategy.triagemAbstract.TriagemStrategy;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemAuxilioGeral;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemCopiarConteudo;
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
import java.util.ArrayList;
import java.util.List;

public class TriagemPeticaoInicial implements TriagemStrategy {
    private String localTriagem = "PET";
    TriagemStrategy triagemStrategy = null;
    TriagemAuxilioGeral geral = new TriagemAuxilioGeral();

    @Override
    public ChavesResultado realizarTriagem(WebDriver driver, WebDriverWait wait, ChavesConfiguracao configuracao, String bancos) throws Exception {
        Tratamento tratamento = new Tratamento();
        BuscarCondicaoBanco cond = new BuscarCondicaoBanco();
        ChavesResultado resultado = new ChavesResultado();
        LeituraPDF pdf = new LeituraPDF();
        Actions action = new Actions(driver);
        String documentoPeticaoInicial = "";
        String orgaoJulgador = "";
        TriagemCopiarConteudo triagemCopiarConteudo = new TriagemCopiarConteudo();

        List<WebElement> listaMovimentacao = carregarPage(driver, wait);

        orgaoJulgador = carregarOrgaoNaDiv(driver, wait);

        // Devolve o driver para a página
        driver.switchTo().defaultContent();

        // Seta previamente a etiqueta com erro
        resultado.setLocal("PETIÇÃO INICIAL");
        resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR PASTA DE PETIÇÃO INICIAL");
        resultado.setPalavraChave("");

        // Verifica a existência de uma pasta na posição 1 do sapiens (isso significaria
        // que existe uma possível petição inicial com nome diferente)
        Boolean existePasta = driver.findElement(By.xpath("//tr[2]/td[2]/div/img[1]")).getAttribute("class")
                .contains("x-tree-expander");
        // Itera a lista de movimentação procurando por "Petição Inicial" ou uma pasta
        // no index 1
        for (int i = 2; i < listaMovimentacao.size(); i++) {

            // Providência Jurídica é o título da movimentação
            Boolean existePeticaoInicial = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText()
                    .toUpperCase().contains("PETIÇÃO INICIAL");
            if (existePeticaoInicial || existePasta) {

                resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR ARQUIVO DE PETIÇÃO INICIAL");

                // Clica na div da Providência Jurídica

                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ext-gen1020")));
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[\" + i + \"]/td/div/span/span[1]")));
                driver.findElement(By.xpath("//tr[" + i + "]/td/div")).click();

                // Verifica se a movimentação é do tipo PDF
                Boolean movimentacaoTemPDF = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[2]"))
                        .getText().toUpperCase().contains("PDF");

                // CAPA
                if (movimentacaoTemPDF) {

                    pdf.apagarPDF();

                    for (int a = 0; a < 2; a++) {
                        if (pdf.PDFBaixado()) {
                            documentoPeticaoInicial = pdf.lerPDF();
                            documentoPeticaoInicial = tratamento.tratamento(documentoPeticaoInicial);
                            break;
                        } else {

                            driver.findElement(By.xpath("//tr[" + i + "]/td/div")).click();

                        }
                    }

                } else {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
                    WebElement iframe = driver.findElement(By.id("iframe-myiframe"));
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));

                    // Laço para garantir que o clique seja feito no HTML dentro do iframe
                    // Isso garante que o conteúdo do iframe que contém o documento da movimentação
                    // clicada já carregou
                    boolean flag = true;
                    do {
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                            driver.findElement(By.tagName("html")).click();
                            flag = false;
                        } catch (Exception e) {
                            //
                        }
                    } while (flag);

                    driver.switchTo().defaultContent();

                    Thread.sleep(500);
                    action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
                    action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    DataFlavor flavor = DataFlavor.stringFlavor;
                    Thread.sleep(500);
                    documentoPeticaoInicial = clipboard.getData(flavor).toString().toUpperCase();
                    documentoPeticaoInicial = tratamento.tratamento(documentoPeticaoInicial);
                }

                Boolean contemPeticaoInicial = cond.verificaCondicao(documentoPeticaoInicial, "PET", bancos);
                if (Boolean.FALSE.equals(contemPeticaoInicial)) {

                    int proximaLinha = Integer
                            .parseInt(driver.findElement(By.xpath("//tr[" + (i + 1) + "]/td/div")).getText()) + 1;

                    // Clica na seta para expandir a pasta
                    driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")).click();

                    // Itera pela pasta
                    for (int j = i + 1; j < proximaLinha; j++) {
                        pdf.apagarPDF();
                        wait.until(ExpectedConditions
                                .presenceOfElementLocated(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")));
                        wait.until(ExpectedConditions
                                .elementToBeClickable(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")));

                        Thread.sleep(500);

                        wait.until(
                                ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"ext-gen1124\"]")));
                        // Clica para abrir o PDF
                        driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div")).click();

                        Boolean movimentacaoPastaContemPDF = driver
                                .findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]")).getText()
                                .contains("PDF");

                        if (movimentacaoPastaContemPDF) {
                            Boolean pdfBaixado = pdf.PDFBaixado();

                            for (int a = 0; a < 2; a++) {
                                if (pdfBaixado) {
                                    String processo = pdf.lerPDF().toUpperCase();
                                    if (cond.verificaCondicao(processo, "PET",bancos)) {
                                        String posicaoDaPeticao = String.valueOf(j - 1);
                                        ChavesResultado.setSeqPeticao("(" + posicaoDaPeticao + ")");


                                        resultado = verificarNucleo(processo, orgaoJulgador, resultado, configuracao, bancos);
                                        System.out.println("Retorno 1 - " + resultado.getSubnucleo());


                                        String nucleo = resultado.getEtiqueta();
                                        //				 resultado = triagemPadrao(driver, wait, config, banco, i, true, nucleo);
                                        resultado.setDriver(driver);
                                        return resultado;
                                    }
                                } else {
                                    driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div")).click();
                                }
                            }

                        } else {
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
                            WebElement iframe = driver.findElement(By.id("iframe-myiframe"));
                            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));

                            boolean flag = true;
                            do {
                                try {
                                    wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                                    driver.findElement(By.tagName("html")).click();
                                    flag = false;
                                } catch (Exception e) {
                                    //
                                }

                            } while (flag);

                            driver.switchTo().defaultContent();
                            String processo = geral.copiarConteudo(driver);
                            processo = tratamento.tratamento(processo);
                            contemPeticaoInicial = cond.verificaCondicao(processo, "PET", bancos);
                            if (contemPeticaoInicial) {
                                String posicaoDaPeticao = String.valueOf(j - 1);
                                ChavesResultado.setSeqPeticao("(" + posicaoDaPeticao + ")");
                                resultado = verificarNucleo(processo, orgaoJulgador,resultado,configuracao, bancos);
                                String nucleo = resultado.getEtiqueta();
                                resultado.setDriver(driver);
                                return resultado;
                            }
                        }
                    }

                    ChavesResultado.setSeqPeticao("PETIÇÃO NÃO ENCONTRADA");
                    // resultado = triagemPadrao(driver, wait, config, banco, i, true, "");
                    resultado.setDriver(driver);
                    return resultado;

                } else {
                    String posicaoDaPeticao = String.valueOf(i - 1);
                    ChavesResultado.setSeqPeticao("(" + posicaoDaPeticao + ")");
                    resultado = verificarNucleo(documentoPeticaoInicial, orgaoJulgador, resultado, configuracao, bancos);
                    resultado.setDriver(driver);
                    return resultado;
                }

            }
        }
        ChavesResultado.setSeqPeticao("PETIÇÃO NÃO ENCONTRADA");
        resultado.setDriver(driver);
        return resultado;

    }

    private String carregarOrgaoNaDiv(WebDriver driver, WebDriverWait wait) throws Exception {
        String orgaoJulgador;
        int x = 4;
        try {
            wait.until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath("/html/body/div/div[" + x + "]/table/tbody/tr[3]/td[2]")));
            orgaoJulgador = orgaoNaDiv(x, driver);
        } catch (Exception e) {
            orgaoJulgador = orgaoNaDiv(x + 1, driver);
        }
        return orgaoJulgador;
    }

    private static List<WebElement> carregarPage(WebDriver driver, WebDriverWait wait) {
        // Limpa conteúdo estáticos da Chaves_Resultado
        ChavesResultado.setSeqPeticao("");
        ChavesResultado.setPalavraChavePeticao("");

        // Aguarda até que tabela com as movimentações (treeview) esteja carregada
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));

        // Armazena todas as movimentações num ArrayList
        WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));
        List<WebElement> listaMovimentacao = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));

        // Aguarda até que o iframe esteja carregado e então envia o Driver para o
        // iframe (para que possa interagir com o interior do iframe)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
        WebElement capa = driver.findElement(By.id("iframe-myiframe"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(capa));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            driver.findElement(By.tagName("html")).click();
        } catch (Exception e) {
            // Lidar com a exceção, se necessário
        }

// Aguarda até que o campo "órgão julgador" esteja carregado e então salva seu conteúdo
        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        driver.findElement(By.tagName("html")).click();

        return listaMovimentacao;
    }

    private String orgaoNaDiv(int x, WebDriver driver) throws Exception {
        String orgaoJulgador = null;
        try {
            orgaoJulgador = driver.findElement(By.xpath("/html/body/div/div[" + x + "]/table/tbody/tr[3]/td[2]"))
                    .getText();
            return orgaoJulgador;
        } catch (Exception e) {
            x++;
            if (x < 15) {
                return orgaoJulgador = orgaoNaDiv(x, driver);
            } else {
                throw new Exception("Não achou o órgão julgador");
            }
        }
    }

    private ChavesResultado verificarNucleo(String processo, String orgaoJulgador, ChavesResultado resultado, ChavesConfiguracao configuracao, String banco) {
        BuscarEtiquetaBanco triagem = new BuscarEtiquetaBanco();
        // Identifica a matéria e salva na variável resultado

        resultado = triagem.triarBanco(processo, banco, resultado, configuracao);
        ChavesResultado.setPalavraChavePeticao(resultado.getPalavraChave());

        Boolean SSEASValido = validarSSEAS(resultado.getSubnucleo(), orgaoJulgador);
        Boolean SBIValido = validarSBI(resultado.getSubnucleo(), orgaoJulgador);
        Boolean TRUValido = validarTRU(orgaoJulgador);
        Boolean naoFoiPossivel = resultado.getEtiqueta().toUpperCase().contains("NÃO FOI POSSÍVEL");

        if (naoFoiPossivel) {
            return resultado;
        } else if (SSEASValido || SBIValido) {
            atualizarEtiqueta(resultado);
            return resultado;
        } else if (TRUValido) {
            resultado.setSubnucleo("ER-TRU");
            atualizarEtiqueta(resultado);
            return resultado;
        }
        resultado.setSubnucleo("PREV/LOCAL");
        atualizarEtiqueta(resultado);
        return resultado;
    }

    private Boolean validarSSEAS(String subnucleo, String orgaoJulgador) {
        return subnucleo.contains("ER-SEAS") && (orgaoJulgador.contains("JUIZADO ESPECIAL") || orgaoJulgador.contains("VARA FEDERAL") || orgaoJulgador.contains("JEF"));
    }

    private Boolean validarSBI(String subnucleo, String orgaoJulgador) {
        return subnucleo.toUpperCase().contains("ETR-BI") && orgaoJulgador.toUpperCase().contains("JUIZADO ESPECIAL");
    }

    private Boolean validarTRU(String orgaoJulgador) {
        return orgaoJulgador.toUpperCase().contains("FEDERAL");
    }

    private void atualizarEtiqueta(ChavesResultado resultado) {
        if (resultado.getEtiqueta().isEmpty()) {
            resultado.setEtiqueta(resultado.getSubnucleo());
        } else {
            resultado.setEtiqueta(resultado.getSubnucleo() + "/ " + resultado.getEtiqueta());
        }
        System.out.println("Subnúcleo colocado - " + resultado.getSubnucleo());
    }

}

