package edu.upb.chatupb_v2.model.entities.message;

import edu.upb.chatupb_v2.model.network.SocketClient;

import java.io.IOException;
import java.util.regex.Pattern;

public class FueraLinea extends Message {
    private String idUsuario;

    public FueraLinea() {
        super("0018");
    }

    public FueraLinea(String idUsuario) {
        super("0018");
        this.idUsuario = idUsuario;
    }

    public static FueraLinea parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 2) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new FueraLinea(split[1]);
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + idUsuario + System.lineSeparator();
    }

    @Override
    public void execute(SocketClient client) throws IOException {
        client.send(this);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
