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
import java.awt.event.*;

public class TaskCard extends JPanel {

    public interface DragListener {
        void onDragEnd(TaskCard card, Point screenLocation);
    }

    private final Task task;
    private final JLabel titleLabel;
    private final JLabel noteLabel;
    private final JButton doneButton;

    private Point dragOffset;
    private DragListener dragListener;
    private float alpha = 1f;

    private static final Color BG = new Color(46, 40, 70);
    private static final Color BORDER = new Color(90, 80, 130);
    private static final Color ACCENT = new Color(168, 120, 255);
    private static final Color TEXT_PRIMARY = new Color(248, 248, 252);
    private static final Color TEXT_SECONDARY = new Color(185, 180, 210);
    private static final Color DONE_COLOR = new Color(0, 224, 179);

    public TaskCard(Task task) {
        this.task = task;
        setOpaque(false);
        setLayout(new BorderLayout(6, 4));
        setBorder(new EmptyBorder(10, 12, 10, 12));
        setPreferredSize(new Dimension(220, 90));
        setSize(getPreferredSize());

        titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        titleLabel.setForeground(TEXT_PRIMARY);

        noteLabel = new JLabel("<html><body style='width:170px'>" + escapeHtml(task.getNote()) + "</body></html>");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.PLAIN, 11f));
        noteLabel.setForeground(TEXT_SECONDARY);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(noteLabel);

        doneButton = new JButton("Done");
        doneButton.setFont(doneButton.getFont().deriveFont(Font.BOLD, 11f));
        styleButton(doneButton, DONE_COLOR);

        add(textPanel, BorderLayout.CENTER);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomRow.setOpaque(false);
        bottomRow.add(doneButton);
        add(bottomRow, BorderLayout.SOUTH);

        enableDragging();
    }

    private static void styleButton(JButton button, Color baseColor) {
        button.setBackground(baseColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(5, 14, 5, 14));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(baseColor.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(baseColor); }
        });
    }

    public Task getTask() { return task; }
    public JButton getDoneButton() { return doneButton; }

    public void setDragListener(DragListener listener) {
        this.dragListener = listener;
    }

    public void setAlpha(float alpha) {
        this.alpha = Math.max(0f, Math.min(1f, alpha));
        repaint();
    }

    private void enableDragging() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset = e.getPoint();
                getParent().setComponentZOrder(TaskCard.this, 0);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragOffset == null) return;
                Point parentPoint = SwingUtilities.convertPoint(TaskCard.this, e.getPoint(), getParent());
                int newX = parentPoint.x - dragOffset.x;
                int newY = parentPoint.y - dragOffset.y;
                setLocation(newX, newY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragListener != null) {
                    Point screenPoint = e.getLocationOnScreen();
                    dragListener.onDragEnd(TaskCard.this, screenPoint);
                }
                dragOffset = null;
            }
        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int w = getWidth(), h = getHeight();

        g2.setColor(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 55));
        g2.fillRoundRect(2, 5, w - 4, h - 4, 18, 18);

        g2.setColor(BG);
        g2.fillRoundRect(0, 0, w - 5, h - 5, 16, 16);

        g2.setColor(BORDER);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(0, 0, w - 6, h - 6, 16, 16);

        g2.setColor(ACCENT);
        g2.fillRoundRect(0, 0, 5, h - 5, 6, 6);

        g2.dispose();
        super.paintComponent(g);
    }
}