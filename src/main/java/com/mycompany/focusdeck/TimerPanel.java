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

public class TimerPanel extends JPanel {

    private static final int DEFAULT_SECONDS = 25 * 60;
    private static final Color TEXT_PRIMARY = new Color(248, 248, 252);
    private static final Color TEXT_SECONDARY = new Color(185, 180, 210);
    private static final Color ACCENT_AMBER = new Color(255, 176, 32);
    private static final Color ACCENT_CORAL = new Color(255, 82, 110);

    private int secondsLeft = DEFAULT_SECONDS;
    private Timer swingTimer;
    private final JLabel clockLabel = new JLabel(format(DEFAULT_SECONDS));
    private final JButton startPauseButton = new JButton("Start");
    private final JButton resetButton = new JButton("Reset");
    private boolean running = false;

    public TimerPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        setBorder(new EmptyBorder(4, 20, 14, 20));

        JLabel caption = new JLabel("Focus Session:");
        caption.setForeground(TEXT_SECONDARY);

        clockLabel.setFont(clockLabel.getFont().deriveFont(Font.BOLD, 22f));
        clockLabel.setForeground(TEXT_PRIMARY);

        startPauseButton.setFont(startPauseButton.getFont().deriveFont(Font.BOLD, 13f));
        resetButton.setFont(resetButton.getFont().deriveFont(Font.BOLD, 13f));
        styleButton(startPauseButton, ACCENT_AMBER);
        styleButton(resetButton, ACCENT_CORAL);

        swingTimer = new Timer(1000, e -> tick());

        startPauseButton.addActionListener(e -> toggle());
        resetButton.addActionListener(e -> reset());

        add(caption);
        add(clockLabel);
        add(startPauseButton);
        add(resetButton);
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

    private void tick() {
        if (secondsLeft > 0) {
            secondsLeft--;
            clockLabel.setText(format(secondsLeft));
        } else {
            swingTimer.stop();
            running = false;
            startPauseButton.setText("Start");
            clockLabel.setText("Session done!");
        }
    }

    private void toggle() {
        running = !running;
        if (running) {
            swingTimer.start();
            startPauseButton.setText("Pause");
        } else {
            swingTimer.stop();
            startPauseButton.setText("Start");
        }
    }

    private void reset() {
        swingTimer.stop();
        running = false;
        secondsLeft = DEFAULT_SECONDS;
        clockLabel.setText(format(secondsLeft));
        startPauseButton.setText("Start");
    }

    private static String format(int totalSeconds) {
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return String.format("%02d:%02d", m, s);
    }
}