package com.mycompany.newmark.triagem.triagemAuxiliares.PDF;
import com.mycompany.newmark.auxiliares.pdf.LeituraPDF;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PDFTratamento {

    public static String tratarPDF(WebDriver driver, WebDriverWait wait, LeituraPDF pdf, String processo, int i) throws InterruptedException, IOException {
        pdf.apagarPDF();
        clicarLinha(driver, i);
        aguardarPDFDownload(pdf, driver, i);

        if (pdf.PDFBaixado()) {
            processo = pdf.lerPDF();
            fecharJanela(driver, 1);
            voltarParaJanelaPrincipal(driver, 0, wait);
            abrirDetalhesDoProcesso(driver);
            aguardarDetalhesCarregarem(wait, i);
        }

        return processo;
    }

    public static void clicarLinha(WebDriver driver, int linha) {
        driver.findElement(By.xpath("//tr[" + linha + "]/td/div")).click();
    }

    public static void aguardarPDFDownload(LeituraPDF pdf, WebDriver driver, int i) throws InterruptedException {
        int cont = 0;
        while (cont <= 2) {
            if (pdf.PDFBaixado()) {
                break;
            } else {
                pdf.apagarPDF();
                clicarLinha(driver, i);
            }
            cont++;
        }
    }

    public static void fecharJanela(WebDriver driver, int janelaIndex) {
        List<String> janelas = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(janelas.get(janelaIndex)).close();
        driver.switchTo().window(janelas.get(0));
    }

    public static void voltarParaJanelaPrincipal(WebDriver driver, int janelaIndex, WebDriverWait wait) {
        driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(janelaIndex));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[starts-with(@id,'edicaotarefawindow')]")));
    }

    public static void abrirDetalhesDoProcesso(WebDriver driver) {
        driver.findElement(By.xpath("//tr[1]/td[3]/div/a")).click();
    }

    public static void aguardarDetalhesCarregarem(WebDriverWait wait, int i) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("treeview-1015")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("treeview-1015")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("treeview-1015-body")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("treeview-1015-body")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")));
    }

    public static Boolean verificarPDF(WebDriver driver, int i) {
        String spanText = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[2]"))
                .getText().toUpperCase();
        if(spanText.contains("PDF")){
            return true;
        } else {
            return false;
        }

    }
}
