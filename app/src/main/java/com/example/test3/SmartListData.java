package com.example.test3;

public class SmartListData {

    private String textName;

    private Integer image;
    private int circleColor;

    public SmartListData(String textName, Integer image, int circleColor) {
        this.textName = textName;

        this.image = image;
        this.circleColor = circleColor;
    }


    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
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
