package edu.upb.chatupb_v2.model.entities.message;

import java.util.regex.Pattern;

public class ConfirmarRecibido extends Message {
    private String idMensaje;

    public ConfirmarRecibido() {
        super("008");
    }

    public ConfirmarRecibido(String idMensaje) {
        super("008");
        this.idMensaje = idMensaje;
    }

    public static ConfirmarRecibido parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 2) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new ConfirmarRecibido(split[1]);
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + idMensaje + System.lineSeparator();
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }
}
