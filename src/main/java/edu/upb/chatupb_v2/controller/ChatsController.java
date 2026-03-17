package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.entities.message.Chats;
import edu.upb.chatupb_v2.model.repository.ChatsDao;
import edu.upb.chatupb_v2.view.iChatView;

import java.util.List;

public class ChatsController {
    public ChatsDao chatsDao;
    public iChatView iChatView;

    public ChatsController(iChatView iChatView) {
        this.chatsDao = new ChatsDao();
        this.iChatView = iChatView;
    }

    public void guardarEnBd(String idMensaje, String mensajeTxt, String idEmisor, String idReceptor, String hora, boolean unico) {
        String unicoStr = unico ? "1" : "0";
        try {
            Chats chats = new Chats(idMensaje, mensajeTxt, hora, idEmisor, idReceptor, "0", "0", unicoStr);
            chatsDao.save(chats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void eliminarMensajeBD(String idMensaje) {
        try {
            if (chatsDao.existById(idMensaje)) {
                chatsDao.updateEliminarMensaje(idMensaje);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mensajeeliminadoPorCLiente(String idMensaje) {
        try {
            if (chatsDao.existById(idMensaje)) {
                chatsDao.updateMensajeEliminado(idMensaje);
            }
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
