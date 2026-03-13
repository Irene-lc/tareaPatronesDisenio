package edu.upb.chatupb_v2.view;

import edu.upb.chatupb_v2.model.entities.message.Chats;
import edu.upb.chatupb_v2.model.entities.message.Contact;

import java.util.List;

public interface iChatView {
    void onloadContacts(List<Contact> contacts);
    void onloadMessages(List<Chats> chats);
}
