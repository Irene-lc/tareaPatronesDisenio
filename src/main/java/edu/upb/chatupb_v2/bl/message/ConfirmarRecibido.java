package edu.upb.chatupb_v2.bl.message;

import java.util.regex.Pattern;

public class ConfirmarRecibido extends Message {
    private String idUsuario;

    public ConfirmarRecibido() {
        super("008");
    }

    public ConfirmarRecibido(String idUsuario) {
        super("008");
        this.idUsuario = idUsuario;
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
        return getCodigo() + "|" + idUsuario + System.lineSeparator();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
