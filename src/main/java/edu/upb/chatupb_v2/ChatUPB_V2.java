/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.upb.chatupb_v2;

import edu.upb.chatupb_v2.controller.ChatsController;
import edu.upb.chatupb_v2.controller.ContactController;
import edu.upb.chatupb_v2.model.network.ChatServer;
import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.view.ChatUI;
import edu.upb.chatupb_v2.view.ChatView;

import java.util.List;

public class ChatUPB_V2 {

    public static void main(String[] args) {
        /* Create and display the form */
        final ChatUI chatUI = new ChatUI();
        ChatView chatView = new ChatView(chatUI);

        ContactController contactController = new ContactController(chatView);
        ChatsController chatsController = new ChatsController(chatView);

        chatView.setContactController(contactController);
        chatView.setChatsController(chatsController);

        Mediador.getInstance().setChatUI(chatUI);
        Mediador.getInstance().setChatView(chatView);

        contactController.onload();
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
//            chatServer.addListener(Mediador.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
