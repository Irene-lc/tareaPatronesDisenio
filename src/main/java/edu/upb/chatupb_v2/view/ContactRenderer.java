/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.view;

import edu.upb.chatupb_v2.model.entities.message.Contact;

import java.awt.*;
import javax.swing.*;

public class ContactRenderer extends JLabel implements ListCellRenderer<Contact> {

    protected static final Font SELECTED_FONT = new Font("Comic Sans MS", Font.PLAIN, 12);
    private int hoverIndex = -1;

    public void setHoverIndex(int index) {
        this.hoverIndex = index;
    }
    static class PuntoRedondo extends JComponent {

        private boolean encendido = false;

        public PuntoRedondo(int size) {
            setPreferredSize(new Dimension(size, size));
        }

        public void setEncendido(boolean encendido) {
            this.encendido = encendido;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!encendido)
                return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 100, 0));
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
    static class Campana extends JLabel {
        public Campana() {
            setText("🔔");
            setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
            setVisible(false);
        }
        public void setEncendida(boolean encendida) {
            setVisible(encendida);
        }
    }

    private PuntoRedondo punto = new PuntoRedondo(10);
    private Campana campana = new Campana();

    public ContactRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 8));
        derecha.setOpaque(false);
        derecha.add(campana);
        derecha.add(punto);

        add(derecha, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contact> list, Contact contac, int index, boolean isSelected, boolean cellHasFocus) {
        ImageIcon imageIcon;
        if (contac.isStateConnect()) {
            imageIcon = new ImageIcon(getClass().getResource("/images/on-line.png"));
        } else {
            imageIcon = new ImageIcon(getClass().getResource("/images/off-line.png"));
        }

        Image imgScaled = imageIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(imgScaled);
        setIcon(scaledIcon);

        setText( "<html><p>"+ contac.getName()+"</p></html>");

        campana.setEncendida(contac.isZumbido());
        punto.setEncendido(contac.isMensajesNoLeidos());

        if (isSelected) {
            setBackground(new Color(200, 200, 200));
            setFont(SELECTED_FONT);
        } else if (index == hoverIndex) {
            setBackground(new Color(220, 220, 220));
            setFont(UIManager.getFont("Label.font"));
        } else {
            setBackground(Color.WHITE);
            setFont(UIManager.getFont("Label.font"));
        }

        setOpaque(true);
        return this;
    }
}

