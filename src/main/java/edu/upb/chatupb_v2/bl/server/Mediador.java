package edu.upb.chatupb_v2.bl.server;

import edu.upb.chatupb_v2.bl.message.Message;

import java.io.IOException;
import java.util.HashMap;

public class Mediador {
    private static final Mediador mediador = new Mediador();
    public final HashMap<String, SocketClient> listaContactos = new HashMap<>();

    private Mediador() {

    }

    public static Mediador getInstance() { // de lo contrario no se puede acceder. Propiedad de singleton
        return mediador;
    }

    //metodo para agregar cliente
    public void addClient(String key, SocketClient value) {
        this.listaContactos.put(key, value);
    }

    //metodo para remover cliente
    public void removeClient(String key) {
        this.listaContactos.remove(key);
    }

    public void sendMessage(String key, Message message) { //enviar mensajes tipo chat privado
        SocketClient cliente = this.listaContactos.get(key);
        if (cliente == null) {
            return;
        }
        try {
            cliente.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Enviando comando: " + message.generarTrama());
    }

    public void enviarMensajeATodos(Message message) {
        for (SocketClient cliente : listaContactos.values()) {
            try {
                cliente.send(message);
                cliente.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listaContactos.clear();
    }
}
