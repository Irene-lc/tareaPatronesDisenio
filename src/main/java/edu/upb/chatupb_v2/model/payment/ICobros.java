package edu.upb.chatupb_v2.model.payment;

import edu.upb.chatupb_v2.model.entities.message.Cobro;

import java.math.BigDecimal;

public interface ICobros {
    Cobro cobrar(BigDecimal importe);
}
