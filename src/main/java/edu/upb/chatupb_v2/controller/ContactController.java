package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.entities.message.Hello;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.model.repository.Contact;
import edu.upb.chatupb_v2.model.repository.ContactDao;
import edu.upb.chatupb_v2.view.iChatView;

import java.util.List;

public class ContactController {
    private ContactDao contactDao;
    private iChatView view;

    public ContactController(iChatView view) {
        this.contactDao = new ContactDao();
        this.view = view;
    }
    public void onload() {
        try {
            List<Contact> contacts = contactDao.findAll();
            view.onloadContacts(contacts);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
