/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.focusdeck;

/**
 *
 * @author Areeba Malik
 */
import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String note;
    private boolean done;
    private int priority;
    private String completedDate;

    public Task(int id, String title, String note, int priority) {
        this.id = id;
        this.title = title;
        this.note = note == null ? "" : note;
        this.priority = priority;
        this.done = false;
        this.completedDate = "";
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getNote() { return note; }
    public boolean isDone() { return done; }
    public int getPriority() { return priority; }
    public String getCompletedDate() { return completedDate; }

    public void setTitle(String title) { this.title = title; }
    public void setNote(String note) { this.note = note; }
    public void setPriority(int priority) { this.priority = priority; }

    public void markDone() {
        this.done = true;
        this.completedDate = LocalDate.now().toString();
    }

    public void markUndone() {
        this.done = false;
        this.completedDate = "";
    }

    public String toLine() {
        return id + "|" + escape(title) + "|" + escape(note) + "|" + done + "|" + priority + "|" + completedDate;
    }

    public static Task fromLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) return null;
        int id = Integer.parseInt(parts[0]);
        String title = unescape(parts[1]);
        String note = unescape(parts[2]);
        boolean done = Boolean.parseBoolean(parts[3]);
        int priority = Integer.parseInt(parts[4]);
        String completedDate = parts[5];

        Task t = new Task(id, title, note, priority);
        if (done) {
            t.done = true;
            t.completedDate = completedDate;
        }
        return t;
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("|", "\\p").replace("\n", "\\n");
    }

    private static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char next = s.charAt(i + 1);
                if (next == 'p') { sb.append('|'); i++; }
                else if (next == 'n') { sb.append('\n'); i++; }
                else if (next == '\\') { sb.append('\\'); i++; }
                else sb.append(c);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}