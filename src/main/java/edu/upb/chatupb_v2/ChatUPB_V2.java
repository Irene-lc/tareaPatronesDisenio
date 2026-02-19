/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.upb.chatupb_v2;

import edu.upb.chatupb_v2.bl.server.ChatServer;
import edu.upb.chatupb_v2.bl.server.Mediador;

/**
 * @author rlaredo
 */
public class ChatUPB_V2 {

    public static void main(String[] args) {
        /* Create and display the form */
        final ChatUI chatUI = new ChatUI();
        System.out.println(Mediador.getInstance().listaContactos);
        java.awt.EventQueue.invokeLater(new Runnable() {
            // lo que se hace dentro del hilo es solo del contexto del hilo
            public void run() {
                // dentro del contexto del hilo
                //no puede tocar nada der contexto externo, no puede comunicarse fuera del hilo
                chatUI.setVisible(true);
            }
        });
        try {
            ChatServer chatServer = new ChatServer();
            chatServer.start();
            chatServer.addListener(chatUI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
