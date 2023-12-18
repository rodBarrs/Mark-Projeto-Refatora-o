/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 *
 */
package com.mycompany.newmark.triagem.strategy.context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.mycompany.newmark.auxiliares.email.UtilEmail;
import com.mycompany.newmark.banco.Banco;
import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;
import com.mycompany.newmark.models.ChavesTeste;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemEtiqueta;
import com.mycompany.newmark.triagem.triagemAuxiliares.TriagemGridMenu;
import com.mycompany.newmark.triagem.strategy.triagemAbstract.TriagemStrategy;
import com.mycompany.newmark.triagem.strategy.triagemConcrete.TriagemDocumento;
import com.mycompany.newmark.triagem.strategy.triagemConcrete.TriagemMovimentacao;
import com.mycompany.newmark.triagem.strategy.triagemConcrete.TriagemPericial;
import com.mycompany.newmark.triagem.strategy.triagemConcrete.TriagemPeticaoInicial;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.mycompany.newmark.auxiliares.system.Sistema;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 *
 * @author admin-felipe
 */
public class TriagemStart {


	public TriagemStart(WebDriver driver, String banco) throws ParseException {
		super();
	}

	//Inicia a Triagem
	public boolean iniciarTriagem(WebDriver driver, WebDriverWait wait, String bancos, ChavesTeste teste) throws InterruptedException {
		ChavesResultado resultado = new ChavesResultado();
		resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA");
		resultado.setDriver(driver);
		ChavesConfiguracao configuracao = new ChavesConfiguracao();
		Banco banco = new Banco();
		configuracao = banco.pegarConfiguracao(configuracao, teste);
		TriagemGridMenu triagem_grid = new TriagemGridMenu(resultado.getDriver());
		TriagemEtiqueta etiqueta = new TriagemEtiqueta();
		boolean grid;



		try {

			do {
				resultado = triagem_grid.buscar_processo(resultado.getDriver(), wait);
				grid = resultado.isGrid();
				if (grid == false) {
					return true;
				} else {
					TriagemStrategy triagemStrategy = definirEstrategia(configuracao);
					resultado = triagemStrategy.realizarTriagem(driver,wait,configuracao,bancos);

					//Fecha a janela do processo e volta para a janela do grid
					List<String> janela = new ArrayList(resultado.getDriver().getWindowHandles());
					resultado.getDriver().switchTo().window(janela.get(1)).close();
					resultado.getDriver().switchTo().window(janela.get(0));

					etiqueta.etiquetar(resultado.getDriver(), wait, resultado,configuracao);
					//Contadores
					if (resultado.getLocal().contains("MOV")) {
						banco.contarMov();
					} else if (resultado.getLocal().contains("DOC")) {
						banco.contarDoc();
					} else {
						banco.contarNao();
					}
				}
			} while (grid != false);
		} catch (Exception e) {
			final Exception ex = e;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert erro = definirMEnsagemError();
					String exceptionText = exibirMensagemError(ex, erro);
					enviarEmail(exceptionText);
					erro.show();
				}
			});
			return true;
		}
		return true;
	}

	private static void filtrarTeste(WebDriver driver, WebDriverWait wait, TriagemEtiqueta etiqueta, ChavesTeste teste) throws InterruptedException {
		Thread.sleep(1000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[4]/div/table/tbody/tr[1]/td[3]/div/a[1]")));
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[4]/div/table/tbody/tr[1]/td[3]/div/a[1]")));


		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS).pageLoadTimeout(30, TimeUnit.SECONDS);
		WebElement setaAparecer = driver.findElement(
				By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div[1]/div[3]/div/div/div[33]/div/span"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div[1]/div[3]/div/div/div[33]/div/span")));
		setaAparecer.click();
		WebElement seta = driver.findElement(
				By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div[1]/div[3]/div/div/div[33]/div/div"));
		seta.click();


		WebElement filtro = driver.findElement(By.xpath("/html/body/div[11]/div/div[2]/div/div[6]/a/div[1]"));
		filtro.click();
		WebElement filtroEs = driver.findElement(By.xpath("/html/body/div[13]/div/div[2]"));
		filtroEs.click();
		WebElement filtroSpace = driver
				.findElement(By.xpath("/html/body/div[13]/div/div[2]/div/table/tbody/tr/td[2]/input"));
		filtroSpace.click();
		System.out.println("etiqueta no filtro: " + etiqueta);
		filtroSpace.sendKeys(teste.getFiltro());
		Thread.sleep(1000);
		long time = 100;

		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[2]/div/div/div[7]")));
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[2]/div/div/div[7]")));
		Thread.sleep(1000);
	}

	private static void enviarEmail(String exceptionText) {
		final UtilEmail email = new UtilEmail(exceptionText);
		Runnable task = new Runnable() {
			@Override
			public void run() {
				email.enviarEmail();
			}

		};
		Thread mail = new Thread(task);
		mail.setDaemon(true);
		mail.start();
	}

	private Alert definirMEnsagemError(){
		Alert erro = new Alert(Alert.AlertType.INFORMATION);
		erro.setTitle("Alerta");
		erro.setHeaderText("Ocorreu um erro duranto o processo de triagem!");
		erro.setContentText("Este relatório será enviado para os desenvolvedores");

		return erro;
	}

	private String exibirMensagemError(Exception ex,Alert erro){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = "Versão: " + Sistema.VERSAO + "\nUsuário: " + System.getProperty("user.name");
		exceptionText += "\n" + sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		// Set expandable Exception into the dialog pane.
		erro.getDialogPane().setExpandableContent(expContent);
		return exceptionText;
	}
	private static TriagemStrategy definirEstrategia(ChavesConfiguracao config) {
		TriagemStrategy triagemStrategy = null;
		//Setar todas as configurações especificas nesses ifs
		if (config.isLaudoPericial() == true) {
			 return triagemStrategy = new TriagemPericial();
		} else if (config.isPeticaoInicial() == true) {
			return  triagemStrategy = new TriagemPeticaoInicial();
		} else {
			switch (config.getTipoTriagem()) {
				case "COM":
					return triagemStrategy = new TriagemMovimentacao();
				case "MOV":
					return triagemStrategy = new TriagemMovimentacao();
				case "DOC":
					return triagemStrategy = new TriagemDocumento();
			}
		}
		return triagemStrategy;
	}
}