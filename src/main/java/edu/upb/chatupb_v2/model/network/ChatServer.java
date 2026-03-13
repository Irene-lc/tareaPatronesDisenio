/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.model.network;

import edu.upb.chatupb_v2.controller.Mediador;

import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer extends Thread {

    private static final int port = 1900;

    private final ServerSocket server;
    public ChatServer() throws IOException {
        this.server = new ServerSocket(port);
    }
    public ChatServer(SocketListener listener) throws IOException {
        this.server = new ServerSocket(port);
//        this.socketListener = listener;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Se agrego el Mediador");
                SocketClient socketClient = new SocketClient(this.server.accept()); // hasta que reciba una solicitud de coonexion
                socketClient.addListener(Mediador.getInstance());
                socketClient.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
