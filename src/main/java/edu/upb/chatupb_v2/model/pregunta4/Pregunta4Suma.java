package edu.upb.chatupb_v2.model.pregunta4;

import java.util.regex.Pattern;

public class Pregunta4Suma implements IPregunta4{
    @Override
    public String modificar(String mensaje) {
        System.out.println("entra aca");
        String res = "";
        if (mensaje.contains("+") && mensaje.contains("=")) {
            String[] split = mensaje.split(Pattern.quote(" "));
            String actual = " ";
            String siguiente = " ";
            for (int i = 0; i < split.length - 1; i++) {
                siguiente = split[i + 1];
                if (siguiente.equals("+")) {
                    actual = split[i];
                    siguiente = split[i + 2];
                    break;
                }
            }
            int a = Integer.parseInt(actual);
            int b = Integer.parseInt(siguiente);
            int r = a + b;
            String rsTR = String.valueOf(r);

            for (String s : split) {
                res += s + " ";
                if (s.equals("=")) {
                    res += rsTR + " ";
                }
            }
        }
        return res;
    }
}
