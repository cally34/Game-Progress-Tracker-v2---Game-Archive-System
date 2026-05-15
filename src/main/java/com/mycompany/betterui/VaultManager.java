/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.betterui;

/**
 *
 * @author gmlol
 */
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class VaultManager {
    private String currentFilePath; 
    private Map<String, Game> gamesDB = new HashMap<>();
    private Preferences prefs;

    public VaultManager() {
        prefs = Preferences.userNodeForPackage(VaultManager.class);
        currentFilePath = prefs.get("LAST_FILE_PATH", "games.csv");
        loadFromFile(currentFilePath);
    }

    public void setFileName(String newPath) {
        this.gamesDB.clear(); 
        this.currentFilePath = newPath; 
        prefs.put("LAST_FILE_PATH", newPath); 
        loadFromFile(newPath);
    }

    public void saveAsFile(String newPath) {
        this.currentFilePath = newPath;
        prefs.put("LAST_FILE_PATH", newPath);
        saveToFile();
    }

    private void loadFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 8); 
                
                if (data.length >= 5) {
                    if (data.length == 8 && data[5].equals("Story")) {
                        gamesDB.put(data[0], new StoryGame(data[0], data[1], data[2], data[3], data[4], data[6], data[7]));
                    } else {
                        gamesDB.put(data[0], new Game(data[0], data[1], data[2], data[3], data[4]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentFilePath))) {
            for (Game g : gamesDB.values()) {
                bw.write(g.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    public boolean insert(Game g) {
        if (gamesDB.containsKey(g.getTitle())) return false; 
        gamesDB.put(g.getTitle(), g);
        saveToFile();
        return true;
    }

    public boolean update(String title, String genre, String extra, String tracker, String imagePath, boolean isStory, String chapter, String level) {
        if (!gamesDB.containsKey(title)) return false;
        
        Game oldGame = gamesDB.get(title);
        String finalImagePath = oldGame.getImagePath(); 
        
        // Ensure image is not overwritten if a new one isn't selected
        if (!imagePath.equals("none") && !imagePath.isEmpty()) {
            finalImagePath = imagePath;
        }

        if (isStory) {
            gamesDB.put(title, new StoryGame(title, genre, extra, tracker, finalImagePath, chapter, level));
        } else {
            gamesDB.put(title, new Game(title, genre, extra, tracker, finalImagePath));
        }
        
        saveToFile();
        return true;
    }

    public boolean delete(String title) {
        if (gamesDB.remove(title) != null) {
            saveToFile();
            return true;
        }
        return false;
    }

    public Map<String, Game> getGames() { return gamesDB; }
}