/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.betterui;

/**
 *
 * @author gmlol
 */
public class Game {
    private String title;
    private String genre;
    private String extraDetail;
    private String tracker;    
    private String imagePath;  

    public Game(String title, String genre, String extraDetail, String tracker, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.extraDetail = extraDetail;
        this.tracker = tracker;
        this.imagePath = imagePath;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getExtraDetail() { return extraDetail; }
    public void setExtraDetail(String extraDetail) { this.extraDetail = extraDetail; }

    public String getTracker() { return tracker; }
    public void setTracker(String tracker) { this.tracker = tracker; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    // Base CSV format marks the game as "Standard"
    public String toCSV() {
        return title + "," + genre + "," + extraDetail + "," + tracker + "," + imagePath + ",Standard,N/A,N/A";
    }
}