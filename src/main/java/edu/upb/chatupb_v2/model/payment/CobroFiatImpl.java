package edu.upb.chatupb_v2.model.payment;

import edu.upb.chatupb_v2.model.entities.message.Cobro;

import java.math.BigDecimal;
import java.util.UUID;

public class CobroFiatImpl implements ICobros {
    @Override
    public Cobro cobrar(BigDecimal importe) {
        System.out.println("Cobrando con Fiat...");
        String qR = UUID.randomUUID().toString();
        return new Cobro(qR, importe, null);
    }

    // que genere un mensaje?. no se manda red
    // monto, qr
}
