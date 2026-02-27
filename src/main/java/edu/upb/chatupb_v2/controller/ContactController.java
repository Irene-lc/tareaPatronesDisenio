package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.entities.message.Hello;
import edu.upb.chatupb_v2.model.entities.message.Invitacion;
import edu.upb.chatupb_v2.model.entities.message.Message;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.model.repository.Contact;
import edu.upb.chatupb_v2.model.repository.ContactDao;
import edu.upb.chatupb_v2.view.iChatView;

import java.net.ConnectException;
import java.sql.SQLException;
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

    public Contact agregarContactoBd(String idCliente, String nombreCliente, String ipCliente) {
        Contact nuevo = new Contact(idCliente, nombreCliente, ipCliente, false);
        try {
            this.contactDao.save(nuevo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nuevo;
    }
    public void notificarContactos() {
        try {
            List<Contact> contacts = contactDao.findAll();
            for (Contact c : contacts) {
                SocketClient client = new SocketClient(c.getIp());
                client.start();
                Message message = new Hello(Mediador.getInstance().getNombre());
                client.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
}
