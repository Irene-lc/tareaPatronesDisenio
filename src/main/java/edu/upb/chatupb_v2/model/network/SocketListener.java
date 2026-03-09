package edu.upb.chatupb_v2.model.network;


import edu.upb.chatupb_v2.controller.FormaConectar;
import edu.upb.chatupb_v2.model.entities.message.Message;

public interface SocketListener {
    void onMessage(SocketClient socketClient, Message message);
    void onDisconnect(SocketClient socketClient);
    void onNotSecure(SocketClient socketClient);
    void onSecure(SocketClient socketClient, FormaConectar formaConectar);
}
