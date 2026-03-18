package edu.upb.chatupb_v2.model.repository;

import edu.upb.chatupb_v2.model.entities.message.Contact;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

public interface IContactDao {
    List<Contact> findAll() throws ConnectException, SQLException;
    boolean existById(String id) throws ConnectException, SQLException;
    Contact findById(String id) throws ConnectException, SQLException;
    boolean exist(String id) throws ConnectException, SQLException;
    void save(Contact contact) throws Exception;
    void update(Contact contact) throws Exception;
    void updateIp(String id, String ip) throws Exception;
    List<Contact> findAllMenosYo() throws ConnectException, SQLException;
}
