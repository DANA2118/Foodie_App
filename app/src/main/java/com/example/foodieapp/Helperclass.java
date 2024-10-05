package com.example.foodieapp;

public class Helperclass {
    private String recipename;
    private String instructions;
    private String ingrediands;
    private String kcal;
    private String dataImage;
    private String dataVideo;

    public Helperclass(String recipename, String instructions, String ingrediands, String kcal, String dataImage, String dataVideo) {
        this.recipename = recipename;
        this.instructions = instructions;
        this.ingrediands = ingrediands;
        this.dataImage = dataImage;
        this.dataVideo = dataVideo;
        this.kcal = kcal;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getRecipename() {
        return recipename;
    }

    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngrediands(String ingrediands) {
        this.ingrediands = ingrediands;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public void setDataVideo(String dataVideo) {
        this.dataVideo = dataVideo;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getIngrediands() {
        return ingrediands;
    }

    public String getDataImage() {
        return dataImage;
    }

    public String getDataVideo() {
        return dataVideo;
    }
}
