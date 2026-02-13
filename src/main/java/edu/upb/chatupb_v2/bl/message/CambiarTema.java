package edu.upb.chatupb_v2.bl.message;

import java.util.regex.Pattern;

public class CambiarTema extends Message {
    private String idUsuario;
    private String idTema;

    public CambiarTema() {
        super("013");
    }

    public CambiarTema(String idUsuario, String idTema) {
        super("013");
        this.idUsuario = idUsuario;
        this.idTema = idTema;
    }

    public static CambiarTema parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 3) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new CambiarTema(split[1], split[2]);
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + idUsuario + "|" + idTema + System.lineSeparator();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdTema() {
        return idTema;
    }

    public void setIdTema(String idTema) {
        this.idTema = idTema;
    }
}
