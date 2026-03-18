///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package edu.upb.chatupb_v2.model.repository;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class ConnectionDB {
//
//    private static final ConnectionDB connection = new ConnectionDB();
//    private static final String DB_PATH = buildDbPath();
//
//    private static String buildDbPath() {
//        String home = System.getProperty("user.home");
//        File appDir = new File(home, "Library/Application Support/ChatUPB");
//        if (!appDir.exists()) {
//            appDir.mkdirs(); // Crea la carpeta si no existe
//        }
//        return new File(appDir, "chat_upb.sqlite").getAbsolutePath();
//    }
//
//    private ConnectionDB(){
//
//    }
//
//    public static ConnectionDB getInstance(){
//        return connection;
//    }
//
//    public Connection getConection(){
//        Connection conn = null;
//        try {
//            Class.forName("org.sqlite.JDBC");
//            conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
//            if (conn != null) {
////                System.out.println("Conexión exitosa.");
//                crearTablaSiNoExiste(conn);
//            } else {
//                System.out.println("Conexión fallida");
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }catch(ClassNotFoundException e){
//
//        }
//        return conn;
//    }
//
//    private void crearTablaSiNoExiste(Connection conn) throws SQLException {
//
//        conn.createStatement().execute("PRAGMA foreign_keys = ON");
//
//        String sqlContact = """
//        CREATE TABLE IF NOT EXISTS contact (
//            id TEXT PRIMARY KEY,
//            name TEXT NOT NULL,
//            ip TEXT NOT NULL UNIQUE
//        );
//    """;
//
//        conn.createStatement().execute(sqlContact);
//
//        String sqlChats = """
//        CREATE TABLE IF NOT EXISTS chats (
//            idMensaje TEXT PRIMARY KEY,
//            mensajeTxt TEXT,
//            hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//            idEmisor TEXT NOT NULL,
//            idReceptor TEXT NOT NULL,
//            leido TEXT NOT NULL,
//            fijado TEXT,
//            unico TEXT,
//
//            FOREIGN KEY (idEmisor) REFERENCES contact(id)
//                ON DELETE CASCADE
//                ON UPDATE CASCADE,
//
//            FOREIGN KEY (idReceptor) REFERENCES contact(id)
//                ON DELETE CASCADE
//                ON UPDATE CASCADE
//        );
//    """;
//
//        conn.createStatement().execute(sqlChats);
//    }
//
//}

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

//    private void crearTablaSiNoExiste(Connection conn) throws SQLException {
//
//        String sqlContact = """
//        CREATE TABLE IF NOT EXISTS contact (
//            id TEXT PRIMARY KEY,
//            name TEXT NOT NULL,
//            ip TEXT NOT NULL UNIQUE
//        );
//    """;
//
//        conn.createStatement().execute(sqlContact);
//
//        String sqlChats = """
//        CREATE TABLE IF NOT EXISTS chats (
//            idMensaje TEXT PRIMARY KEY,
//            mensajeTxt TEXT NOT NULL,
//            hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//            idEmisor TEXT NOT NULL,
//            idReceptor TEXT NOT NULL,
//            leido TEXT NOT NULL
//        );
//    """;
//
//        conn.createStatement().execute(sqlChats);
//    }

    private void crearTablaSiNoExiste(Connection conn) throws SQLException {

        conn.createStatement().execute("PRAGMA foreign_keys = ON");

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
            mensajeTxt TEXT,
            hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            idEmisor TEXT NOT NULL,
            idReceptor TEXT NOT NULL,
            leido TEXT NOT NULL,
            fijado TEXT,
            unico TEXT,

            FOREIGN KEY (idEmisor) REFERENCES contact(id)
                ON DELETE CASCADE
                ON UPDATE CASCADE,

            FOREIGN KEY (idReceptor) REFERENCES contact(id)
                ON DELETE CASCADE
                ON UPDATE CASCADE
        );
    """;

        conn.createStatement().execute(sqlChats);
    }

}
