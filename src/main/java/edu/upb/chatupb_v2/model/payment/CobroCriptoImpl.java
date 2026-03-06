package edu.upb.chatupb_v2.model.payment;

import edu.upb.chatupb_v2.model.entities.message.Cobro;

import java.math.BigDecimal;
import java.util.UUID;

public class CobroCriptoImpl implements ICobros {
    @Override
    public Cobro cobrar(BigDecimal importe) {
        System.out.println("Cobrando con Cripto...");
        String qR = UUID.randomUUID().toString();
        String red = "Poligon";
        return new Cobro(qR, importe, red);
    }

    //tiene que mandar la blockchain/red en la caul se cobra
    //monto, qr y red

}
