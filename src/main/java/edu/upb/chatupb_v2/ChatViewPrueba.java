package edu.upb.chatupb_v2;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChatViewPrueba extends JFrame {

    private JTextPane txtChat;
    private JTextField jtMensaje;
    private JButton btnEnviar;

    public ChatViewPrueba() {
        initComponents();
    }

    private void initComponents() {

        setTitle("Chat");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        txtChat = new JTextPane();
        txtChat.setEditable(false);
        txtChat.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(txtChat);
        add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        jtMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");

        bottomPanel.add(jtMensaje, BorderLayout.CENTER);
        bottomPanel.add(btnEnviar, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        btnEnviar.addActionListener(this::enviarMensaje);

        setVisible(true);
    }

    private void enviarMensaje(ActionEvent e) {

        String mensaje = jtMensaje.getText().trim();

        if (!mensaje.isEmpty()) {
            agregarMensaje("Yo: " + mensaje, true);
            jtMensaje.setText("");

            // 🔵 AQUI LLAMAS TU LOGICA YA IMPLEMENTADA
            // socketClient.enviarMensaje(mensaje);
        }
    }

    public void agregarMensaje(String mensaje, boolean esMio) {
        StyledDocument doc = txtChat.getStyledDocument();

        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align,
                esMio ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);

        try {
            doc.insertString(doc.getLength(), mensaje + "\n\n", null);
            doc.setParagraphAttributes(doc.getLength(), 1, align, false);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatViewPrueba().setVisible(true);
            }
        });
    }
}