/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.view;

import edu.upb.chatupb_v2.model.repository.Contact;

import java.awt.*;
import javax.swing.*;

public class ContactRenderer extends JLabel implements ListCellRenderer<Contact> {

    protected static final Font SELECTED_FONT = new Font("Comic Sans MS", Font.PLAIN, 12);

    @Override
    public Component getListCellRendererComponent(JList<? extends Contact> list, Contact contac, int index, boolean isSelected, boolean cellHasFocus) {
        ImageIcon imageIcon;
        if (contac.getStateConnect().equals("1")) {
            imageIcon = new ImageIcon(getClass().getResource("/images/on-line.png"));
        } else {
            imageIcon = new ImageIcon(getClass().getResource("/images/off-line.png"));
        }

        Image imgScaled = imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(imgScaled);
        setIcon(scaledIcon);

        setText( "<html><p>"+ contac.getName()+"</p></html>");

        if (isSelected) {
            setBackground(Color.BLUE);
            setFont(SELECTED_FONT);
        } else {
            setFont(UIManager.getFont("Label.font"));
            setBackground(Color.WHITE);
        }
        return this;
    }
}

//public class ContactRenderer extends JLabel implements ListCellRenderer<Contact> {
//
//    @Override
//    public Component getListCellRendererComponent(
//            JList<? extends Contact> list,
//            Contact contact,
//            int index,
//            boolean isSelected,
//            boolean cellHasFocus) {
//
//        setText("<html><b>" + contact.getNombreCliente() + "</b><br/>" +
//                "<small>" + contact.getIp() + "</small></html>");
//
//        if (isSelected) {
//            setBackground(new Color(220, 220, 255));
//            setOpaque(true);
//        } else {
//            setBackground(Color.WHITE);
//            setOpaque(true);
//        }
//
//        return this;
//    }
//}