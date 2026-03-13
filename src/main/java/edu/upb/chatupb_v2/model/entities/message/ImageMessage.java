package edu.upb.chatupb_v2.model.entities.message;

import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.controller.exception.OperationException;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.model.repository.MensajeDao;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;

public class ImageMessage extends Message {
    private String idEmisor;
    private String idReceptor;
    private String idMensaje;
    private String hora;
    private byte[] image;

    // Prefijo que identifica a un mensaje de imagen dentro de la tabla chats
    public static final String IMG_PREFIX = "IMG:";

    public ImageMessage() {
        super("021");
    }

    public ImageMessage(String idEmisor, String idReceptor, String idMensaje, String hora, byte[] image) {
        super("021");
        this.idEmisor  = idEmisor;
        this.idReceptor = idReceptor;
        this.idMensaje = idMensaje;
        this.hora      = hora;
        this.image     = image;
    }

    // Trama de red: 021|idEmisor|idMensaje|hora|<base64>
    @Override
    public String generarTrama() {
        return getCodigo() + "|"
                + idEmisor  + "|"
                + idMensaje + "|"
                + hora      + "|"
                + Base64.getEncoder().encodeToString(image)
                + System.lineSeparator();
    }

    // Parsea la trama recibida desde la red
    public static ImageMessage parse(String trama) {
        // Limite 5 para que el base64 (que puede contener '=') no se parta
        String[] split = trama.split(Pattern.quote("|"), 5);
        if (split.length != 5) {
            throw new IllegalArgumentException("Trama de imagen invalida: " + trama);
        }

        String idEmisor   = split[1];
        String idMensaje  = split[2];
        String hora       = split[3];
        byte[] imagen     = Base64.getDecoder().decode(split[4].trim());
        String idReceptor = Mediador.getInstance().getIdMio();

        return new ImageMessage(idEmisor, idReceptor, idMensaje, hora, imagen);
    }

    /**
     * Patrón Command — execute():
     *   1. Envía la imagen por socket al destinatario.
     *   2. Guarda en la tabla 'chats' como cualquier Mensaje,
     *      pero con "IMG:<base64>" en el campo mensajeTxt.
     */
    @Override
    public void execute(SocketClient client) throws IOException {
        // 1. Enviar por red
        client.send(this);

        // 2. Persistir en la tabla chats (solo el emisor guarda su propio envío)
        if (idEmisor.equals(Mediador.getInstance().getIdMio())) {
            try {
                String base64     = Base64.getEncoder().encodeToString(image);
                String textoBlob  = IMG_PREFIX + base64;   // "IMG:<base64>"

                Mensaje mensajeImagen = new Mensaje(
                        idMensaje,
                        "0",         // leido
                        idReceptor,
                        idEmisor,
                        hora,
                        textoBlob
                );
                new MensajeDao().save(mensajeImagen);
            } catch (Exception e) {
                e.printStackTrace();
                throw new OperationException("No se pudo guardar la imagen en la BD");
            }
        }
    }

    // Getters & Setters
    public String getIdEmisor()            { return idEmisor; }
    public void   setIdEmisor(String v)    { this.idEmisor = v; }
    public String getIdReceptor()          { return idReceptor; }
    public void   setIdReceptor(String v)  { this.idReceptor = v; }
    public String getIdMensaje()           { return idMensaje; }
    public void   setIdMensaje(String v)   { this.idMensaje = v; }
    public String getHora()                { return hora; }
    public void   setHora(String v)        { this.hora = v; }
    public byte[] getImage()               { return image; }
    public void   setImage(byte[] v)       { this.image = v; }
}
