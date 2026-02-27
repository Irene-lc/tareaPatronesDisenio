package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.model.network.SocketListener;
import edu.upb.chatupb_v2.view.ChatUI;
import edu.upb.chatupb_v2.view.ChatView;
import edu.upb.chatupb_v2.model.entities.message.Invitacion;
import edu.upb.chatupb_v2.model.entities.message.Mensaje;
import edu.upb.chatupb_v2.model.entities.message.Message;
import edu.upb.chatupb_v2.model.repository.Contact;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

// que el mediador se subscriba a los eventos
@Data
public class Mediador implements SocketListener {

    private ChatUI chatUI;
    private ChatView chatView;
    private static final Mediador mediador = new Mediador();
    private final HashMap<String, SocketClient> clientes = new HashMap<>();
//    public List<SocketClient> listaNegra;
    private final String idMio = "kf3fc20a-766c-4rd4-813d-b1967a01fa9a";
    private final String nombre = "Irene";

    private Mediador() {
    }

    public static Mediador getInstance() { // de lo contrario no se puede acceder. Propiedad de singleton
        return mediador;
    }
    public void addClient(String idUsuario, String nombreClient, SocketClient client) {
        this.clientes.put(idUsuario, client);

        System.out.println("Creando contacto:");
        System.out.println("ID contacto: " + idUsuario);
        System.out.println("Nombre contacto: " + nombreClient);
        System.out.println("IP contacto: " + client.getIp());
        System.out.println("------");

        Contact nuevo = chatView.contactController.agregarContactoBd(idUsuario, nombreClient, client.getIp());
        if (chatView != null)
            chatView.agregarContacto(nuevo);
    }
    public void removeClient(String key) {
        this.clientes.remove(key);
    }
    public void establecerConexion(String ip, String idMio, String nombre) {
        try {
            SocketClient client = new SocketClient(ip);
            client.start();
            client.addListener(this); // El mediador se subscribe a los eventos de SocketClient para escuchar
            Message message = new Invitacion(idMio, nombre);
            client.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void sendMessage(String key, Message message) { //enviar mensajes tipo chat privado
        SocketClient cliente = this.clientes.get(key);
        if (cliente == null)
            return;
        try {
            cliente.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Enviando comando: " + message.generarTrama());
    }
    public void enviarMensajePorChat(String idCliente, String mensaje) {
        if (!clientes.containsKey(idCliente)) {
            if (chatView != null) {
                chatView.mostrarMensajeSistema("No hay conexion con cliente");
                System.out.println("Cliente no en lsitaContactos");
            }
            return;
        }
        try {
            String idMensaje = UUID.randomUUID().toString();
            Message message = new Mensaje(idMio, idMensaje, mensaje);
            sendMessage(idCliente, message);
            chatView.chatsController.guardarEnBd(idMensaje, mensaje, idMio, idCliente, chatView.obtenerHoraActual());
            if (chatView != null)
                chatView.agregarMensaje(mensaje, true, chatView.obtenerHoraActual());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    public void notificar(SocketClient client, Message message) {
//        SwingUtilities.invokeLater(() -> {
//            if (chatUI != null) {
//                chatUI.onMessage(client, message);
//            }
//        });
//        SwingUtilities.invokeLater(() -> {
//            if (chatView != null)
//                chatView.onMessage(message);
//        });
//    }
    public void enviarMensajeATodos(Message message) {
        for (SocketClient cliente : clientes.values()) {
            try {
                cliente.send(message);
                cliente.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clientes.clear();
    }
    @Override
    public synchronized void onMessage(SocketClient socketClient, Message message) {
        chatUI.onMessage(socketClient, message);
        chatView.onMessage(message);
        //llamar Dao?
    }
}

