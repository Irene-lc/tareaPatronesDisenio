package edu.upb.chatupb_v2.model.entities.message;

import java.util.regex.Pattern;

public class RespuestaEncriptado extends Message {
    private String algEncriptacion;
    private String llave;

    public RespuestaEncriptado() {
        super("015");
    }

    public RespuestaEncriptado(String algEncriptacion, String llave) {
        super("015");
        this.algEncriptacion = algEncriptacion;
        this.llave = llave;
    }

    public static RespuestaEncriptado parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 3) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new RespuestaEncriptado(split[1], split[2]);
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + algEncriptacion + "|" + llave + System.lineSeparator();
    }

    public String getAlgEncriptacion() {
        return algEncriptacion;
    }

    public void setAlgEncriptacion(String algEncriptacion) {
        this.algEncriptacion = algEncriptacion;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }
}
