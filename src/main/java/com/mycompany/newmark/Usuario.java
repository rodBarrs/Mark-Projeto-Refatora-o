/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
//Acescentar função de exibir e salvar senha
package com.mycompany.newmark;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

class Usuario {
    private String Login = "";
    private String Senha = "";

    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }
    
    public void logar(WebDriver driver, WebDriverWait wait, Usuario usuario) throws InterruptedException {
    	driver.findElement(By.id("cpffield-1017-inputEl")).sendKeys(usuario.getLogin());
		driver.findElement(By.id("textfield-1018-inputEl")).sendKeys(usuario.getSenha());
		try {
            driver.findElement(By.id("button-1019-btnInnerEl")).click();
        }
		catch(Exception erro){
            System.out.println("Erro no clique");
        }

    }
}