package edu.upb.chatupb_v2.model.entities.message;

import java.util.regex.Pattern;

public class Parcial extends Message {
    private String idUsuario;
    private String nombreCliente;
    private String ip;

    public Parcial() {
        super("020");
    }

    public Parcial(String idUsuario, String nombreCliente, String ip) {
        super("020");
        this.idUsuario = idUsuario;
        this.nombreCliente = nombreCliente;
        this.ip = ip;
    }

    public static Parcial parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 4) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new Parcial(split[1], split[2], split[3]);
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + idUsuario + "|" + nombreCliente + "|" + ip + System.lineSeparator();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
