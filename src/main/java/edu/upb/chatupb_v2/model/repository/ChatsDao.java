package edu.upb.chatupb_v2.model.repository;

import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class ChatsDao {


    private DaoHelper<Chats> helper;

    public ChatsDao() {
        helper = new DaoHelper<>();
    }

    DaoHelper.ResultReader<Chats> resultReader = result -> {
        Chats prefacturaSync = new Chats();
        if (existColumn(result, Chats.Column.ID_MENSAJE)) {
            prefacturaSync.setId(result.getString(Chats.Column.ID_MENSAJE));
        }
        if (existColumn(result, Chats.Column.MENSAJETXT)) {
            prefacturaSync.setMensajeTxt(result.getString(Chats.Column.MENSAJETXT));
        }
        if (existColumn(result, Chats.Column.HORA)) {
            prefacturaSync.setHora(result.getString(Chats.Column.HORA));
        }
        if (existColumn(result, Chats.Column.ID_EMISOR)) {
            prefacturaSync.setIdEmisor(result.getString(Chats.Column.ID_EMISOR));
        }
        if (existColumn(result, Chats.Column.ID_RECEPTOR)) {
            prefacturaSync.setIdReceptor(result.getString(Chats.Column.ID_RECEPTOR));
        }
        return prefacturaSync;
    };

    public static boolean existColumn(ResultSet result, String columnName) {
        try {
            result.findColumn(columnName);
            return true;
        } catch (SQLException sqlex) {
            //log.error("No se encontro la columna: {}", columnName); // log innecesario
        }
        return false;
    }

    public List<Chats> findAll() throws ConnectException, SQLException {
        String query = "SELECT * FROM chats";
        return helper.executeQuery(query, resultReader);
    }

    public boolean exist(String argument) throws ConnectException, SQLException {
        String query = "SELECT count(*) FROM chats WHERE " + argument;
        return helper.executeQueryCount(query, null) == 1;
    }

    public boolean existById(String idMensaje) throws ConnectException, SQLException {
        String query = "SELECT count(*) FROM chats WHERE idMensaje ='" + idMensaje + "'";
        return helper.executeQueryCount(query, null) == 1;
    }

    public void update(String query) throws Exception {
        helper.update(query, null);
    }

    public void save(Chats chats) throws Exception {
        String query = "INSERT INTO chats(idMensaje, mensajeTxt, hora, idEmisor, idReceptor) values (?,?,?,?,?)";
        DaoHelper.QueryParameters params = new DaoHelper.QueryParameters() {
            @Override
            public void setParameters(PreparedStatement pst) throws SQLException {
                pst.setString(1, chats.getIdMensaje());
                pst.setString(2, chats.getMensajeTxt());
                pst.setString(3, chats.getHora());
                pst.setString(4, chats.getIdEmisor());
                pst.setString(5, chats.getIdReceptor());
            }
        };
        helper.insert(query, params, chats);
    }

    public void update(String query, String conditionWhere) throws SQLException, ConnectException {
        if (query.trim().endsWith("%s")) {
            query = String.format(query, conditionWhere);
        } else {
            query = String.format("%s %s", query, conditionWhere);
        }
        helper.update(query, null);
    }
    public List<Chats> findByContact(String idEmisor, String idReceptor) throws Exception {
        String query = """
        SELECT idMensaje, mensajeTxt, hora, idEmisor, idReceptor
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

        return helper.executeQuery(query, params, resultReader);
    }
}
