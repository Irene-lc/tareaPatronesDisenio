package edu.upb.chatupb_v2.bl.message;

import java.util.regex.Pattern;

public class Zumbido extends Message {
    private String idUsuario;

    public Zumbido() {
        super("010");
    }

    public Zumbido(String idUsuario) {
        super("010");
        this.idUsuario = idUsuario;
    }

    public static Zumbido parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 2) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new Zumbido(split[1]);
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

