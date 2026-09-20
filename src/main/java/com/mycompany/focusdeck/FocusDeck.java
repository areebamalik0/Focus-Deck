/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.focusdeck;

/**
 *
 * @author Areeba Malik
 */
import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class FocusDeck extends JFrame {

    private static final Color BG_TOP = new Color(28, 22, 48);
    private static final Color BG_BOTTOM = new Color(16, 18, 34);
    private static final Color ACCENT_PURPLE = new Color(168, 120, 255);
    private static final Color ACCENT_TEAL = new Color(0, 224, 179);
    private static final Color TEXT_SECONDARY = new Color(185, 180, 210);

    private final TaskStorage storage = new TaskStorage("focusdeck_tasks.txt");
    private final List<Task> allTasks = new ArrayList<>();

    private final DeckPanel deckPanel = new DeckPanel();
    private final DoneZonePanel doneZone = new DoneZonePanel();
    private final AddTaskPanel addTaskPanel = new AddTaskPanel();
    private final TimerPanel timerPanel = new TimerPanel();
    private final StatsPanel statsPanel = new StatsPanel();

    public FocusDeck() {
        super("Focus Deck");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 660);
        setMinimumSize(new Dimension(620, 500));
        setLocationRelativeTo(null);

        setContentPane(new GradientBackground());
        getContentPane().setLayout(new BorderLayout());

        buildLayout();
        wireEvents();
        loadTasksFromDisk();
    }

    private static class GradientBackground extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            GradientPaint gp = new GradientPaint(0, 0, BG_TOP, getWidth(), getHeight(), BG_BOTTOM);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private static class GradientTitleLabel extends JLabel {
        GradientTitleLabel(String text) {
            super(text, SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.BOLD, 32));
            Map<TextAttribute, Object> attrs = new java.util.HashMap<>(getFont().getAttributes());
            attrs.put(TextAttribute.TRACKING, 0.12);
            setFont(getFont().deriveFont(attrs));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            FontMetrics fm = g2.getFontMetrics(getFont());
            String text = getText();
            int textWidth = fm.stringWidth(text);
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            GradientPaint gp = new GradientPaint(x, 0, ACCENT_PURPLE, x + textWidth, 0, ACCENT_TEAL);
            g2.setPaint(gp);
            g2.setFont(getFont());
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(22, 20, 10, 20));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        GradientTitleLabel title = new GradientTitleLabel("FOCUS DECK");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        title.setPreferredSize(new Dimension(400, 46));

        JLabel subtitle = new JLabel("Drag a card to the done zone to complete it", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.ITALIC, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JSeparator divider = new JSeparator();
        divider.setForeground(ACCENT_PURPLE);
        divider.setBackground(new Color(0, 0, 0, 0));
        divider.setMaximumSize(new Dimension(220, 2));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(10));
        header.add(divider);
        return header;
    }

    private void buildLayout() {
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(buildHeader());
        topPanel.add(addTaskPanel);
        topPanel.add(timerPanel);
        add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(deckPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        deckPanel.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(6, 20, 18, 20));
        bottomPanel.add(doneZone, BorderLayout.WEST);
        bottomPanel.add(statsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        deckPanel.setDoneZone(doneZone);
    }

    private void wireEvents() {
        addTaskPanel.setOnAdd((title, note) -> {
            int id = storage.nextId(allTasks);
            Task task = new Task(id, title, note, allTasks.size());
            allTasks.add(task);
            deckPanel.addTaskCard(task);
            persistAndRefreshStats();
        });

        deckPanel.setDeckListener(new DeckPanel.DeckListener() {
            @Override
            public void onTaskCompleted(Task task, TaskCard card) {
                persistAndRefreshStats();
            }

            @Override
            public void onOrderChanged(List<Task> newOrderedTasks) {
                persistAndRefreshStats();
            }
        });
    }

    private void loadTasksFromDisk() {
        List<Task> loaded = storage.load();
        allTasks.addAll(loaded);
        allTasks.sort((a, b) -> Integer.compare(a.getPriority(), b.getPriority()));
        for (Task t : allTasks) {
            if (!t.isDone()) {
                deckPanel.addTaskCard(t);
            }
        }
        statsPanel.update(allTasks);
    }

    private void persistAndRefreshStats() {
        storage.save(allTasks);
        statsPanel.update(allTasks);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new FocusDeck().setVisible(true);
        });
    }
}
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    

