/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.model.network;

import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.controller.exception.OperationException;
import edu.upb.chatupb_v2.model.entities.message.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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


    @Override
    public void run() {
        try {
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println("Comando recibido: " + message);

                String split[] = message.split(Pattern.quote("|"));
                if (split.length == 0) {
                    return;
                }
                switch (split[0]) {
                    case "001":
                        Invitacion inv = Invitacion.parse(message);
                        notificar(inv);
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
                    case "004":
                        Hello hello = Hello.parse(message);
                        notificar(hello);
                        break;
                    case "005":
                        AceptarHello aceptarHello = AceptarHello.parse(message);
                        notificar(aceptarHello);
                        break;
                    case "006":
                        RechazarHello rechazarHello = RechazarHello.parse(message);
                        notificar(rechazarHello);
                        break;
                    case "007":
                        Mensaje mensaje = Mensaje.parse(message);
                        notificar(mensaje);
                        break;
                    case "008":
                        ConfirmarRecibido confirmarRecibido = ConfirmarRecibido.parse(message);
                        notificar(confirmarRecibido);
                    case "0018":
                        FueraLinea fueraLinea = FueraLinea.parse(message);
                        notificar(fueraLinea);
                        break;
                }
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                System.out.println("Socket cerrado por cliente");
            } else {
                System.out.println("Socket cerrado correctamente.");
            }
        } finally {
            close();
        }
    }

    public void addListener(SocketListener listener) {
        this.socketListener.add(listener);
    }

    public void removeListener(SocketListener listener) {
        this.socketListener.remove(listener);
    }
    public void notificar(Message message) {
        for (SocketListener listener : socketListener) {
            java.awt.EventQueue.invokeLater(() -> listener.onMessage(this, message));
        }
    }
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
            removeListener(Mediador.getInstance());
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
