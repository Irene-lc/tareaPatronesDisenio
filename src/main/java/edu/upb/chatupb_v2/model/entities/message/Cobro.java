package edu.upb.chatupb_v2.model.entities.message;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Cobro {
    private String Qr;
    private BigDecimal importe;
    private String red;

    public Cobro(String qr, BigDecimal importe, String red) {
        Qr = qr;
        this.importe = importe;
        this.red = red;
    }
}
