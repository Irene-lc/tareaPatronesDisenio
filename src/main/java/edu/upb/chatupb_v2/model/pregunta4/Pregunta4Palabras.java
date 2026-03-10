package edu.upb.chatupb_v2.model.pregunta4;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Pregunta4Palabras implements IPregunta4{
    @Override
    public String modificar(String mensaje) {
        System.out.println("entra aca 2");
        List<String> palabras = new ArrayList<>();
        palabras.add("erda");
        palabras.add("ajo");
        palabras.add("holaa");
        palabras.add("chauu");
        palabras.add("faa");

        String[] split = mensaje.split(Pattern.quote(" "));

        String res = "";
        for (String s : split) {
            if (palabras.contains(s)) {
                res += "* ";
            } else {
                res += s + " ";
            }
        }
        System.out.println(res);
        return res;
    }
}
