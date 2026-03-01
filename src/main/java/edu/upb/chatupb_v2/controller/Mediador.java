package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.controller.exception.OperationException;
import edu.upb.chatupb_v2.model.entities.message.*;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.model.network.SocketListener;
import edu.upb.chatupb_v2.model.repository.ContactDao;
import edu.upb.chatupb_v2.view.ChatUI;
import edu.upb.chatupb_v2.view.ChatView;
import edu.upb.chatupb_v2.model.repository.Contact;
import lombok.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

// que el mediador se subscriba a los eventos
@Data
public class Mediador implements SocketListener {

    private ChatUI chatUI;
    private ChatView chatView;
    private ContactDao contactDao = new ContactDao();
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

        Contact nuevo = new Contact(idUsuario, nombreClient, client.getIp(), false);
        try {
            this.contactDao.save(nuevo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se pudo guardar el contacto en la base de datos");
        }
        if (chatView != null)
            chatView.agregarContacto(nuevo);

    }
    public void removeClient(String key) {
        this.clientes.remove(key);
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
                chatView.agregarMensajeUI(mensaje, true, chatView.obtenerHoraActual());
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

    public void establecerConexion(String ip) {
        SocketClient client;
        try {
            System.out.println("entroooo");
            client = new SocketClient(ip);
            client.addListener(this); // El mediador se subscribe a los eventos de SocketClient para escuchar
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se logró establecer la conexión");
        }
        Message message = new Invitacion(idMio, nombre);
        try {
            client.send(message);
            System.out.println("Enviando 001...");
        } catch (IOException e) {
            throw new OperationException("No se logró enviar la invitación");
        }
    }
    public void aceptarInvitacion(SocketClient socketClient, Invitacion invitacion) {
        addClient(invitacion.getIdUsuario(), invitacion.getNombre(), socketClient);

        Message aceptar = new Aceptar(idMio, nombre);
        sendMessage(invitacion.getIdUsuario(), aceptar);
        if (chatView != null) {
            chatView.actualizarValores(invitacion.getIdUsuario());
            chatView.setVisible(true);
        }
        chatUI.setVisible(false);
    }
    public void rechazarInvitacion(SocketClient socketClient) {
        try {
            SocketClient client = new SocketClient(socketClient.getIp());
            Message rechazar = new Rechazar();
            client.send(rechazar);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se pudo rechazar la invitación");
        }
    }
    public void invitacionAceptada(SocketClient socketClient, Message message) {
        Aceptar aceptar = (Aceptar) message;
        addClient(aceptar.getIdUsuario(), aceptar.getNombre(), socketClient);
        if (chatView != null) {
            chatView.actualizarValores(aceptar.getIdUsuario());
            chatView.setVisible(true);
        }
    }
    @Override
    public synchronized void onMessage(SocketClient socketClient, Message message) {
        if (message instanceof Invitacion) {
            Invitacion invitacion = (Invitacion) message;
            boolean accepted = chatUI.showInvitationPopup(invitacion.getNombre());
            if (accepted) {
                System.out.println("Enviando 002...");
                aceptarInvitacion(socketClient, invitacion);
            } else  {
                System.out.println("Enviando 003...");
                rechazarInvitacion(socketClient);
            }
        }
        if (message instanceof Aceptar) {
            invitacionAceptada(socketClient, message);
            chatView.setVisible(true);
        }
        chatUI.onMessage(message);
        chatView.onMessage(message);
        //llamar Dao?
    }
}

