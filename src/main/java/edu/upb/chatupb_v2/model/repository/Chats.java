/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.model.repository;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chats implements Serializable, Model {
//    public static final String ME_CODE = "kf3fc20a-766c-4rd4-813d-b1967a01fa9a";


    public static final class Column{
        public static final String ID_MENSAJE= "idMensaje";
        public static final String MENSAJETXT ="mensajeTxt";
        public static final String HORA ="hora";
        public static final String ID_EMISOR ="idEmisor";
        public static final String ID_RECEPTOR ="idReceptor";
        public static final String LEIDO = "leido";

    }
    @Override
    public void setId(String id) {
        this.idMensaje = id;
    }

    @Override
    public String getId() {
        return idMensaje;
    }

    private String idMensaje;
    private String mensajeTxt;
    private String hora;
    private String idEmisor;
    private String idReceptor;
    private String leido;
}