package edu.upb.chatupb_v2.view;

import javax.swing.*;
import java.awt.*;

public class ChatUIStyles {

    private Color BG_DARK;
    private Color BG_PANEL;
    private Color BG_SIDEBAR;
    private Color BG_INPUT;
    private Color BG_BUBBLE_ME;
    private Color BG_BUBBLE_YOU;
    private Color ACCENT;
    private Color ACCENT_HOVER;
    private Color TEXT_PRIMARY;
    private Color TEXT_MUTED;
    private Color DIVIDER;
    private Color BTN_SEND;
    private Color BTN_SEND_HOV;
    private Color SYSTEM_RED;
    private Color BTN_UNICO_ACTIVE;

    public ChatUIStyles() {
        loadThemeColors();
    }

    public void loadThemeColors() {
        ThemeManager.ThemeColors colors = ThemeManager.getColors();
        BG_DARK = colors.BG_DARK;
        BG_PANEL = colors.BG_PANEL;
        BG_SIDEBAR = colors.BG_SIDEBAR;
        BG_INPUT = colors.BG_INPUT;
        BG_BUBBLE_ME = colors.BG_BUBBLE_ME;
        BG_BUBBLE_YOU = colors.BG_BUBBLE_YOU;
        ACCENT = colors.ACCENT;
        ACCENT_HOVER = colors.ACCENT_HOVER;
        TEXT_PRIMARY = colors.TEXT_PRIMARY;
        TEXT_MUTED = colors.TEXT_MUTED;
        DIVIDER = colors.DIVIDER;
        BTN_SEND = colors.BTN_SEND;
        BTN_SEND_HOV = colors.BTN_SEND_HOVER;
        SYSTEM_RED = colors.SYSTEM_RED;
        BTN_UNICO_ACTIVE = colors.BTN_UNIQUE_ACTIVE;
    }

    public Color getBgDark() { return BG_DARK; }
    public Color getBgPanel() { return BG_PANEL; }
    public Color getBgSidebar() { return BG_SIDEBAR; }
    public Color getBgInput() { return BG_INPUT; }
    public Color getBgBubbleMe() { return BG_BUBBLE_ME; }
    public Color getBgBubbleYou() { return BG_BUBBLE_YOU; }
    public Color getAccent() { return ACCENT; }
    public Color getAccentHover() { return ACCENT_HOVER; }
    public Color getTextPrimary() { return TEXT_PRIMARY; }
    public Color getTextMuted() { return TEXT_MUTED; }
    public Color getDivider() { return DIVIDER; }
    public Color getBtnSend() { return BTN_SEND; }
    public Color getBtnSendHov() { return BTN_SEND_HOV; }
    public Color getSystemRed() { return SYSTEM_RED; }
    public Color getBtnUnicoActive() { return BTN_UNICO_ACTIVE; }

    public JButton createSendButton() {
        JButton btn = new JButton("➤") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? BTN_SEND_HOV : BTN_SEND;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btn.setForeground(Color.WHITE);
        btn.setMargin(new Insets(8, 20, 8, 20));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JButton createNuevaConexionButton(String text, String tooltip) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                        ? new Color(79, 70, 229, 80)
                        : new Color(79, 70, 229, 35);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(ACCENT);
        btn.setMargin(new Insets(4, 10, 4, 10));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        return btn;
    }

    public JButton createOffButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                        ? new Color(239, 68, 68, 90)
                        : new Color(239, 68, 68, 35);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        btn.setForeground(new Color(248, 113, 113));
        btn.setMargin(new Insets(4, 10, 4, 10));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Desconectarse");
        return btn;
    }

    public JButton createThemeButton() {
        JButton btn = new JButton("TEMA") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                        ? new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 80)
                        : new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 35);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btn.setForeground(ACCENT);
        btn.setMargin(new Insets(4, 10, 4, 10));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Cambiar tema");
        return btn;
    }

    public JButton createMensajeUnicoButton() {
        JButton btn = new JButton("⏱") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                        ? new Color(79, 70, 229, 80)
                        : new Color(79, 70, 229, 35);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        btn.setForeground(ACCENT);
        btn.setMargin(new Insets(4, 10, 4, 10));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Mensaje único (se ve solo una vez)");
        return btn;
    }

    public JButton createPopupOkButton(String text, int x, int y, int w, int h) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? BTN_SEND_HOV : BTN_SEND;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBounds(x, y, w, h);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JButton createDeclineButton(String text, int x, int y, int w, int h) {
        JButton decline = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(239, 68, 68, 80) : new Color(239, 68, 68, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        decline.setBounds(x, y, w, h);
        decline.setForeground(SYSTEM_RED);
        decline.setFont(new Font("Tahoma", Font.BOLD, 13));
        decline.setContentAreaFilled(false);
        decline.setBorderPainted(false);
        decline.setFocusPainted(false);
        decline.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return decline;
    }

    public JTextField createDarkTextField(int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT);
        tf.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return tf;
    }

    public JTextField createSearchField() {
        JTextField jtBuscadorContactos = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TEXT_MUTED);
                    g2.setFont(new Font("Tahoma", Font.PLAIN, 13));
                    g2.drawString("Buscar contactos...", 12, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        jtBuscadorContactos.setBackground(BG_INPUT);
        jtBuscadorContactos.setForeground(TEXT_PRIMARY);
        jtBuscadorContactos.setCaretColor(ACCENT);
        jtBuscadorContactos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 40), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        jtBuscadorContactos.setFont(new Font("Impact", Font.PLAIN, 13));
        return jtBuscadorContactos;
    }

    public JTextField createMessageField() {
        JTextField jtMensaje = new JTextField();
        jtMensaje.setBackground(BG_INPUT);
        jtMensaje.setForeground(TEXT_PRIMARY);
        jtMensaje.setCaretColor(ACCENT);
        jtMensaje.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        jtMensaje.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jtMensaje.putClientProperty("JTextField.placeholderText", "Escribe un mensaje...");
        return jtMensaje;
    }

    public void styleScrollBar(JScrollPane sp) {
        JScrollBar vsb = sp.getVerticalScrollBar();
        final Color bgSidebar = BG_SIDEBAR;
        final Color accentColor = ACCENT;

        vsb.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                trackColor = new Color(0, 0, 0, 0);
            }
            @Override
            protected JButton createDecreaseButton(int o) { return zeroButton(); }
            @Override
            protected JButton createIncreaseButton(int o) { return zeroButton(); }
            private JButton zeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(bgSidebar);
                g2.fillRect(r.x, r.y, r.width, r.height);
                g2.dispose();
            }
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                if (r.isEmpty() || !sp.getVerticalScrollBar().isVisible()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color thumbColor = new Color(
                        accentColor.getRed(),
                        accentColor.getGreen(),
                        accentColor.getBlue(),
                        isThumbRollover() ? 140 : 80
                );
                g2.setColor(thumbColor);
                int padding = 2;
                int width = r.width - (padding * 2);
                g2.fillRoundRect(r.x + padding, r.y + padding, width, r.height - (padding * 2), 10, 10);
                g2.dispose();
            }
        });
        vsb.setPreferredSize(new Dimension(8, 0));
        vsb.setOpaque(false);
    }

    public void stylePopupMenu(JPopupMenu menu) {
        menu.setBackground(new Color(26, 32, 54));
        menu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(79, 70, 229, 80), 1),
                BorderFactory.createEmptyBorder(4, 0, 4, 0)
        ));
    }

    public JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(new Color(26, 32, 54));
        item.setForeground(TEXT_PRIMARY);
        item.setFont(new Font("Tahoma", Font.PLAIN, 13));
        item.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        item.setOpaque(true);
        return item;
    }

    public JPanel buildSidebarDivider() {
        JPanel d = new JPanel();
        d.setPreferredSize(new Dimension(0, 1));
        d.setBackground(new Color(255, 255, 255, 15));
        return d;
    }

    public JPanel createDarkCard(int x, int y, int w, int h) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(79, 70, 229, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setLayout(null);
        card.setOpaque(false);
        card.setBounds(x, y, w, h);
        return card;
    }

    public JDialog createBaseDialog(JFrame parent, int w, int h) {
        JDialog dialog = new JDialog(parent, true);
        dialog.setSize(w, h);
        dialog.setLocationRelativeTo(parent);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        return dialog;
    }
    public JLabel createRobotIcon(int x, int y, String imagePath, Class<?> resourceClass) {
        JLabel lbl = new JLabel();
        lbl.setBounds(x, y, 90, 120);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(resourceClass.getResource(imagePath));
            Image img = icon.getImage().getScaledInstance(90, -1, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lbl.setText("🤖");
            lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        }
        return lbl;
    }

    public JButton createCloseButton(Class<?> resourceClass) {
        JButton btnCerrar = new JButton();
        try {
            ImageIcon icon = new ImageIcon(resourceClass.getResource("/images/x1.png"));
            Image img = icon.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH);
            btnCerrar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btnCerrar.setText("X");
        }
        btnCerrar.setForeground(TEXT_MUTED);
        btnCerrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btnCerrar;
    }

    public JPanel crearBurbuja(String texto, boolean esMio, String hora, boolean leido, String idMensaje) {
        int maxBubbleWidth = 320;
        int htmlWidth = maxBubbleWidth - 56;
        String colorHex = esMio ? "#e0e7ff" : "#cbd5e1";

        JLabel lblTexto;
        if (texto.equals("Eliminaste este mensaje") || texto.equals("Se elimino este mensaje")) {
            lblTexto = new JLabel(
                    "<html><body style='max-width:" + htmlWidth + "px; color:#6b7280; font-style:italic; font-family:Tahoma;'>" + texto + "</body></html>"
            );
        } else {
            if (texto.length() > htmlWidth) {
                lblTexto = new JLabel(
                        "<html><body style='width:" + htmlWidth + "px; color:" + colorHex + "; font-family:Tahoma;'>" + texto + "</body></html>"
                );
            } else {
                lblTexto = new JLabel(
                        "<html><body style='max-width:" + htmlWidth + "px; color:" + colorHex + "; font-family:Tahoma;'>" + texto + "</body></html>"
                );
            }
        }
        lblTexto.setName(idMensaje);
        lblTexto.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHora = new JLabel(hora);
        lblHora.setFont(new Font("Tahoma", Font.PLAIN, 9));
        lblHora.setForeground(esMio ? new Color(196, 181, 253, 180) : new Color(148, 163, 184, 180));

        JPanel contenidoInterno = new JPanel();
        contenidoInterno.setLayout(new BoxLayout(contenidoInterno, BoxLayout.Y_AXIS));
        contenidoInterno.setOpaque(false);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0)) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int w = contenidoInterno.getWidth() > 0 ? contenidoInterno.getWidth() : Math.max(d.width, 95);
                return new Dimension(w, d.height);
            }
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        panelInferior.setOpaque(false);
        panelInferior.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInferior.add(lblHora);

        contenidoInterno.add(lblTexto);
        contenidoInterno.add(panelInferior);

        final Color bubbleBgMe = BG_BUBBLE_ME;
        final Color bubbleBgYou = BG_BUBBLE_YOU;

        JPanel bubble = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
            }
            @Override
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(maxBubbleWidth, pref.height);
            }
            @Override
            public Dimension getMinimumSize() {
                return new Dimension(110, super.getMinimumSize().height);
            }
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(Math.max(d.width, 110), d.height);
            }
        };
        bubble.setOpaque(false);
        bubble.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        bubble.setBackground(esMio ? bubbleBgMe : bubbleBgYou);
        bubble.add(contenidoInterno, BorderLayout.CENTER);

        if (esMio) {
            JLabel lblCheck = new JLabel(leido ? "✔✔" : "✔");
            lblCheck.setName(idMensaje);
            lblCheck.setFont(new Font("Tahoma", Font.PLAIN, 9));
            lblCheck.setForeground(leido ? new Color(167, 243, 208) : new Color(148, 163, 184));
            panelInferior.add(lblCheck);
        }

        return bubble;
    }

    public JPanel crearBurbujaUnica(boolean esMio, String hora, boolean leido, String idMensaje, boolean mensajeYaLeido) {
        int maxBubbleWidth = 320;
        int htmlWidth = maxBubbleWidth - 56;
        Color bgBubble = new Color(120, 90, 40);
        Color borderColor = new Color(251, 191, 36, 150);

        JLabel lblIcono = new JLabel("⏱ ");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblIcono.setForeground(new Color(251, 191, 36));

        JLabel lblTexto = new JLabel(
                "<html><body style='max-width:" + htmlWidth + "px; color:#fef3c7; font-family:Tahoma;'>Mensaje</body></html>"
        );
        if (mensajeYaLeido) {
            lblTexto.setText("<html><body style='color:#6b7280; font-family:Tahoma; font-style:italic;'>Mensaje visto</body></html>");
            lblIcono.setForeground(new Color(107, 114, 128));
        }
        lblTexto.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHora = new JLabel(hora);
        lblHora.setFont(new Font("Tahoma", Font.PLAIN, 9));
        lblHora.setForeground(new Color(251, 191, 36, 180));

        JLabel lblUnico = new JLabel("mensaje único");
        lblUnico.setFont(new Font("Tahoma", Font.ITALIC, 9));
        lblUnico.setForeground(new Color(251, 191, 36, 150));

        JPanel contenidoInterno = new JPanel();
        contenidoInterno.setLayout(new BoxLayout(contenidoInterno, BoxLayout.Y_AXIS));
        contenidoInterno.setOpaque(false);

        JPanel panelTexto = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTexto.setOpaque(false);
        panelTexto.add(lblIcono);
        panelTexto.add(lblTexto);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelInferior.setOpaque(false);
        panelInferior.add(lblUnico);
        panelInferior.add(lblHora);

        contenidoInterno.add(panelTexto);
        contenidoInterno.add(panelInferior);

        if (esMio) {
            JLabel lblCheck = new JLabel(leido ? "✔✔" : "✔");
            lblCheck.setName(idMensaje);
            lblCheck.setFont(new Font("Tahoma", Font.PLAIN, 9));
            lblCheck.setForeground(leido ? new Color(167, 243, 208) : new Color(148, 163, 184));
            panelInferior.add(lblCheck);
        }

        JPanel bubble = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgBubble);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
                g2.dispose();
            }
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(maxBubbleWidth, getPreferredSize().height);
            }
            @Override
            public Dimension getMinimumSize() {
                return new Dimension(130, super.getMinimumSize().height);
            }
        };
        bubble.setOpaque(false);
        bubble.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        bubble.add(contenidoInterno, BorderLayout.CENTER);

        return bubble;
    }

    public JButton crearBotonMensajeUnico(String hora, boolean esMio, boolean leido, String idMensaje, boolean mensajeYaLeido) {
        int maxBubbleWidth = 320;
        int htmlWidth = maxBubbleWidth - 56;
        Color bgNormal = new Color(120, 90, 40);
        Color bgHover = new Color(140, 110, 50);
        Color borderColor = new Color(251, 191, 36, 150);

        JLabel lblIcono = new JLabel("⏱ ");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblIcono.setForeground(new Color(251, 191, 36));

        JLabel lblTexto = new JLabel(
                "<html><body style='max-width:" + htmlWidth + "px; color:#fef3c7; font-family:Tahoma;'>Mensaje</body></html>"
        );
        lblTexto.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblUnico = new JLabel("mensaje único");
        lblUnico.setFont(new Font("Tahoma", Font.ITALIC, 9));
        lblUnico.setForeground(new Color(251, 191, 36, 150));

        JLabel lblHora = new JLabel(hora);
        lblHora.setFont(new Font("Tahoma", Font.PLAIN, 9));
        lblHora.setForeground(new Color(251, 191, 36, 180));

        JLabel lblCheck = new JLabel(esMio ? (leido ? "✔✔" : "✔") : "");
        lblCheck.setName(idMensaje);
        lblCheck.setFont(new Font("Tahoma", Font.PLAIN, 9));
        lblCheck.setForeground(leido ? new Color(167, 243, 208) : new Color(148, 163, 184));

        JPanel panelTexto = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTexto.setOpaque(false);
        panelTexto.add(lblIcono);
        panelTexto.add(lblTexto);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelInferior.setOpaque(false);
        panelInferior.add(lblUnico);
        panelInferior.add(lblHora);
        if (esMio) {
            panelInferior.add(lblCheck);
        }

        JPanel contenidoInterno = new JPanel();
        contenidoInterno.setLayout(new BoxLayout(contenidoInterno, BoxLayout.Y_AXIS));
        contenidoInterno.setOpaque(false);
        contenidoInterno.add(panelTexto);
        contenidoInterno.add(panelInferior);

        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? bgHover : bgNormal;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
                g2.dispose();
            }
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(maxBubbleWidth, getPreferredSize().height);
            }
            @Override
            public Dimension getMinimumSize() {
                return new Dimension(130, super.getMinimumSize().height);
            }
        };
        btn.setLayout(new BorderLayout());
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Clic para ver el mensaje único");
        btn.add(contenidoInterno, BorderLayout.CENTER);

        // Si ya fue leído, deshabilitar
        if (mensajeYaLeido) {
            btn.setEnabled(false);
            btn.setToolTipText("El mensaje ya fue leído");
            btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            lblTexto.setText("<html><body style='color:#6b7280; font-family:Tahoma; font-style:italic;'>Mensaje visto</body></html>");
            lblTexto.setForeground(new Color(148, 163, 184));
            lblIcono.setForeground(new Color(107, 114, 128));
        }

        return btn;
    }

    public JComponent crearAvatarCircular(String initial, Color bgColor) {
        JComponent avatar = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillOval(0, 0, 30, 30);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Georgia", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (30 - fm.stringWidth(initial)) / 2;
                int ty = (30 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initial, tx, ty);
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() { return new Dimension(30, 30); }
            @Override
            public Dimension getMinimumSize() { return new Dimension(30, 30); }
            @Override
            public Dimension getMaximumSize() { return new Dimension(30, 30); }
        };
        return avatar;
    }

    public JComponent crearFotoPerfilYo(String miNombre) {
        if (miNombre == null || miNombre.isEmpty()) {
            miNombre = "Y";
        }
        String initial = String.valueOf(miNombre.charAt(0)).toUpperCase();
        return crearAvatarCircular(initial, ACCENT_HOVER);
    }

    public JComponent crearFotoPerfil(String nombreContacto) {
        if (nombreContacto == null || nombreContacto.isEmpty() || nombreContacto.equals("ChatUPB")) {
            nombreContacto = "?";
        }
        String initial = String.valueOf(nombreContacto.charAt(0)).toUpperCase();
        Color[] avatarColors = ThemeManager.getColors().AVATAR_COLORS;
        Color avatarColor = avatarColors[Math.abs(nombreContacto.hashCode()) % avatarColors.length];
        return crearAvatarCircular(initial, avatarColor);
    }
}
