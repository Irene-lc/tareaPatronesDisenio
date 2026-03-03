/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.upb.chatupb_v2;

import edu.upb.chatupb_v2.controller.ChatsController;
import edu.upb.chatupb_v2.controller.ContactController;
import edu.upb.chatupb_v2.model.network.ChatServer;
import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.view.ChatUI;

public class ChatUPB_V2 {

    public static void main(String[] args) {
        /* Create and display the form */
        final ChatUI chatUI = new ChatUI();

        ContactController contactController = new ContactController(chatUI);
        ChatsController chatsController = new ChatsController(chatUI);

        chatUI.setContactController(contactController);
        chatUI.setChatsController(chatsController);

        Mediador.getInstance().setChatUI(chatUI);

        contactController.onload();
        java.awt.EventQueue.invokeLater(new Runnable() {
            // lo que se hace dentro del hilo es solo del contexto del hilo
            public void run() {
                // dentro del contexto del hilo
                //no puede tocar nada del contexto externo, no puede comunicarse fuera del hilo
                chatUI.setVisible(true);
            }
        });
        try {
            ChatServer chatServer = new ChatServer();
            chatServer.start();
//            chatServer.addListener(Mediador.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
