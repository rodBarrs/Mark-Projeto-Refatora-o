/**
 * @author Felipe Marques, João Paulo, Gabriel Ramos, Rafael Henrique
 * 
 * Responsável por Verificar as condições do processo (Cabeçalho e Providência Juridica)
 */

package com.mycompany.newmark.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuscarCondicaoBanco {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    public boolean verificaCondicao(String processo, String tipo, String banco ) throws SQLException {
        Connection connection = connectionFactory.obterConexao();
        PreparedStatement stmt;
        ResultSet resultSet;

        stmt = connection.prepareStatement("SELECT * FROM condicao");


        resultSet = stmt.executeQuery();
        while (resultSet.next()){
            String Banco = resultSet.getString("banco");
            if((Banco.contains(banco)) || banco.equals("TODOS OS BANCOS")){
                String texto = resultSet.getString("texto");

                if (processo.contains(texto)) {
                    connection.close();
                    return true;
                }
            }

        }
        connection.close();
        return false;
    }
    
}