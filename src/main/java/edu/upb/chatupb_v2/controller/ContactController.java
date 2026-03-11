package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.entities.message.Contact;
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
            List<Contact> contacts = contactDao.findAllMenosYo();
            for (Contact c : contacts) {
                c.setStateConnect(false);
            }
            view.onloadContacts(contacts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
