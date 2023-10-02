/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 *
 */
package com.mycompany.newmark.banco;

import com.mycompany.newmark.auxiliares.Tratamento;
import com.mycompany.newmark.models.ChavesConfiguracao;
import com.mycompany.newmark.models.ChavesResultado;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuscarEtiquetaBanco {
ConnectionFactory connectionFactory = new ConnectionFactory();
	public ChavesResultado triarBanco(String processo, String banco, ChavesResultado resultado, ChavesConfiguracao configuracao) {
		processo = processo.replace("\n", "").replace(" ", "");
		Tratamento tratamento = new Tratamento();
		processo = tratamento.tratamento(processo);

			try {
				Connection connection = connectionFactory.obterConexao();

				String query = configuracao.isPeticaoInicial()
						? "SELECT * FROM identificador_materia ORDER BY prioridade DESC"
						: "SELECT * FROM etiquetas WHERE tipo = ? ORDER BY prioridade DESC";

				try (PreparedStatement stmt = connection.prepareStatement(query)) {
					if (!configuracao.isPeticaoInicial()) {
						stmt.setString(1, resultado.getLocal());
					}

					ResultSet resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						String palavraChave = tratamento.tratamento(resultSet.getString("palavrachave"));
						String complemento = tratamento.tratamento(resultSet.getString("complemento"));
						String bancoFromDB = resultSet.getString("banco");

						if (processo.contains(palavraChave) && processo.contains(complemento) &&
								(bancoFromDB.contains(banco) || banco.contains("TODOS OS BANCOS"))) {
							resultado.setPalavraChave(resultSet.getString("id"));
							if (configuracao.isPeticaoInicial()) {
								resultado.setSubnucleo(resultSet.getString("subnucleo"));
							}
							resultado.setEtiqueta(resultSet.getString("etiqueta"));
							connection.close();
							return resultado;
						}
					}
				}

				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(BuscarEtiquetaBanco.class.getName()).log(Level.SEVERE, null, ex);
			}

			resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA (" +
					banco + ", " + configuracao.getTipoTriagem() + ")");
			return resultado;
		}
}


