package edu.upb.chatupb_v2.bl.message;

import java.util.regex.Pattern;

public class FijarMensaje extends Message {
    private String idMensaje;

    public FijarMensaje() {
        super("011");
    }

    public FijarMensaje(String idMensaje) {
        super("011");
        this.idMensaje = idMensaje;
    }

    public static FijarMensaje parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 2) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new FijarMensaje(split[1]);
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
