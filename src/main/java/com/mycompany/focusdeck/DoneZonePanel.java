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
import java.awt.*;

public class DoneZonePanel extends JPanel {

    private static final Color BORDER = new Color(0, 224, 179);
    private static final Color BG_HOVER = new Color(38, 33, 60);
    private static final Color TEXT_PRIMARY = new Color(248, 248, 252);

    public DoneZonePanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(170, 90));
    }

    public boolean containsScreenPoint(Point screenPoint) {
        if (!isShowing()) return false;
        Point origin = getLocationOnScreen();
        Rectangle bounds = new Rectangle(origin.x, origin.y, getWidth(), getHeight());
        return bounds.contains(screenPoint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(BG_HOVER);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

        float[] dash = {6f, 6f};
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dash, 0));
        g2.setColor(BORDER);
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 14, 14);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13f));
        FontMetrics fm = g2.getFontMetrics();
        String label = "Drop here \u2713";
        int tx = (getWidth() - fm.stringWidth(label)) / 2;
        int ty = getHeight() / 2 + fm.getAscent() / 2 - 2;
        g2.setColor(TEXT_PRIMARY);
        g2.drawString(label, tx, ty);

        g2.dispose();
    }
}