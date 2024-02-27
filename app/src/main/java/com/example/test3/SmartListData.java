package com.example.test3;

public class SmartListData {

    private String textName;
    private String textCount;
    private Integer image;
    private int circleColor;

    public SmartListData(String textName, String textCount, Integer image, int circleColor) {
        this.textName = textName;
        this.textCount = textCount;
        this.image = image;
        this.circleColor = circleColor;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getTextCount() {
        return textCount;
    }

    public void setTextCount(String textDesc) {
        this.textCount = textCount;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Integer getCircleColor() {
        return circleColor;
    }
    public void setCircleColor(Integer circleColor) {
        this.circleColor = circleColor;
    }
}
