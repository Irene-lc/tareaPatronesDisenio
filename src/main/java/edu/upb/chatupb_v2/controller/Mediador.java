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
    public static String ID_MIO;
    public static String NOMBRE;

    private ChatUI chatUI;
    private ContactDao contactDao = new ContactDao();
    private ChatsDao chatsDao = new ChatsDao();
    private static final Mediador mediador = new Mediador();
    private final HashMap<String, SocketClient> clientes = new HashMap<>();

    private Mediador() {
    }

    public static Mediador getInstance() { // de lo contrario no se puede acceder. Propiedad de singleton
        return mediador;
    }
    public void addClient(String idUsuario, String nombreClient, SocketClient client) {
        this.clientes.put(idUsuario, client);
        Contact nuevo = new Contact(idUsuario, nombreClient, client.getIp(), false, false, false);
        System.out.println(client.getIp());
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
            if (!chatUI.modoMensajeUnico) {
                Message message = new Mensaje(ID_MIO, idMensaje, mensaje);
                chatUI.chatsController.guardarEnBd(idMensaje, mensaje, ID_MIO, idCliente, obtenerHoraActual(), false);
                sendMessage(idCliente, message);
                if (chatUI != null) {
                    if (chatUI.jContactos.getSelectedValue().getId().equals(idCliente))
                        chatUI.agregarMensajeUI(mensaje, true, obtenerHoraActual(), false, idMensaje);
                }
            } else {
                MensajeUnico mensajeUnico = new MensajeUnico(ID_MIO, idMensaje, mensaje);
                chatUI.chatsController.guardarEnBd(idMensaje, mensaje, ID_MIO, idCliente, obtenerHoraActual(), true);
                this.chatsDao.updateUnico(idMensaje);
                sendMessage(idCliente, mensajeUnico);
                if (chatUI != null) {
                    if (chatUI.jContactos.getSelectedValue().getId().equals(idCliente))
                        chatUI.agregarMensajeUnicoUI(true, obtenerHoraActual(), false, idMensaje);
                }
            }
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
        Message message = new Invitacion(ID_MIO, NOMBRE);
        try {
            client.send(message);
            System.out.println("Enviando 001...");
        } catch (IOException e) {
            throw new OperationException("No se logró enviar la invitación");
        }
    }
    public void aceptarInvitacion(SocketClient socketClient, Invitacion invitacion) {
        addClient(invitacion.getIdUsuario(), invitacion.getNombre(), socketClient);
        Message aceptar = new Aceptar(ID_MIO, NOMBRE);
        sendMessage(invitacion.getIdUsuario(), aceptar);
        if (chatUI != null) {
            chatUI.actualizarEstadoContacto(invitacion.getIdUsuario(), true);
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
            chatUI.actualizarEstadoContacto(aceptar.getIdUsuario(), true);
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
        Message message = new Hello(ID_MIO);
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
                Message message = new AceptarHello(ID_MIO);
                client.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se logró establecer la conexión");
        }
    }
    public void actualizarLeidoBd(String idMensaje) {
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
        this.ID_MIO = UUID.randomUUID().toString();
        this.NOMBRE = nombre;
        Contact contact = new Contact(ID_MIO, nombre, "0",false,false, false);
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
                ID_MIO = contact.getId();
                NOMBRE = contact.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void enviarContacto(String idUsuarioPaMandar, String nombreAquienMandar) {
        try {
            Contact contactEnviar = this.contactDao.findById(idUsuarioPaMandar);
            System.out.println("enviadno contacto: " + contactEnviar.getId() + " " + contactEnviar.getName());
            EnviarContacto enviarContacto = new EnviarContacto(contactEnviar.getId(), contactEnviar.getName(), contactEnviar.getIp());
            Contact contactAquien = this.contactDao.findByName(nombreAquienMandar);
            System.out.println("el contacto se envia a: " + contactAquien.getName());
            sendMessage(contactAquien.getId(), enviarContacto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void actualizarFijado(String id, boolean fijado) {
        try {
            this.chatsDao.updateFijado(id, fijado);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean mensajeLeido(String idMensaje) {
        try {
            return this.chatsDao.isLeido(idMensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
            String idUsuario = hello.getIdUsuario();
            System.out.println("Llegó 004" + '\n');
            try {
                if (this.contactDao.existById(idUsuario)) {
                    Contact contact = this.contactDao.findById(idUsuario);
                    if (!contact.getIp().equals(socketClient.getIp())) {
                        this.contactDao.updateIp(idUsuario, socketClient.getIp());
                        contact.setIp(socketClient.getIp());
                        if (chatUI != null)
                            chatUI.actualizarContacto(contact);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            actualizarLeidoBd(confirmarRecibido.getIdMensaje());
        }
        if (message instanceof Zumbido) {
            Zumbido zumbido = (Zumbido) message;
            try {
                System.out.println( " 2" + chatUI.idUsuarioActual);
                System.out.println(zumbido.getIdUsuario());
                if (!chatUI.idUsuarioActual.equals(zumbido.getIdUsuario()) || chatUI.jContactos.getSelectedValue() == null)
                    chatUI.marcarZumbido(zumbido.getIdUsuario());
//                Contact contact = this.contactDao.findById(zumbido.getIdUsuario());
//                if (contact != null) {
//                    chatUI.showZumbidoPopup(contact.getName());
//                }
                chatUI.ejecutarEfectoZumbido();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (message instanceof EnviarContacto) {
            EnviarContacto enviarContacto = (EnviarContacto) message;
            try {
                Contact contact = new Contact(enviarContacto.getIdUsuario(), enviarContacto.getNombreCliente(), enviarContacto.getIp(), false, false, false);
                this.contactDao.save(contact);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (message instanceof FijarMensaje) {
            FijarMensaje fijarMensaje = (FijarMensaje) message;
            try {
                if (chatsDao.existById(fijarMensaje.getIdMensaje())) {
                    Chats mensaje = chatsDao.findById(fijarMensaje.getIdMensaje());
                    if (chatUI != null) {
                        if ((mensaje.getIdEmisor().equals(chatUI.idUsuarioActual) || mensaje.getIdReceptor().equals(chatUI.idUsuarioActual)) && chatUI.jContactos.getSelectedValue() != null) {
                            chatUI.fijarMensajeUI(fijarMensaje.getIdMensaje(), mensaje.getMensajeTxt());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (message instanceof ConfirmarRecibido) {
            try {
                if (this.chatsDao.isUnico(((ConfirmarRecibido) message).getIdMensaje())) {
                    this.chatsDao.updateMensajeUnico(((ConfirmarRecibido) message).getIdMensaje());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(() -> {
            chatUI.onMessage(message);
        });
    }
    public void updateUnico(String idMensaje) {
        try {
            this.chatsDao.updateUnico(idMensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

