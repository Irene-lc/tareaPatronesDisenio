/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.upb.chatupb_v2;

import edu.upb.chatupb_v2.bl.server.ChatServer;
import edu.upb.chatupb_v2.bl.server.Mediador;

public class ChatUPB_V2 {

    public static void main(String[] args) {
        /* Create and display the form */
        final ChatUI chatUI = new ChatUI();
        ChatView chatView = new ChatView(chatUI);
        Mediador.getInstance().setChatUI(chatUI);
        Mediador.getInstance().setChatView(chatView);
        Mediador.getInstance().cargarDesdeBD();
        java.awt.EventQueue.invokeLater(new Runnable() {
            // lo que se hace dentro del hilo es solo del contexto del hilo
            public void run() {
                // dentro del contexto del hilo
                //no puede tocar nada del contexto externo, no puede comunicarse fuera del hilo
                chatView.setVisible(true);
            }
        });
        try {
            ChatServer chatServer = new ChatServer();
            chatServer.start();
//            chatServer.addListener(chatUI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
