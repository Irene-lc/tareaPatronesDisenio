package edu.upb.chatupb_v2.view;

import edu.upb.chatupb_v2.model.entities.message.Contact;

import java.awt.*;
import javax.swing.*;

public class ContactRenderer extends JPanel implements ListCellRenderer<Contact> {

    private static final Color BG_NORMAL   = new Color(18, 24, 42);
    private static final Color BG_HOVER    = new Color(28, 36, 60);
    private static final Color BG_SELECTED = new Color(79, 70, 229, 55);
    private static final Color TEXT_NAME   = new Color(230, 235, 255);
    private static final Color TEXT_MUTED  = new Color(148, 163, 184);
    private static final Color ACCENT      = new Color(129, 140, 248);
    private static final Color ONLINE_DOT  = new Color(52, 211, 153);
    private static final Color OFFLINE_DOT = new Color(100, 116, 139);

    private int hoverIndex = -1;

    public void setHoverIndex(int index) {
        this.hoverIndex = index;
    }

    // Avatar circular con inicial
    static class AvatarLabel extends JComponent {
        private String initial = "?";
        private Color color = new Color(99, 102, 241);
        private boolean online = false;

        public AvatarLabel() {
            setPreferredSize(new Dimension(38, 38));
        }

        public void setInitial(String initial) { this.initial = initial; }
        public void setColor(Color c) { this.color = c; }
        public void setOnline(boolean online) { this.online = online; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Círculo del avatar
            g2.setColor(color);
            g2.fillOval(1, 1, 34, 34);

            // Inicial
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Georgia", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (36 - fm.stringWidth(initial)) / 2;
            int ty = (36 + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(initial, tx, ty);

            // Indicador de estado (abajo a la derecha)
            g2.setColor(online ? ONLINE_DOT : OFFLINE_DOT);
            g2.fillOval(25, 25, 10, 10);
            g2.setColor(new Color(18, 24, 42)); // border
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(25, 25, 10, 10);

            g2.dispose();
        }
    }

    static class NotifDot extends JComponent {
        private boolean visible2 = false;

        public NotifDot() {
            setPreferredSize(new Dimension(10, 10));
        }

        public void setShowing(boolean show) {
            this.visible2 = show;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (!visible2) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setColor(new Color(129, 140, 248));
            g2.setColor(new Color(20, 184, 166));
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private final AvatarLabel avatar = new AvatarLabel();
    private final JLabel lblName = new JLabel();
    private final JLabel lblBell = new JLabel("🔔");
    private final NotifDot notifDot = new NotifDot();

    // Paleta de colores para avatares
    private static final Color[] AVATAR_COLORS = {
            new Color(99, 102, 241),
            new Color(168, 85, 247),
            new Color(236, 72, 153),
            new Color(20, 184, 166),
            new Color(245, 158, 11),
            new Color(59, 130, 246),
    };

    public ContactRenderer() {
        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setOpaque(true);

        // Panel izquierdo: avatar
        add(avatar, BorderLayout.WEST);

        // Panel central: nombre
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblName.setVerticalAlignment(SwingConstants.CENTER);
        lblName.setForeground(TEXT_NAME);
        add(lblName, BorderLayout.CENTER);

        // Panel derecho: notificaciones
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 10));
        rightPanel.setOpaque(false);
        lblBell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblBell.setVisible(false);
        rightPanel.add(lblBell);
        rightPanel.add(notifDot);
        add(rightPanel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Contact> list,
            Contact contact,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        // Avatar: initial + color determinístico por nombre
        String name = contact.getName() != null && !contact.getName().isEmpty()
                ? contact.getName() : "?";
        String initial = String.valueOf(name.charAt(0)).toUpperCase();
        Color avatarColor = AVATAR_COLORS[Math.abs(name.hashCode()) % AVATAR_COLORS.length];
        avatar.setInitial(initial);
        avatar.setColor(avatarColor);
        avatar.setOnline(contact.isStateConnect());

        // Nombre
        lblName.setText(name);
        lblName.setForeground(isSelected ? new Color(224, 231, 255) : TEXT_NAME);

        // Notificaciones
        lblBell.setVisible(contact.isZumbido());
        notifDot.setShowing(contact.isMensajesNoLeidos());

        // Fondo
        if (isSelected) {
            setBackground(BG_SELECTED);
        } else if (index == hoverIndex) {
            setBackground(BG_HOVER);
        } else {
            setBackground(BG_NORMAL);
        }

        return this;
    }
}
