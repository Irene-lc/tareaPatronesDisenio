package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.repository.Chats;
import edu.upb.chatupb_v2.model.repository.ChatsDao;
import edu.upb.chatupb_v2.view.iChatView;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatsController {
    public ChatsDao chatsDao;
    public iChatView iChatView;

    public ChatsController(iChatView iChatView) {
        this.chatsDao = new ChatsDao();
        this.iChatView = iChatView;
    }

    public void guardarEnBd(String idMensaje, String mensajeTxt, String idEmisor, String idReceptor, String hora) {
        try {
            Chats chats = new Chats(idMensaje, mensajeTxt, hora, idEmisor, idReceptor, "0");
            chatsDao.save(chats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onloadMessages(String idEmisor, String idReceptor) {
        try {
            List<Chats> chats = chatsDao.findByContact(idEmisor, idReceptor);
            iChatView.onloadMessages(chats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
