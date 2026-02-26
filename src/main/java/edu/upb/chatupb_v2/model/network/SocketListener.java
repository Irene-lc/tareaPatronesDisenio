package edu.upb.chatupb_v2.model.network;


import edu.upb.chatupb_v2.model.entities.message.Message;

public interface SocketListener {
//    void onMessage(Message message);
    void onMessage(SocketClient socketClient, Message message);
}
