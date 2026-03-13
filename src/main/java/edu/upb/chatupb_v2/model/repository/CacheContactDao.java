package edu.upb.chatupb_v2.model.repository;

import edu.upb.chatupb_v2.model.entities.message.Contact;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CacheContactDao implements IContactDao{

    private final Map<String, Contact> cacheContact = new HashMap<>();
    private final IContactDao daoOriginal;
    public CacheContactDao(IContactDao daoOriginal) {
        super();
        this.daoOriginal = daoOriginal;
    }

    public List<Contact> findAll() throws ConnectException, SQLException {
        if (cacheContact.isEmpty()) {
            List<Contact> list = daoOriginal.findAll();
            list.forEach(contact -> cacheContact.put(contact.getId(), contact));
            return list;
        }
        return new ArrayList<>(cacheContact.values());
    }
    public List<Contact> findAllMenosYo() throws ConnectException, SQLException {
        if (cacheContact.isEmpty()) {
            List<Contact> list = daoOriginal.findAllMenosYo();
            list.forEach(contact -> cacheContact.put(contact.getId(), contact));
            return list;
        }
        return new ArrayList<>(cacheContact.values());
    }

    public boolean exist(String id) throws ConnectException, SQLException {
        return cacheContact.containsKey(id);
    }

    public boolean existById(String id) throws ConnectException, SQLException {
        if (cacheContact.containsKey(id)) {
            return true;
        }
        return daoOriginal.existById(id);
    }

    public Contact findById(String id) throws ConnectException, SQLException {
        if (cacheContact.containsKey(id)) {
            return cacheContact.get(id);
        }
        return daoOriginal.findById(id);
    }

    public void save(Contact contact) throws Exception {
        System.out.println("GUARDANDO EN CACHE");
        daoOriginal.save(contact);
        cacheContact.put(contact.getId(), contact);
    }

    public void update(Contact contact) throws Exception {
        daoOriginal.update(contact);
        cacheContact.put(contact.getId(), contact);
    }

}
