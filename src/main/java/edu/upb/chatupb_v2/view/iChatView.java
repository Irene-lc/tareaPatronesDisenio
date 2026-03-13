package edu.upb.chatupb_v2.view;

import edu.upb.chatupb_v2.model.entities.message.Contact;
import edu.upb.chatupb_v2.model.entities.message.Mensaje;

import java.util.List;

public interface iChatView {
    void onloadContacts(List<Contact> contacts);
    void onloadMessages(List<Mensaje> mensajes);
}
