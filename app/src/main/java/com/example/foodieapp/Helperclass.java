package com.example.foodieapp;

public class Helperclass {
    private String recipename;
    private String instructions;
    private String ingrediands;
    private String dataImage;
    private String dataVideo;

    public Helperclass(String recipename, String instructions, String ingrediands, String dataImage, String dataVideo) {
        this.recipename = recipename;
        this.instructions = instructions;
        this.ingrediands = ingrediands;
        this.dataImage = dataImage;
        this.dataVideo = dataVideo;
    }

    public String getRecipename() {
        return recipename;
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
