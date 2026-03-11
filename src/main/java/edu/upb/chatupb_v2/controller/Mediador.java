package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.controller.exception.OperationException;
import edu.upb.chatupb_v2.model.entities.message.*;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.model.network.SocketListener;
import edu.upb.chatupb_v2.model.repository.ChatsDao;
import edu.upb.chatupb_v2.model.repository.ContactDao;
import edu.upb.chatupb_v2.view.ChatUI;
import edu.upb.chatupb_v2.model.entities.message.Contact;
import lombok.Data;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

// que el mediador se subscriba a los eventos
@Data
public class Mediador implements SocketListener {

    private ChatUI chatUI;
    private ContactDao contactDao = new ContactDao();
    private ChatsDao chatsDao = new ChatsDao();
    private static final Mediador mediador = new Mediador();
    private final HashMap<String, SocketClient> clientes = new HashMap<>();
    private String idMio;
    private String nombre;

    private Mediador() {
    }

    public static Mediador getInstance() { // de lo contrario no se puede acceder. Propiedad de singleton
        return mediador;
    }
    public void addClient(String idUsuario, String nombreClient, SocketClient client) {
        this.clientes.put(idUsuario, client);
        Contact nuevo = new Contact(idUsuario, nombreClient, client.getIp(), false);
        try {
            if (this.contactDao.existById(idUsuario)) {
                Contact contact = this.contactDao.findById(idUsuario);
                if (!contact.getIp().equals(client.getIp()) || !contact.getName().equals(nombreClient)) {
                    this.contactDao.update(nuevo);
                    if (chatUI != null)
                        chatUI.actualizarContacto(nuevo);
                }
                return;
            }
            this.contactDao.save(nuevo);
            chatUI.actualizarEstadoContacto(idUsuario, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se pudo guardar el contacto en la base de datos");
        }
        if (chatUI != null)
            chatUI.agregarContacto(nuevo);

    }
    public void removeClient(String key) {
        this.clientes.remove(key);
    }
    public void sendMessage(String key, Message message) { //enviar mensajes tipo chat privado
        SocketClient cliente = this.clientes.get(key);
        try {
            message.execute(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (chatUI != null) {
                chatUI.mostrarMensajeSistema("No hay conexion con cliente");
            }
            return;
        }
        try {
            String idMensaje = UUID.randomUUID().toString();
            Message message = new Mensaje(idMio, idMensaje, mensaje);
            sendMessage(idCliente, message);
            chatUI.chatsController.guardarEnBd(idMensaje, mensaje, idMio, idCliente, obtenerHoraActual());
            if (chatUI != null)
                chatUI.agregarMensajeUI(mensaje, true, obtenerHoraActual(), false, idMensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public void enviarChau(Message message, String idUsuario) {
        try {
            SocketClient client = clientes.get(idUsuario);
            client.send(message);
            client.close();
            clientes.remove(idUsuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void establecerConexion(String ip) {
        SocketClient client;
        try {
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
        if (chatUI != null) {
            chatUI.actualizarValores(invitacion.getIdUsuario());
        }
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
        if (chatUI != null) {
            chatUI.actualizarValores(aceptar.getIdUsuario());
        }
    }
    public String obtenerHoraActual() {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formatter);
    }
    public void enviarHello(String id) {
        SocketClient client;
        Contact contact;
        try {
            contact = this.contactDao.findById(id);
            client = new SocketClient(contact.getIp());
            client.addListener(this);
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se logró establecer la conexión");
        }
        Message message = new Hello(idMio);
        try {
            client.send(message);
            System.out.println("Enviando 004...");
        } catch (IOException e) {
            throw new OperationException("No se logró enviar la invitación");
        }
    }
    public void enviarRespuestaHello(SocketClient client, String id) {
        Contact contact;
        try {
            contact = this.contactDao.findById(id);
            if (contact == null) {
                System.out.println("Enviando 006...");
                Message message = new RechazarHello();
                client.send(message);
            } else {
                clientes.put(id, client);
                chatUI.actualizarValores(id);
//                chatUI.showHelloPopup(contact.getName());
                Message message = new AceptarHello(idMio);
                client.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se logró establecer la conexión");
        }
    }
    public void actualizarLeido(String idMensaje) {
        try {
            this.chatsDao.updateLeido(idMensaje);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se pudo actualizar a 'Mensaje Leido'");
        }
    }
    public boolean dbVacia() {
        try {
            if (this.contactDao.findAll().isEmpty())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void primerRegistro(String nombre) {
        String idMio = "aed70cc4-7f4f-4e6d-974b-2dd32160a490";
        this.nombre = nombre;
        Contact contact = new Contact(idMio, nombre, "localhost", false);
        try {
            this.contactDao.save(contact);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cargarMisDatos() {
        try {
            Contact contact = this.contactDao.findUsuarioPrincipal();
            if (contact != null) {
                idMio = contact.getId();
                nombre = contact.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onMessage(SocketClient socketClient, Message message) {
        if (message instanceof Invitacion) {
            Invitacion invitacion = (Invitacion) message;
            boolean accepted = chatUI.showInvitationPopup(invitacion.getNombre());
            if (accepted) {
                System.out.println("Enviando 002..." + '\n');
                aceptarInvitacion(socketClient, invitacion);
                chatUI.actualizarEstadoContacto(invitacion.getIdUsuario(), true);
            } else  {
                System.out.println("Enviando 003..." + '\n');
                rechazarInvitacion(socketClient);
            }
        }
        if (message instanceof Aceptar) {
            Aceptar aceptar = (Aceptar) message;
            invitacionAceptada(socketClient, message);
            chatUI.actualizarEstadoContacto(aceptar.getIdUsuario(), true);

        }
        if (message instanceof Hello) {
            Hello hello = (Hello) message;
            System.out.println("Llegó 004" + '\n');
            enviarRespuestaHello(socketClient, hello.getIdUsuario());
            chatUI.actualizarEstadoContacto(hello.getIdUsuario(), true);
        }
        if (message instanceof AceptarHello) {
            AceptarHello aceptarHello = (AceptarHello) message;
            System.out.println("Hello aceptado por: " + aceptarHello.getIdUsuario() + '\n');
            clientes.put(aceptarHello.getIdUsuario(), socketClient);
            chatUI.actualizarEstadoContacto(aceptarHello.getIdUsuario(), true);
        }
        if (message instanceof ConfirmarRecibido) {
            ConfirmarRecibido confirmarRecibido = (ConfirmarRecibido) message;
            actualizarLeido(confirmarRecibido.getIdMensaje());
        }
        if (message instanceof Zumbido) {
            Zumbido zumbido = (Zumbido) message;
            try {
                Contact contact = this.contactDao.findById(zumbido.getIdUsuario());
                if (contact != null) {
                    chatUI.showZumbidoPopup(contact.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            chatUI.onMessage(message);
        });
    }

    @Override
    public void onDisconnect(SocketClient socketClient) {
        try {
            Contact contact = this.contactDao.findByIp(socketClient.getIp());
            if (contact != null) {
                chatUI.actualizarEstadoContacto(contact.getId(), false);
                removeClient(contact.getId());
                clientes.remove(contact.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

