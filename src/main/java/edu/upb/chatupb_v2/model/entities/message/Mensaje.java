package edu.upb.chatupb_v2.model.entities.message;

import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.controller.exception.OperationException;
import edu.upb.chatupb_v2.model.network.SocketClient;
//import edu.upb.chatupb_v2.model.repository.ChatsDao;
import edu.upb.chatupb_v2.model.repository.MensajeDao;
import edu.upb.chatupb_v2.model.repository.Model;

import java.io.IOException;
import java.util.regex.Pattern;

public class Mensaje extends Message implements Model {
    private String idMensaje;
    private String mensaje;
    private String hora;
    private String idEmisor;
    private String idReceptor;
    private String leido;

    public Mensaje() {
        super("007");
    }

    public Mensaje(String idMensaje, String leido, String idReceptor, String idEmisor, String hora, String mensaje) {
        super("007");
        this.idMensaje = idMensaje;
        this.leido = leido;
        this.idReceptor = idReceptor;
        this.idEmisor = idEmisor;
        this.hora = hora;
        this.mensaje = mensaje;
    }

    public static Mensaje parse(String trama) {

        String[] split = trama.split(Pattern.quote("|"),4);

        if (split.length != 4) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }

        String idEmisor = split[1];
        String idMensaje = split[2];
        String mensaje = split[3];

        String hora = Mediador.getInstance().obtenerHoraActual();
        String idReceptor = Mediador.getInstance().getIdMio();

        return new Mensaje(
                idMensaje,
                "0",
                idReceptor,   // receptor
                idEmisor,     // emisor
                hora,
                mensaje
        );
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + idEmisor + "|" + idMensaje + "|" + mensaje + System.lineSeparator();
    }

    @Override
    public void execute(SocketClient client) throws IOException {
        try {
            client.send(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MensajeDao dao = new MensajeDao();
            if (idEmisor.equals(Mediador.getInstance().getIdMio())) {
                dao.save(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("No se pudo guardar el mensaje en BD");
        }
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getLeido() {
        return leido;
    }

    public void setLeido(String leido) {
        this.leido = leido;
    }

    @Override
    public String toString() {
        return generarTrama();
    }

    @Override
    public void setId(String id) {
        this.idMensaje = id;
    }

    @Override
    public String getId() {
        return idMensaje;
    }
}
