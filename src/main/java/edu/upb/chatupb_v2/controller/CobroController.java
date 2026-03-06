package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.entities.message.Cobro;
import edu.upb.chatupb_v2.model.payment.CobroCriptoImpl;
import edu.upb.chatupb_v2.model.payment.CobroFiatImpl;
import edu.upb.chatupb_v2.model.payment.ICobros;

import java.math.BigDecimal;

public class CobroController {
    // metodo cobrar recibe: monto y que seleciono el usuario, cripto o bob

    // en funcion del tipo de cobro, se tiene que llamar al metodo cobrar y debe retornar un objeto cobro, donde la red sea base o el campo red es null

    // atributo cobro;
    String cobro;
    public Cobro cobrar(BigDecimal importe, String tipoPago) {
        ICobros cobros ;
        if (tipoPago.equals("bolivianos")) {
            cobros = new CobroFiatImpl();
        } else {
            cobros = new CobroCriptoImpl();
        }
        return cobros.cobrar(importe);
    }
}
