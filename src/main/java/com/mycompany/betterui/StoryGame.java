/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.betterui;

/**
 *
 * @author gmlol
 */
public class StoryGame extends Game {
    private String chapter;
    private String level;

    public StoryGame(String title, String genre, String extraDetail, String tracker, String imagePath, String chapter, String level) {
        super(title, genre, extraDetail, tracker, imagePath); 
        this.chapter = chapter;
        this.level = level;
    }

    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    // POLYMORPHISM: Overriding parent behavior
    @Override
    public String toCSV() {
        return getTitle() + "," + getGenre() + "," + getExtraDetail() + "," + 
               getTracker() + "," + getImagePath() + ",Story," + chapter + "," + level;
    }
}