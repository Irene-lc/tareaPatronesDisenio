/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.bl.server;

import edu.upb.chatupb_v2.bl.message.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author rlaredo
 */
public class SocketClient extends Thread {
    private final Socket socket;
    private final String ip;
    private final DataOutputStream dout;
    private final BufferedReader br;
    private List<SocketListener> socketListener = new ArrayList<>();

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
        dout = new DataOutputStream(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public SocketClient(String ip) throws IOException {
        this.socket = new Socket(ip, 1900); // solicita conectarse
        this.ip = ip;
        dout = new DataOutputStream(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void addListener(SocketListener listener) {
        this.socketListener.add(listener);
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = br.readLine()) != null) {
//                System.out.println("Comando recibido: " + message);
                System.out.println(message);

                String split[] = message.split(Pattern.quote("|"));
                if (split.length == 0) {
                    return;
                }
                switch (split[0]) {
                    case "001":
                        Invitacion inv = Invitacion.parse(message);
//                        if (socketListener != null) {
//                            socketListener.onInvitacion(inv);
//                        }
//                        inv.setIp(ip);
                        notificar(inv);
//                    System.out.println(inv.generarTrama());
                        break;

                    case "002":
                        Aceptar acept = Aceptar.parse(message);
//                        acept.setIp(ip);
                        notificar(acept);
                        break;
                    case "003":
                        Rechazar rechazar = Rechazar.parse(message);
                        notificar(rechazar);
                        break;
                    case "007":
                        Mensaje mensaje = Mensaje.parse(message);
                        notificar(mensaje);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notificar(Message message) {
        for (SocketListener listener : socketListener) {
            java.awt.EventQueue.invokeLater(() -> listener.onMessage(this, message));
        }
    }

    //    public void send(String message) throws IOException {
//        message = message + System.lineSeparator();
//        try {
//            dout.write(message.getBytes("UTF-8"));
//            dout.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void send(Message message) throws IOException { //enviar mensaje a quien me habló
        String messageStr = message.generarTrama();
        try {
            dout.write(messageStr.getBytes("UTF-8"));
            dout.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.socket.close();
            this.br.close();
            this.dout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIp() {
        return ip;
    }
}
