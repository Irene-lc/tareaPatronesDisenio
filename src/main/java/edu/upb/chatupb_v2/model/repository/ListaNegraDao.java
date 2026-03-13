package edu.upb.chatupb_v2.model.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListaNegraDao {

    private Connection connection;

    public ListaNegraDao(Connection connection) {
        this.connection = connection;
    }

    public void agregarBloqueado(String mio, String bloqueado) throws SQLException {
        String sql = "INSERT OR IGNORE INTO listanegra (id_usuario_duenio, id_usuario_bloqueado) VALUES (?, ?)";

        try (Connection connection = ConnectionDB.getInstance().getConection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, mio);
            ps.setString(2, bloqueado);
            ps.executeUpdate();
        }
    }

    public boolean estaBloqueado(String duenio, String bloqueado) throws SQLException {
        String sql = "SELECT 1 FROM listanegra WHERE id_usuario_duenio = ? AND id_usuario_bloqueado = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, duenio);
            ps.setString(2, bloqueado);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
