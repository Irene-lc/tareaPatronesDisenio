/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.model.network;

import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.controller.exception.OperationException;
import edu.upb.chatupb_v2.model.entities.message.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class SocketClient extends Thread {
    private final Socket socket;
    private final String ip;
    private final DataOutputStream dout;
    private final BufferedReader br;

    private String algoritmo;
    private SecretKey secretKey;

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
        ListaProtocolos listaProtocolos = new ListaProtocolos("AES_128,AES_256");
        send(listaProtocolos);
    }


    @Override
    public void run() {
        try {
            String message;
            while ((message = br.readLine()) != null) {
                if (!message.contains("|"))
                    message = desencriptar(message);

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
                        break;
                    case "010":
                        Zumbido zumbido = Zumbido.parse(message);
                        notificar(zumbido);
                        break;
                    case "014":
                        ListaProtocolos listaProtocolos = ListaProtocolos.parse(message);
                        String[] protocolos = listaProtocolos.getListaComandos().split(",");
                        boolean flag256 = false;
                        boolean flag128 = false;
                        for (String p : protocolos) {
                            if (p.equals("AES_256"))
                                flag256 = true;
                            if (p.equals("AES_128"))
                                flag128 = true;
                        }
                        if (!flag256 && !flag128)
                            socket.close();

                        if (flag256 && flag128) {
                            String[] listaEncriptado = new String[]{"AES_256", "AES_128"};
                            int r = new Random().nextInt(2);
                            algoritmo = listaEncriptado[r];

                        } else if (flag256) {
                            algoritmo = "AES_256";
                        } else if (flag128) {
                            algoritmo = "AES_128";
                        }

                        System.out.println("Se eligio: " + algoritmo);

                        try {
                            secretKey = generarLlave(algoritmo);
                            byte[] llaveByte = secretKey.getEncoded();
                            String llave = Base64.getEncoder().encodeToString(llaveByte);
                            System.out.println("llave: " + llave);
//                            String llave = Base64.getEncoder().encodeToString(generarLlave(elegido).getEncoded());
                            RespuestaEncriptado respuestaEncriptado = new RespuestaEncriptado(algoritmo,llave);
                            send(respuestaEncriptado);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "015":
                        RespuestaEncriptado respuestaEncriptado = RespuestaEncriptado.parse(message);
                        algoritmo = respuestaEncriptado.getAlgEncriptacion();
                        String llave = respuestaEncriptado.getLlave();
                        byte[] llaveDecodificada = Base64.getDecoder().decode(llave);
                        secretKey = new SecretKeySpec(llaveDecodificada, "AES");
                        break;
                    case "0018":
                        FueraLinea fueraLinea = FueraLinea.parse(message);
                        notificar(fueraLinea);
                        break;
                }
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                if (socketListener != null){
                    for (SocketListener listener : socketListener) {
                        java.awt.EventQueue.invokeLater(() -> listener.onDisconnect(this));
                    }
                }
                e.printStackTrace();
                System.out.println("Socket cerrado por cliente");
            } else {
                System.out.println("Socket cerrado correctamente.");
            }
        } finally {
            close();
        }
    }

    public String desencriptar(String message) {
        try {
            byte[] messageBytes = Base64.getDecoder().decode(message);
            Cipher cipher = Cipher.getInstance("AES"); // inicia el algoritmo
            cipher.init(Cipher.DECRYPT_MODE, secretKey); // carga la clave
            byte[] messageDesencriptado = cipher.doFinal(messageBytes); //ejecuta deciphrado
            return new String(messageDesencriptado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    public String encriptar(String menssage) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] mensajeEncriptadobyte = cipher.doFinal(menssage.getBytes());
            return Base64.getEncoder().encodeToString(mensajeEncriptadobyte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menssage;
    }
    public SecretKey generarLlave(String elegido) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        if (elegido.equals("AES_128")) {
            keyGenerator.init(128);
        } else {
            keyGenerator.init(256);
        }
        return keyGenerator.generateKey();
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
        if (message instanceof RespuestaEncriptado || message instanceof ListaProtocolos) {
            messageStr = message.generarTrama();
        } else {
            messageStr = encriptar(messageStr) + System.lineSeparator();
        }
        System.out.println("ENVIANDO: " + messageStr);
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
