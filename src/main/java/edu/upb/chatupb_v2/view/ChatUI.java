package edu.upb.chatupb_v2.view;

import edu.upb.chatupb_v2.controller.ContactController;
import edu.upb.chatupb_v2.model.entities.message.*;
import edu.upb.chatupb_v2.controller.Mediador;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.UUID;


public class ChatUI extends JFrame implements iChatView {
    public String idUsuarioActual;
    private DefaultListModel<Contact> contacModel = new DefaultListModel<>();

    @Setter
    public ContactController contactController;

    public ChatUI() {
        initComponents();
        this.jContactos.setCellRenderer(new ContactRenderer());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jtMensaje = new JTextField();
        jPanelChat = new JPanel();
        jPanelChat.setLayout(new BoxLayout(jPanelChat, BoxLayout.Y_AXIS));
        jPanelChat.setBackground(Color.WHITE);

        jScrollPaneChat = new JScrollPane(jPanelChat);
        jScrollPaneChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jContactos = new JList<>();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        jBtnEnviar = new JButton("Enviar");
        jBtnEnviar.addActionListener(evt -> jBtnEnviarActionPerformed(evt));

        // ===== BOTON FOTO — usa FileDialog en lugar de JFileChooser (mejor compatibilidad Mac) =====
        jBtnFoto = new JButton("Foto");
        jBtnFoto.setToolTipText("Enviar imagen");
        jBtnFoto.addActionListener(evt -> jBtnFotoActionPerformed(evt));

        jBtnNuevaConexion = new JButton("+");
        jBtnNuevaConexion.addActionListener(evt -> jBtnNuevaConexionActionPerformed(evt));

        jBtnOff = new JButton("off");
        jBtnOff.addActionListener(evt -> jBtnOffActionPerformed(evt));

        setLayout(new BorderLayout());

        // PANEL IZQUIERDO (CONTACTOS)
        JPanel panelContactos = new JPanel(new BorderLayout());
        panelContactos.setPreferredSize(new Dimension(220, 0));
        panelContactos.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JLabel lblContactos = new JLabel("Contactos", SwingConstants.CENTER);
        lblContactos.setFont(new Font("Arial", Font.BOLD, 14));
        lblContactos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jContactos = new JList<>(contacModel);
        jContactos.setCellRenderer(new ContactRenderer());
        jContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jContactos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contact seleccionado = jContactos.getSelectedValue();
                if (seleccionado != null) {
                    idUsuarioActual = seleccionado.getId();
                    limpiarChat();
                    Mediador.getInstance().cargarMensajes(idUsuarioActual);
                }
            }
        });

        jContactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Contact seleccionado = jContactos.getSelectedValue();
                    if (seleccionado != null) {
                        idUsuarioActual = seleccionado.getId();
                        Mediador.getInstance().enviarHello(idUsuarioActual);
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    Contact seleccionado = jContactos.getSelectedValue();
                    if (seleccionado != null) {
                        idUsuarioActual = seleccionado.getId();
                        Zumbido zumbido = new Zumbido(Mediador.getInstance().getIdMio());
                        Mediador.getInstance().sendMessage(idUsuarioActual, zumbido);
                    }
                }
            }
        });

        JScrollPane scrollContactos = new JScrollPane(jContactos);
        scrollContactos.setBorder(null);

        panelContactos.add(lblContactos, BorderLayout.NORTH);
        panelContactos.add(scrollContactos, BorderLayout.CENTER);

        // PANEL DERECHO (CHAT)
        JPanel panelChat = new JPanel(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperior.add(jBtnOff);
        panelSuperior.add(jBtnNuevaConexion);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 0));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 4, 0));
        panelBotones.add(jBtnFoto);
        panelBotones.add(jBtnEnviar);

        panelInferior.add(jtMensaje, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        panelChat.add(panelSuperior, BorderLayout.NORTH);
        panelChat.add(jScrollPaneChat, BorderLayout.CENTER);
        panelChat.add(panelInferior, BorderLayout.SOUTH);

        add(panelContactos, BorderLayout.WEST);
        add(panelChat, BorderLayout.CENTER);

        if (Mediador.getInstance().dbVacia()) {
            showNombrePopup();
        }
        Mediador.getInstance().cargarMisDatos();
    }

    // ===== BOTON FOTO — FileDialog funciona nativamente en Mac =====
    private void jBtnFotoActionPerformed(ActionEvent evt) {
        if (idUsuarioActual == null) {
            mostrarMensajeSistema("Selecciona un contacto primero");
            return;
        }

        // FileDialog usa el dialogo nativo del sistema operativo (funciona en Mac, Windows y Linux)
        FileDialog fileDialog = new FileDialog(this, "Seleccionar imagen", FileDialog.LOAD);
        fileDialog.setFilenameFilter((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                    || lower.endsWith(".png") || lower.endsWith(".gif")
                    || lower.endsWith(".bmp");
        });
        fileDialog.setVisible(true); // bloquea hasta que el usuario elige o cancela

        String directory = fileDialog.getDirectory();
        String filename  = fileDialog.getFile();

        // Si el usuario canceló, filename es null
        if (filename == null) return;

        try {
            File file = new File(directory, filename);
            byte[] imageBytes = java.nio.file.Files.readAllBytes(file.toPath());
            Mediador.getInstance().enviarImagen(idUsuarioActual, imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeSistema("Error al cargar la imagen: " + e.getMessage());
        }
    }

    public void agregarImagenUI(byte[] imageBytes, boolean esMio, String hora, String idMensaje) {
        String textoBlob = ImageMessage.IMG_PREFIX + java.util.Base64.getEncoder().encodeToString(imageBytes);
        agregarMensajeUI(textoBlob, esMio, hora, false, idMensaje);
    }

    private void jBtnEnviarActionPerformed(ActionEvent evt) {
        String mensajeTxt = jtMensaje.getText();
        if (mensajeTxt.isEmpty() || idUsuarioActual == null) return;

        System.out.println("Enviando 007...");
        Mensaje mensaje = new Mensaje(
                UUID.randomUUID().toString(),
                "0",
                idUsuarioActual,
                Mediador.getInstance().getIdMio(),
                Mediador.getInstance().obtenerHoraActual(),
                mensajeTxt
        );

        Mediador.getInstance().sendMessage(idUsuarioActual, mensaje);
        agregarMensajeUI(mensajeTxt, true, mensaje.getHora(), false, mensaje.getIdMensaje());
        jtMensaje.setText("");
    }

    private void jBtnOffActionPerformed(ActionEvent evt) {
        try {
            System.out.println("Enviando 0018...");
            Message message = new FueraLinea(Mediador.getInstance().getIdMio());
            Mediador.getInstance().enviarMensajeATodos(message);
            mostrarMensajeSistema("CONEXION TERMINADA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jBtnNuevaConexionActionPerformed(ActionEvent evt) {
        showNuevaConexionPopup();
    }

    // ===== BURBUJA — detecta IMG: y renderiza imagen en lugar de texto =====
    private JPanel crearBurbuja(String texto, boolean esMio, String hora, boolean leido, String idMensaje) {
        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        bubble.setOpaque(true);
        bubble.setBackground(esMio ? new Color(173, 216, 230) : new Color(230, 230, 230));

        if (texto.startsWith(ImageMessage.IMG_PREFIX)) {
            // ===== CONTENIDO: IMAGEN =====
            try {
                String base64 = texto.substring(ImageMessage.IMG_PREFIX.length());
                byte[] bytes = java.util.Base64.getDecoder().decode(base64);
                BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));
                if (bi != null) {
                    Image scaled = bi.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                    bubble.add(new JLabel(new ImageIcon(scaled)), BorderLayout.CENTER);
                } else {
                    bubble.add(new JLabel("[Imagen no válida]"), BorderLayout.CENTER);
                }
            } catch (Exception e) {
                bubble.add(new JLabel("[Error al mostrar imagen]"), BorderLayout.CENTER);
            }
        } else {
            // ===== CONTENIDO: TEXTO =====
            JLabel lblTexto = new JLabel(
                    "<html><div style='max-width:250px;'>" + texto + "</div></html>"
            );
            bubble.add(lblTexto, BorderLayout.CENTER);
        }

        // ===== HORA Y CHECKS =====
        JLabel lblHora = new JLabel(hora);
        lblHora.setFont(new Font("Arial", Font.PLAIN, 9));
        lblHora.setForeground(Color.DARK_GRAY);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        panelInferior.setOpaque(false);
        panelInferior.add(lblHora);

        if (esMio) {
            JLabel lblCheck = new JLabel(leido ? "\u2714\u2714" : "\u2714");
            lblCheck.setName(idMensaje);
            lblCheck.setForeground(leido ? Color.BLUE : Color.GRAY);
            panelInferior.add(lblCheck);
        }

        bubble.add(panelInferior, BorderLayout.SOUTH);
        return bubble;
    }

    private JPanel crearFilaMensaje(String texto, boolean esMio, String fechaHora, boolean leido, String idMensaje) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Las imágenes necesitan más altura que los mensajes de texto
        int altura = texto.startsWith(ImageMessage.IMG_PREFIX) ? 220 : 60;
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, altura));

        String soloHora = formatearSoloHora(fechaHora);
        JPanel bubble = crearBurbuja(texto, esMio, soloHora, leido, idMensaje);

        if (esMio) {
            JPanel contenedor = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            contenedor.setOpaque(false);
            contenedor.add(bubble);
            contenedor.add(crearFotoPerfilyo());
            fila.add(contenedor, BorderLayout.EAST);
        } else {
            JPanel contenedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            contenedor.setOpaque(false);
            contenedor.add(crearFotoPerfil());
            contenedor.add(bubble);
            fila.add(contenedor, BorderLayout.WEST);
        }
        return fila;
    }

    public void agregarMensajeUI(String texto, boolean esMio, String fechaHora, boolean leido, String idMensaje) {
        JPanel mensaje = crearFilaMensaje(texto, esMio, fechaHora, leido, idMensaje);
        jPanelChat.add(mensaje);
        jPanelChat.revalidate();
        jPanelChat.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = jScrollPaneChat.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void limpiarChat() {
        jPanelChat.removeAll();
        jPanelChat.revalidate();
        jPanelChat.repaint();
    }

    private String formatearSoloHora(String fechaHora) {
        try {
            if (fechaHora.length() == 5) return fechaHora;
            java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(
                    fechaHora, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return fechaHora;
        }
    }

    public void actualizarChecksAzules(String idMensaje) {
        for (Component comp : jPanelChat.getComponents()) {
            if (comp instanceof JPanel panelFila) {
                for (Component subComp : panelFila.getComponents()) {
                    if (subComp instanceof JPanel bubble) {
                        for (Component inner : bubble.getComponents()) {
                            if (inner instanceof JPanel panelInferior) {
                                for (Component checkComp : panelInferior.getComponents()) {
                                    if (checkComp instanceof JLabel lbl && idMensaje.equals(lbl.getName())) {
                                        lbl.setText("\u2714\u2714");
                                        lbl.setForeground(Color.BLUE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        jPanelChat.revalidate();
        jPanelChat.repaint();
    }

    public void agregarContacto(Contact contact) {
        if (contact == null || contact.getId() == null) return;
        for (int i = 0; i < contacModel.size(); i++) {
            if (contacModel.get(i).getId().equals(contact.getId())) return;
        }
        contacModel.addElement(contact);
    }

    public void actualizarContacto(Contact contact) {
        for (int i = 0; i < contacModel.size(); i++) {
            Contact c = contacModel.get(i);
            if (c.getId().equals(contact.getId())) {
                c.setName(contact.getName());
                c.setIp(contact.getIp());
                contacModel.set(i, c);
                break;
            }
        }
        jContactos.repaint();
    }

    public void mostrarMensajeSistema(String texto) {
        JPanel panelSistema = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSistema.setOpaque(false);
        JLabel lblSistema = new JLabel(texto);
        lblSistema.setForeground(Color.RED);
        lblSistema.setFont(new Font("Arial", Font.BOLD, 12));
        panelSistema.add(lblSistema);
        jPanelChat.add(panelSistema);
        jPanelChat.revalidate();
        jPanelChat.repaint();
    }

    public void actualizarValores(String idUsuarioActual) {
        this.idUsuarioActual = idUsuarioActual;
    }

    public void actualizarEstadoContacto(String id, boolean estado) {
        for (int i = 0; i < contacModel.size(); i++) {
            Contact c = contacModel.get(i);
            if (c.getId().equals(id)) {
                c.setStateConnect(estado);
                contacModel.set(i, c);
                break;
            }
        }
        jContactos.repaint();
    }

    private JLabel crearFotoPerfilyo() {
        JLabel foto = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/user.png"));
            foto.setIcon(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        } catch (Exception e) { foto.setText(""); }
        return foto;
    }

    private JLabel crearFotoPerfil() {
        JLabel foto = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/people.png"));
            foto.setIcon(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        } catch (Exception e) { foto.setText(""); }
        return foto;
    }

    // ===== POPUPS =====
    private void showOffPopup() {
        JDialog dialog = new JDialog(this, true);
        dialog.setSize(320, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0,0,0,0));
        JPanel root = new JPanel(null); root.setOpaque(false);
        JPanel card = new JPanel(); card.setLayout(null); card.setBackground(Color.WHITE);
        card.setBounds(40, 90, 250, 110); card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        JLabel title = new JLabel("Su conexión fue terminada"); title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(37,29,75)); title.setHorizontalAlignment(SwingConstants.CENTER); title.setBounds(15, 20, 220, 30);
        JButton okButton = new JButton("Ok"); okButton.setBounds(100, 60, 50, 30);
        okButton.setBackground(new Color(202,203,233)); okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());
        card.add(title); card.add(okButton);
        JLabel imageLabel = new JLabel(); imageLabel.setBounds(115, 0, 110, 110);
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/robotOff.png"));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("Img"); }
        root.add(card); root.add(imageLabel); dialog.add(root); dialog.setVisible(true);
    }

    private void showAcceptPopup() {
        JDialog dialog = new JDialog(this, true); dialog.setSize(320, 300);
        dialog.setLocationRelativeTo(this); dialog.setUndecorated(true); dialog.setBackground(new Color(0,0,0,0));
        JPanel root = new JPanel(null); root.setOpaque(false);
        JPanel card = new JPanel(); card.setLayout(null); card.setBackground(Color.WHITE);
        card.setBounds(40, 90, 220, 110); card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        JLabel title = new JLabel("Conexion aceptada"); title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(37,29,75)); title.setHorizontalAlignment(SwingConstants.CENTER); title.setBounds(20, 20, 180, 30);
        JButton okButton = new JButton("Ok"); okButton.setBounds(85, 60, 50, 30);
        okButton.setBackground(new Color(202,203,233)); okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());
        card.add(title); card.add(okButton);
        JLabel imageLabel = new JLabel(); imageLabel.setBounds(85, 0, 110, 110);
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/robotAccept.png"));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("Img"); }
        root.add(card); root.add(imageLabel); dialog.add(root); dialog.setVisible(true);
    }

    private void showDeclinePopup() {
        JDialog dialog = new JDialog(this, true); dialog.setSize(320, 300);
        dialog.setLocationRelativeTo(this); dialog.setUndecorated(true); dialog.setBackground(new Color(0,0,0,0));
        JPanel root = new JPanel(null); root.setOpaque(false);
        JPanel card = new JPanel(); card.setLayout(null); card.setBackground(Color.WHITE);
        card.setBounds(40, 90, 220, 110); card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        JLabel title = new JLabel("Conexion rechazada"); title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(37,29,75)); title.setHorizontalAlignment(SwingConstants.CENTER); title.setBounds(20, 20, 180, 30);
        JButton okButton = new JButton("Ok"); okButton.setBounds(85, 60, 50, 30);
        okButton.setBackground(new Color(202,203,233)); okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());
        card.add(title); card.add(okButton);
        JLabel imageLabel = new JLabel(); imageLabel.setBounds(85, 0, 110, 110);
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/robotDecline.png"));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("Img"); }
        root.add(card); root.add(imageLabel); dialog.add(root); dialog.setVisible(true);
    }

    public void showZumbidoPopup(String nombre) {
        JDialog dialog = new JDialog(this, true); dialog.setSize(320, 300);
        dialog.setLocationRelativeTo(this); dialog.setUndecorated(true); dialog.setBackground(new Color(0,0,0,0));
        JPanel root = new JPanel(null); root.setOpaque(false);
        JPanel card = new JPanel(); card.setLayout(null); card.setBackground(Color.WHITE);
        card.setBounds(40, 90, 220, 110); card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        JLabel title = new JLabel(nombre + " te esta zumbando"); title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(37,29,75)); title.setHorizontalAlignment(SwingConstants.CENTER); title.setBounds(20, 20, 180, 30);
        JButton okButton = new JButton("Ok"); okButton.setBounds(85, 60, 50, 30);
        okButton.setBackground(new Color(202,203,233)); okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());
        card.add(title); card.add(okButton);
        JLabel imageLabel = new JLabel(); imageLabel.setBounds(85, 0, 110, 110);
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/robotAccept.png"));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("Img"); }
        root.add(card); root.add(imageLabel); dialog.add(root); dialog.setVisible(true);
    }

    public boolean showInvitationPopup(String nombreContacto) {
        final boolean[] accepted = {false};
        JDialog dialog = new JDialog(this, true); dialog.setSize(350, 320);
        dialog.setLocationRelativeTo(this); dialog.setUndecorated(true); dialog.setBackground(new Color(0,0,0,0));
        JPanel root = new JPanel(null); root.setOpaque(false);
        JPanel card = new JPanel(); card.setLayout(null); card.setBackground(Color.WHITE);
        card.setBounds(40, 100, 270, 150); card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        JLabel title = new JLabel("Llegó la invitación: " + nombreContacto); title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(37,29,75)); title.setHorizontalAlignment(SwingConstants.CENTER); title.setBounds(20, 20, 230, 40);
        JButton acceptButton = new JButton("Aceptar"); acceptButton.setBounds(30, 80, 90, 35);
        acceptButton.setBackground(Color.lightGray); acceptButton.setFocusPainted(false);
        acceptButton.addActionListener(e -> { accepted[0] = true; dialog.dispose(); });
        JButton declineButton = new JButton("Rechazar"); declineButton.setBounds(150, 80, 90, 35);
        declineButton.setBackground(Color.GRAY); declineButton.setFocusPainted(false);
        declineButton.addActionListener(e -> { accepted[0] = false; dialog.dispose(); });
        card.add(title); card.add(acceptButton); card.add(declineButton);
        JLabel imageLabel = new JLabel(); imageLabel.setBounds(120, 0, 110, 110);
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/robotRequest.png"));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("Img"); }
        root.add(card); root.add(imageLabel); dialog.add(root); dialog.setVisible(true);
        return accepted[0];
    }

    private void showNuevaConexionPopup() {
        JDialog dialog = new JDialog(this, true); dialog.setSize(420, 450);
        dialog.setLocationRelativeTo(this); dialog.setUndecorated(true); dialog.setBackground(new Color(0,0,0,0));
        JPanel root = new JPanel(null); root.setOpaque(false);
        JPanel card = new JPanel(); card.setLayout(null); card.setBackground(Color.WHITE);
        card.setBounds(35, 80, 350, 300); card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        JButton btnCerrar = new JButton();
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/x1.png"));
            btnCerrar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH)));
        } catch (Exception e) { btnCerrar.setText("X"); }
        btnCerrar.setBounds(10, 10, 25, 25); btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false); btnCerrar.setFocusPainted(false); btnCerrar.setOpaque(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR)); btnCerrar.addActionListener(e -> dialog.dispose());
        JLabel imageLabel = new JLabel(); imageLabel.setHorizontalAlignment(SwingConstants.CENTER); imageLabel.setBounds(155, 20, 110, 110);
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/robotAdd.png"));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("Imagen no encontrada"); }
        JLabel title = new JLabel("Add Contact"); title.setFont(new Font("Arial", Font.BOLD, 18)); title.setBounds(110, 70, 200, 30);
        JLabel ipLabel = new JLabel("Contact IP:"); ipLabel.setFont(new Font("Arial", Font.PLAIN, 13)); ipLabel.setBounds(30, 170, 200, 20);
        jtIp = new JTextField(); jtIp.setBounds(30, 190, 290, 30);
        jBtnConectar = new JButton("CONECTAR"); jBtnConectar.setBounds(75, 235, 200, 35);
        jBtnConectar.setBackground(Color.GRAY); jBtnConectar.setForeground(Color.BLACK); jBtnConectar.setFocusPainted(false);
        jBtnConectar.addActionListener(evt -> { Mediador.getInstance().establecerConexion(jtIp.getText()); dialog.dispose(); });
        card.add(title); card.add(ipLabel); card.add(jtIp); card.add(jBtnConectar); card.add(btnCerrar);
        root.add(imageLabel); root.add(card); dialog.add(root); dialog.setVisible(true);
    }

    public void showNombrePopup() {
        JDialog dialog = new JDialog(this, true); dialog.setSize(420, 450);
        dialog.setLocationRelativeTo(this); dialog.setUndecorated(true); dialog.setBackground(new Color(0,0,0,0));
        JPanel root = new JPanel(null); root.setOpaque(false);
        JPanel card = new JPanel(); card.setLayout(null); card.setBackground(Color.WHITE);
        card.setBounds(35, 80, 350, 300); card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        JLabel imageLabel = new JLabel(); imageLabel.setHorizontalAlignment(SwingConstants.CENTER); imageLabel.setBounds(155, 20, 110, 110);
        try { ImageIcon icon = new ImageIcon(getClass().getResource("/images/robotAdd.png"));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("Imagen no encontrada"); }
        JLabel title = new JLabel("Ingrese su nombre"); title.setFont(new Font("Arial", Font.BOLD, 18)); title.setBounds(110, 70, 200, 30);
        JLabel ipLabel = new JLabel("Nombre:"); ipLabel.setFont(new Font("Arial", Font.PLAIN, 13)); ipLabel.setBounds(30, 170, 200, 20);
        jtName = new JTextField(); jtName.setBounds(30, 190, 290, 30);
        jBtnAceptar = new JButton("ACEPTAR"); jBtnAceptar.setBounds(75, 235, 200, 35);
        jBtnAceptar.setBackground(Color.GRAY); jBtnAceptar.setForeground(Color.BLACK); jBtnAceptar.setFocusPainted(false);
        jBtnAceptar.addActionListener(evt -> { Mediador.getInstance().primerRegistro(jtName.getText()); dialog.dispose(); });
        card.add(title); card.add(ipLabel); card.add(jtName); card.add(jBtnAceptar);
        root.add(imageLabel); root.add(card); dialog.add(root); dialog.setVisible(true);
    }

    // ===== VARIABLES =====
    private JButton jBtnEnviar;
    private JButton jBtnFoto;
    private JTextField jtMensaje;
    private JPanel jPanelChat;
    private JScrollPane jScrollPaneChat;
    private JButton jBtnNuevaConexion;
    private JButton jBtnOff;
    private JList<Contact> jContactos;
    private JButton jBtnConectar;
    private JButton jBtnAceptar;
    private JTextField jtIp;
    private JTextField jtName;

    public void onMessage(Message message) {
        if (message instanceof Aceptar) {
            System.out.println("Entra en 002 Aceptar");
            showAcceptPopup();
        }
        if (message instanceof Rechazar) {
            System.out.println("Entra en 003 Solicitud Rechazada");
            showDeclinePopup();
        }
        if (message instanceof ImageMessage) {
            ImageMessage img = (ImageMessage) message;
            boolean esMio = img.getIdEmisor().equals(Mediador.getInstance().getIdMio());
            if (img.getIdEmisor().equals(idUsuarioActual) || esMio) {
                agregarImagenUI(img.getImage(), esMio, img.getHora(), img.getIdMensaje());
            }
        }
        if (message instanceof Mensaje) {
            Mensaje mensaje = (Mensaje) message;
            boolean esMio = mensaje.getIdEmisor().equals(Mediador.getInstance().getIdMio());
            if (mensaje.getIdEmisor().equals(idUsuarioActual) || esMio) {
                agregarMensajeUI(mensaje.getMensaje(), esMio, mensaje.getHora(), false, mensaje.getIdMensaje());
            }
            if (mensaje.getIdEmisor().equals(idUsuarioActual)) {
                ConfirmarRecibido confirmarRecibido = new ConfirmarRecibido(mensaje.getIdMensaje());
                Mediador.getInstance().sendMessage(idUsuarioActual, confirmarRecibido);
                Mediador.getInstance().actualizarLeido(mensaje.getIdMensaje());
            }
        }
        if (message instanceof Hello) {
            Hello hello = (Hello) message;
            System.out.println("Se recibio el Hello de: " + hello.getIdUsuario());
        }
        if (message instanceof AceptarHello) {
            System.out.println("Hello Aceptado");
        }
        if (message instanceof RechazarHello) {
            System.out.println("Hello Rechazado");
        }
        if (message instanceof ConfirmarRecibido) {
            actualizarChecksAzules(((ConfirmarRecibido) message).getIdMensaje());
        }
        if (message instanceof FueraLinea) {
            System.out.println("Conexión terminada por cliente");
            showOffPopup();
            mostrarMensajeSistema("CONEXION TERMINADA");
            actualizarEstadoContacto(idUsuarioActual, false);
        }
    }

    @Override
    public void onloadContacts(List<Contact> contacts) {
        try {
            if (contacts.isEmpty()) System.out.println("BD vacia");
            contacModel.addAll(contacts);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onloadMessages(List<Mensaje> mensajes) {
        limpiarChat();
        for (Mensaje c : mensajes) {
            boolean esMio = c.getIdEmisor().equals(Mediador.getInstance().getIdMio());
            boolean leido = c.getLeido().equals("1");
            // agregarMensajeUI maneja tanto texto como "IMG:" automáticamente via crearBurbuja
            agregarMensajeUI(c.getMensaje(), esMio, c.getHora(), leido, c.getIdMensaje());
            if (!esMio && !leido) {
                System.out.println("Enviando 008...");
                ConfirmarRecibido confirmarRecibido = new ConfirmarRecibido(c.getIdMensaje());
                Mediador.getInstance().sendMessage(c.getIdEmisor(), confirmarRecibido);
                Mediador.getInstance().actualizarLeido(c.getIdMensaje());
            }
        }
    }
}
