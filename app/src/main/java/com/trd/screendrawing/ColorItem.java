package com.trd.screendrawing;


public class ColorItem {
    private final String hexHash;
    private final String contrastHexHash;
    private String colorName;
    private String hex;
    private String contrastHex;

    public String getHexHash() {
        return this.hexHash;
    }

    public String getContrastHexHash() {
        return this.contrastHexHash;
    }

    public String getColorName() {
        return this.colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getHex() {
        return this.hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getContrastHex() {
        return this.contrastHex;
    }

    public void setContrastHex(String contrastHex) {
        this.contrastHex = contrastHex;
    }

    public ColorItem(String colorName, String hex, String contrastHex) {
        super();
        this.colorName = colorName;
        this.hex = hex;
        this.contrastHex = contrastHex;
        this.hexHash = '#' + this.hex;
        this.contrastHexHash = '#' + this.contrastHex;
    }
}
