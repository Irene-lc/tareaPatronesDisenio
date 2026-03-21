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
//            appDir.mkdirs();
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
//            conn = DriverManager.getConnection("jdbc:sqlite:chat_upb.sqlite");
////            conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
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
//            ip TEXT NOT NULL
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
//}
package edu.upb.chatupb_v2.model.repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static final ConnectionDB connection = new ConnectionDB();

    // Cambio clave: NO inicializar aquí, usar lazy initialization
    private static String dbPath = null;

    private static synchronized String getDbPath() {
        if (dbPath == null) {
            dbPath = buildDbPath();
        }
        return dbPath;
    }

    private static String buildDbPath() {
        String home = System.getProperty("user.home");

        // Debug: Agregar log para diagnosticar
        System.out.println("[ChatUPB] user.home = " + home);

        if (home == null || home.isEmpty()) {
            // Fallback para macOS si user.home falla
            home = System.getenv("HOME");
            System.out.println("[ChatUPB] Fallback HOME = " + home);
        }

        if (home == null || home.isEmpty()) {
            // Último recurso: usar directorio temporal
            home = System.getProperty("java.io.tmpdir");
            System.out.println("[ChatUPB] Fallback tmpdir = " + home);
        }

        File appDir = new File(home, "Library/Application Support/ChatUPB");

        System.out.println("[ChatUPB] Intentando crear directorio: " + appDir.getAbsolutePath());

        if (!appDir.exists()) {
            boolean created = appDir.mkdirs();
            System.out.println("[ChatUPB] Directorio creado: " + created);
        }

        String finalPath = new File(appDir, "chat_upb.sqlite").getAbsolutePath();
        System.out.println("[ChatUPB] Ruta final DB: " + finalPath);

        return finalPath;
    }

    private ConnectionDB() {
    }

    public static ConnectionDB getInstance() {
        return connection;
    }

    public Connection getConection() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // Usar el getter en lugar del campo directamente
            conn = DriverManager.getConnection("jdbc:sqlite:" + getDbPath());
            if (conn != null) {
                crearTablaSiNoExiste(conn);
            } else {
                System.out.println("Conexión fallida");
            }
        } catch (SQLException e) {
            System.out.println("[ChatUPB] Error SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("[ChatUPB] Driver no encontrado: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    private void crearTablaSiNoExiste(Connection conn) throws SQLException {
        conn.createStatement().execute("PRAGMA foreign_keys = ON");

        String sqlContact = """
            CREATE TABLE IF NOT EXISTS contact (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                ip TEXT NOT NULL
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
                    ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (idReceptor) REFERENCES contact(id)
                    ON DELETE CASCADE ON UPDATE CASCADE
            );
        """;
        conn.createStatement().execute(sqlChats);
    }
}
