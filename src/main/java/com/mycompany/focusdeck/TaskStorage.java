/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.focusdeck;

/**
 *
 * @author Areeba Malik
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TaskStorage {
    private final Path filePath;

    public TaskStorage(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) return tasks;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Task t = Task.fromLine(line);
                if (t != null) tasks.add(t);
            }
        } catch (IOException e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
        }
        return tasks;
    }

    public void save(List<Task> tasks) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Task t : tasks) {
                writer.write(t.toLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }

    public int nextId(List<Task> tasks) {
        int max = 0;
        for (Task t : tasks) max = Math.max(max, t.getId());
        return max + 1;
    }
}