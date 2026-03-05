package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.repository.Contact;
import edu.upb.chatupb_v2.model.repository.ContactDao;
import edu.upb.chatupb_v2.view.iChatView;

import java.util.ArrayList;
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
            List<Contact> copia = new ArrayList<>();
            if (contacts.toArray().length > 1) {
                copia = new ArrayList<>(contacts.subList(1, contacts.size()));
            }
            if (!copia.isEmpty()) {
                for (Contact c : copia) {
                    c.setStateConnect(false);
                }
            } else {
                for (Contact c : contacts) {
                    c.setStateConnect(false);
                }
            }
            view.onloadContacts(contacts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
