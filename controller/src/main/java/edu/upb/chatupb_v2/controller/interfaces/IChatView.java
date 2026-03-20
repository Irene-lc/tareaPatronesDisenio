package edu.upb.chatupb_v2.controller.interfaces;

import edu.upb.chatupb_v2.controller.ChatsController;
import edu.upb.chatupb_v2.model.entities.message.Chats;
import edu.upb.chatupb_v2.model.entities.message.Contact;
import edu.upb.chatupb_v2.model.entities.message.Message;

import java.util.List;

public interface IChatView {
    void onloadContacts(List<Contact> contacts);
    void onloadMessages(List<Chats> chats);
    void agregarContacto(Contact contact);
    void actualizarContacto(Contact contact);
    void actualizarEstadoContacto(String id, boolean estado);
    void agregarMensajeUI(String texto, boolean esMio, String fechaHora, boolean leido, String idMensaje);
    void agregarMensajeUnicoUI(boolean esMio, String fechaHora, boolean leido, String idMensaje);
    void mostrarMensajeSistema(String texto);
    void fijarMensajeUI(String idMensaje, String texto);
    boolean showInvitationPopup(String nombreContacto);
    void ejecutarEfectoZumbido();
    void marcarZumbido(String idUsuario);
    void actualizarValores(String idUsuario);
    void onMessage(Message message);

    String getIdUsuarioActual();
    boolean isModoMensajeUnico();
    Contact getSelectedContact();
    ChatsController getChatsController();
}

