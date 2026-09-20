/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.focusdeck;

/**
 *
 * @author Areeba Malik
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

public class AddTaskPanel extends JPanel {

    private static final Color BG_FIELD = new Color(42, 37, 62);
    private static final Color BORDER = new Color(90, 80, 130);
    private static final Color TEXT_PRIMARY = new Color(248, 248, 252);
    private static final Color TEXT_SECONDARY = new Color(185, 180, 210);
    private static final Color ACCENT_PURPLE = new Color(168, 120, 255);

    private final JTextField titleField = new JTextField(16);
    private final JTextField noteField = new JTextField(20);
    private BiConsumer<String, String> onAdd;

    public AddTaskPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        setBorder(new EmptyBorder(10, 20, 10, 20));

        styleField(titleField);
        styleField(noteField);

        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setForeground(TEXT_SECONDARY);
        JLabel noteLbl = new JLabel("Note:");
        noteLbl.setForeground(TEXT_SECONDARY);

        JButton addButton = new JButton("+ Add Task");
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 13f));
        styleButton(addButton, ACCENT_PURPLE);

        addButton.addActionListener(e -> submit());
        titleField.addActionListener(e -> submit());

        add(titleLbl);
        add(titleField);
        add(noteLbl);
        add(noteField);
        add(addButton);
    }

    private void styleField(JTextField field) {
        field.setBackground(BG_FIELD);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    private static void styleButton(JButton button, Color baseColor) {
        button.setBackground(baseColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(7, 16, 7, 16));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(baseColor.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(baseColor); }
        });
    }

    public void setOnAdd(BiConsumer<String, String> onAdd) {
        this.onAdd = onAdd;
    }

    private void submit() {
        String title = titleField.getText().trim();
        String note = noteField.getText().trim();
        if (title.isEmpty()) {
            titleField.requestFocusInWindow();
            return;
        }
        if (onAdd != null) onAdd.accept(title, note);
        titleField.setText("");
        noteField.setText("");
        titleField.requestFocusInWindow();
    }
}