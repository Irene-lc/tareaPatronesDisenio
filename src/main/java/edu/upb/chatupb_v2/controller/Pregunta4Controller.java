package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.payment.CobroCriptoImpl;
import edu.upb.chatupb_v2.model.payment.CobroFiatImpl;
import edu.upb.chatupb_v2.model.pregunta4.IPregunta4;
import edu.upb.chatupb_v2.model.pregunta4.Pregunta4Palabras;
import edu.upb.chatupb_v2.model.pregunta4.Pregunta4Suma;

public class Pregunta4Controller {
    public String modificar(String mensaje) {

        String res = "";
        res = new Pregunta4Suma().modificar(mensaje);
        res = new Pregunta4Palabras().modificar(res);

        return res;
    }
}
