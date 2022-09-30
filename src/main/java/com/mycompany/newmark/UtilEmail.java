package com.mycompany.newmark;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert;

public class UtilEmail {
	String mensagem;

	public UtilEmail(String mensagem) {
		this.mensagem = mensagem;
	}

	public void enviarEmail() {
		final String username = "markdev413@gmail.com";
		final String password = "markaguemail";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("markdev413@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("nutecpfpa@gmail.com"));
			message.setSubject("Relatório de Erro do Mark");
			message.setText(mensagem);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
