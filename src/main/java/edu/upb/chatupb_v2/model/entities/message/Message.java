package edu.upb.chatupb_v2.model.entities.message;

import edu.upb.chatupb_v2.model.network.SocketClient;

import java.io.IOException;

public abstract class Message {
    private String codigo;
//    private String ip;

    public Message(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public abstract String generarTrama();
    public abstract void execute(SocketClient client) throws IOException;
//    public String getIp() {return ip;}
//    public void setIp(String ip) {this.ip = ip;}

}
