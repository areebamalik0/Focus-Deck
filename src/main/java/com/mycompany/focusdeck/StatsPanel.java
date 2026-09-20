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
import java.time.LocalDate;
import java.util.List;

public class StatsPanel extends JPanel {

    private static final Color TEXT_PRIMARY = new Color(248, 248, 252);
    private static final Color BG_FIELD = new Color(42, 37, 62);
    private static final Color ACCENT_TEAL = new Color(0, 224, 179);
    private static final Color BORDER_LIGHT = new Color(100, 92, 140);

    private int completedToday = 0;
    private int totalCompleted = 0;
    private int remaining = 0;

    public StatsPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(220, 60));
        setBorder(new EmptyBorder(6, 6, 6, 6));
    }

    public void update(List<Task> allTasks) {
        String today = LocalDate.now().toString();
        completedToday = 0;
        totalCompleted = 0;
        remaining = 0;
        for (Task t : allTasks) {
            if (t.isDone()) {
                totalCompleted++;
                if (today.equals(t.getCompletedDate())) completedToday++;
            } else {
                remaining++;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13f));
        g2.setColor(TEXT_PRIMARY);
        String text = "Completed today: " + completedToday + "   |   Total completed: " + totalCompleted;
        g2.drawString(text, 4, 16);

        int barX = 4, barY = 30, barW = getWidth() - 12, barH = 14;
        int total = Math.max(1, totalCompleted + remaining);
        int filledW = (int) (barW * (totalCompleted / (double) total));

        g2.setColor(BG_FIELD);
        g2.fillRoundRect(barX, barY, barW, barH, 8, 8);

        g2.setColor(ACCENT_TEAL);
        g2.fillRoundRect(barX, barY, Math.max(filledW, filledW > 0 ? 8 : 0), barH, 8, 8);

        g2.setColor(BORDER_LIGHT);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(barX, barY, barW - 1, barH - 1, 8, 8);

        g2.dispose();
    }
}