package edu.upb.chatupb_v2.bl.message;

import java.util.regex.Pattern;

public class RechazarHello extends Message {
    public RechazarHello() {
        super("006");
    }

    public static RechazarHello parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if (split.length != 1) {
            throw new IllegalArgumentException("Formato de trama no valido");
        }
        return new RechazarHello();
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + System.lineSeparator();
    }
}

