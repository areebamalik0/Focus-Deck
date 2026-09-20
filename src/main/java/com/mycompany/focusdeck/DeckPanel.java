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
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class DeckPanel extends JPanel {

    public interface DeckListener {
        void onTaskCompleted(Task task, TaskCard card);
        void onOrderChanged(List<Task> newOrderedTasks);
    }

    private final List<TaskCard> cards = new ArrayList<>();
    private DeckListener deckListener;
    private DoneZonePanel doneZone;

    private static final int CARD_W = 220, CARD_H = 90;
    private static final int GAP_X = 16, GAP_Y = 16;
    private static final int MARGIN = 16;

    public DeckPanel() {
        setLayout(null);
        setOpaque(false);
    }

    public void setDeckListener(DeckListener listener) { this.deckListener = listener; }
    public void setDoneZone(DoneZonePanel zone) { this.doneZone = zone; }

    public void addTaskCard(Task task) {
        TaskCard card = new TaskCard(task);
        card.setDragListener(this::handleDragEnd);
        card.getDoneButton().addActionListener(e -> completeCard(card));
        cards.add(card);
        add(card);
        relayout();
    }

    public void removeCard(TaskCard card) {
        cards.remove(card);
        remove(card);
        revalidate();
        repaint();
    }

    public List<TaskCard> getCards() { return cards; }

    public void relayout() {
        int cols = Math.max(1, (getWidth() - MARGIN) / (CARD_W + GAP_X));
        for (int i = 0; i < cards.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = MARGIN + col * (CARD_W + GAP_X);
            int y = MARGIN + row * (CARD_H + GAP_Y);
            TaskCard c = cards.get(i);
            c.setBounds(x, y, CARD_W, CARD_H);
        }
        int rows = (int) Math.ceil(cards.size() / (double) cols);
        setPreferredSize(new Dimension(getWidth(), MARGIN * 2 + rows * (CARD_H + GAP_Y)));
        revalidate();
        repaint();
    }

    private void handleDragEnd(TaskCard card, Point screenPoint) {
        if (doneZone != null && doneZone.containsScreenPoint(screenPoint)) {
            completeCard(card);
            return;
        }
        List<TaskCard> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparingInt((TaskCard c) -> c.getY() / (CARD_H / 2))
                .thenComparingInt(TaskCard::getX));
        cards.clear();
        cards.addAll(sorted);
        relayout();

        if (deckListener != null) {
            List<Task> ordered = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).getTask().setPriority(i);
                ordered.add(cards.get(i).getTask());
            }
            deckListener.onOrderChanged(ordered);
        }
    }

    private void completeCard(TaskCard card) {
        Task task = card.getTask();
        task.markDone();
        animateAndRemove(card, () -> {
            removeCard(card);
            relayout();
            if (deckListener != null) deckListener.onTaskCompleted(task, card);
        });
    }

    private void animateAndRemove(TaskCard card, Runnable onDone) {
        int startX = card.getX();
        int targetX = startX + 260;
        int steps = 14;
        int[] frame = {0};

        Timer timer = new Timer(16, null);
        timer.addActionListener(e -> {
            frame[0]++;
            float progress = frame[0] / (float) steps;
            int newX = (int) (startX + (targetX - startX) * progress);
            card.setLocation(newX, card.getY());
            card.setAlpha(1f - progress);
            if (frame[0] >= steps) {
                timer.stop();
                onDone.run();
            }
        });
        timer.start();
    }
}