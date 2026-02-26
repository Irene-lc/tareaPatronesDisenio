package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.view.ChatUI;
import edu.upb.chatupb_v2.view.ChatView;
import edu.upb.chatupb_v2.model.entities.message.Invitacion;
import edu.upb.chatupb_v2.model.entities.message.Mensaje;
import edu.upb.chatupb_v2.model.entities.message.Message;
import edu.upb.chatupb_v2.model.repository.Contact;
import edu.upb.chatupb_v2.model.repository.ContactDao;
import lombok.Data;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// que el mediador se subscriba a los eventos
@Data
public class Mediador {

    private ChatUI chatUI;
    private ChatView chatView;
    private static final Mediador mediador = new Mediador();
    public final HashMap<String, SocketClient> listaContactos = new HashMap<>();
    public List<SocketClient> listaNegra;
    private final String idMio = "kf3fc20a-766c-4rd4-813d-b1967a01fa9a";
    private final String nombre = "Irene";

    private Mediador() {
    }

    public static Mediador getInstance() { // de lo contrario no se puede acceder. Propiedad de singleton
        return mediador;
    }

    public void addClient(String idUsuario, String nombreClient, SocketClient client) {
        this.listaContactos.put(idUsuario, client);

        Contact nuevo = nuevoContacto(idUsuario, nombreClient, client.getIp());
        if (chatView != null)
            chatView.agregarContacto(nuevo);
    }

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
    public void notificar(SocketClient client, Message message) {
        SwingUtilities.invokeLater(() -> {
            if (chatUI != null) {
                chatUI.onMessage(client, message);
            }
        });
        SwingUtilities.invokeLater(() -> {
            if (chatView != null)
                chatView.onMessage(message);
        });
    }
    public void establecerConexion(String ip, String idMio, String nombre) {
        try {
            SocketClient client = new SocketClient(ip);
            client.start();
            Message message = new Invitacion(idMio, nombre);
            client.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Contact nuevoContacto(String idCliente, String nombreCliente, String ipCliente) {

        System.out.println("Creando contacto:");
        System.out.println("ID: " + idCliente);
        System.out.println("Nombre: " + nombreCliente);
        System.out.println("IP: " + ipCliente);

        ContactDao contactDao = new ContactDao();

        try {
            Contact verificar = contactDao.findByIp(ipCliente);
            if (verificar != null) {
                if (verificar.getId() == null || !verificar.getId().equals(idCliente)) {
                    System.out.println("Contacto diferente con ip registrada");
                    verificar.setId(idCliente);
                    verificar.setIp(ipCliente);
                    verificar.setName(nombreCliente);
                    contactDao.update(verificar);
                }
                return verificar;
            }
            Contact nuevo = new Contact(idCliente, nombreCliente, ipCliente, false);
            contactDao.save(nuevo);
            return nuevo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void enviarMensajePorChat(String idCliente, String mensaje) {
        if (!listaContactos.containsKey(idCliente)) {
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
//    @Override
//    void onMessage(SocketClient socketClient, Message message) {
//        SwingUtilities.invokeLater(() -> {
//            if (chatUI != null) {
//                chatUI.onMessage(socketClient, message);
//            }
//        });
//        SwingUtilities.invokeLater(() -> {
//            if (chatView != null)
//                chatView.onMessage(socketClient, message);
//        });
//    }
}

