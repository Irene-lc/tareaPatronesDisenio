/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
 
    private static final ConnectionDB connection = new ConnectionDB();
    
    private ConnectionDB(){
       
    }
    
    public static ConnectionDB getInstance(){
        return connection;
    }

    public Connection getConection(){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:chat_upb.sqlite");
            if (conn != null) {
//                System.out.println("Conexión exitosa.");
                crearTablaSiNoExiste(conn);
            } else {
                System.out.println("Conexión fallida");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch(ClassNotFoundException e){

        }
        return conn;
    }

    private void crearTablaSiNoExiste(Connection conn) throws SQLException {

        String sqlContact = """
        CREATE TABLE IF NOT EXISTS contact (
            id TEXT PRIMARY KEY,
            name TEXT NOT NULL,
            ip TEXT NOT NULL UNIQUE
        );
    """;

        conn.createStatement().execute(sqlContact);

        String sqlChats = """
        CREATE TABLE IF NOT EXISTS chats (
            idMensaje TEXT PRIMARY KEY,
            mensajeTxt TEXT NOT NULL,
            hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            idEmisor TEXT NOT NULL,
            idReceptor TEXT NOT NULL,
            leido TEXT NOT NULL
        );
    """;

        conn.createStatement().execute(sqlChats);
    }
}
