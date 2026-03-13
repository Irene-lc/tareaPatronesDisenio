package edu.upb.chatupb_v2.model.repository;

import edu.upb.chatupb_v2.model.entities.message.Mensaje;

import java.util.List;

public class MensajeDao {
    private DaoHelper<Mensaje> helper;

    public MensajeDao() {
        helper = new DaoHelper<>();
    }
    public void save(Mensaje mensaje) throws Exception {

        String query = """
        INSERT INTO chats(idMensaje, mensajeTxt, hora, idEmisor, idReceptor, leido)
        VALUES (?,?,?,?,?,?)
        """;

        DaoHelper.QueryParameters params = pst -> {
            pst.setString(1, mensaje.getIdMensaje());
            pst.setString(2, mensaje.getMensaje());
            pst.setString(3, mensaje.getHora());
            pst.setString(4, mensaje.getIdEmisor());
            pst.setString(5, mensaje.getIdReceptor());
            pst.setString(6, mensaje.getLeido());
        };

        helper.insert(query, params, mensaje);
    }
    public void updateLeido(String id) throws Exception {
        String query = "UPDATE chats SET leido = 1 WHERE idMensaje = ? AND leido = 0;";
        DaoHelper.QueryParameters params = pst -> {
            pst.setString(1, id);
        };
        helper.update(query, params);
    }
    public List<Mensaje> findByContact(String idEmisor, String idReceptor) throws Exception {

        String query = """
        SELECT idMensaje, mensajeTxt, hora, idEmisor, idReceptor, leido
        FROM chats
        WHERE (idEmisor = ? AND idReceptor = ?)
           OR (idEmisor = ? AND idReceptor = ?)
        ORDER BY datetime(hora) ASC
    """;

        DaoHelper.QueryParameters params = pst -> {
            pst.setString(1, idEmisor);
            pst.setString(2, idReceptor);
            pst.setString(3, idReceptor);
            pst.setString(4, idEmisor);
        };

        DaoHelper.ResultReader<Mensaje> reader = result -> {

            Mensaje m = new Mensaje();

            m.setIdMensaje(result.getString("idMensaje"));
            m.setMensaje(result.getString("mensajeTxt"));
            m.setHora(result.getString("hora"));
            m.setIdEmisor(result.getString("idEmisor"));
            m.setIdReceptor(result.getString("idReceptor"));
            m.setLeido(result.getString("leido"));

            return m;
        };

        return helper.executeQuery(query, params, reader);
    }
}
