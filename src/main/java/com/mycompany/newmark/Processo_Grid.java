/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 *
 */
package com.mycompany.newmark;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Processo_Grid {
    private int tamanho = -1;

    public Processo_Grid(WebDriver driver) {
        super();
    }

    //Encontra os processos
    @SuppressWarnings("SleepWhileInLoop")
    public final Chaves_Resultado buscar_processo(WebDriver driver, WebDriverWait wait) throws InterruptedException, UnsupportedFlavorException, IOException, SQLException {
    	Actions actions = new Actions(driver);
    	Chaves_Resultado resultado = new Chaves_Resultado();
    	
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[starts-with(@id,'edicaotarefawindow')]")));
        if (tamanho > 1) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gridview-1109-body")));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("gridview-1109-body")));
        }

        WebElement tabela = driver.findElement(By.id("gridview-1109-table"));
        List<WebElement> tarefas = new ArrayList(tabela.findElements(By.cssSelector("tr")));
        tamanho = tarefas.size();
        if (tamanho > 0) {
            try {
                tabela = driver.findElement(By.id("gridview-1109-table"));
                tarefas = new ArrayList(tabela.findElements(By.cssSelector("tr")));
                driver.findElement(By.xpath("//tr[1]/td[3]/div/a")).click();
                try {
                    boolean flag = false;
                    while (!flag) {
                        try {
                            List<String> janela = new ArrayList(driver.getWindowHandles());
                            driver.switchTo().window(janela.get(1));
                            //Todos os waits abaixo estão verificando se o elemento existe dentro da página, para ter certeza que o Driver está na página correta
                            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("treeview-1015")));
                            wait.until(ExpectedConditions.elementToBeClickable(By.id("treeview-1015")));
                            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("treeview-1015-body")));
                            wait.until(ExpectedConditions.elementToBeClickable(By.id("treeview-1015-body")));
                            //Encerra o laço
                            flag = true;
                        } catch (Exception e) {
                            // nothing to do
                        }
                    }
                    resultado.setDriver(driver);
                    resultado.setGrid(true);
                    return resultado;
                } catch (Exception erro) {
                    resultado.setDriver(driver);
                    resultado.setGrid(false);
                    return resultado;
                }

            } catch (Exception erro) {
                URL url = getClass().getResource("/SOUNDS/have_this.wav");
                AudioClip clip = Applet.newAudioClip(url);
                clip.play();
                
                int continuar = JOptionPane.showConfirmDialog(null, "Demora no tempo de resposta do Sapiens\nDeseja continuar triando?", "Triagem encerrada", JOptionPane.YES_NO_OPTION);
                if (continuar == 0) {
                    buscar_processo(driver, wait);
                } else {
                    resultado.setDriver(driver);
                    resultado.setGrid(false);
                    return resultado;
                }
                
            }
        }
        resultado.setDriver(driver);
        resultado.setGrid(false);
        return resultado;
    }

}
