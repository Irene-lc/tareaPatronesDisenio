package edu.upb.chatupb_v2.model.entities.message;

import java.util.regex.Pattern;

public class ListaProtocolos extends Message {
    private String listaComandos;

    public ListaProtocolos() {
        super("014");
    }

    public ListaProtocolos(String listaComandos) {
        super("014");
        this.listaComandos = listaComandos;
    }

    public static ListaProtocolos parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 2) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new ListaProtocolos(split[1]);
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + listaComandos + System.lineSeparator();
    }

    public String getListaComandos() {
        return listaComandos;
    }

    public void setListaComandos(String listaComandos) {
        this.listaComandos = listaComandos;
    }
}
