/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author rlaredo
 */
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
                System.out.println("Conexión exitosa.");
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
        String sql = """
        CREATE TABLE IF NOT EXISTS blacklist (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id_usuario_duenio TEXT NOT NULL,
            id_usuario_bloqueado TEXT NOT NULL,
            fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UNIQUE(id_usuario_duenio, id_usuario_bloqueado)
        );
    """;

        conn.createStatement().execute(sql);
    }
}
