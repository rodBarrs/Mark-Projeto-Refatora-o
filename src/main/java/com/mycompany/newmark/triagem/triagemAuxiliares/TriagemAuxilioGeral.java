package com.mycompany.newmark.triagem.triagemAuxiliares;

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

public class TriagemAuxilioGeral {

    public  String copiarConteudo(WebDriver driver) throws InterruptedException, UnsupportedFlavorException, IOException {
        String processo;
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
        return processo;
    }

    public static String tratarHMTL(WebDriver driver, WebDriverWait wait, int i) throws InterruptedException, UnsupportedFlavorException, IOException {
        String processo;
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

        processo = this.copiarConteudo(driver);
        return processo;
    }

    public static WebElement carregaMovimentacaoAtual(WebDriver driver, WebDriverWait wait, int i) {
        WebElement movimentacaoAtual;
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td[2]/div")));
        movimentacaoAtual = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div"));
        return movimentacaoAtual;
    }
}
