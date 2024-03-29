package com.example.test3;

public class MainModel {
    String taskTitle;
    String taskId;
    String taskDesc;
    String date;
    String isComplete;
    String alarm;
    String lastOpenedDate;
    String creationDate;
    String taskPassword;
    String completedDate;

    MainModel() {

    }

    public String getLastOpenedDate() {
        return lastOpenedDate;
    }

    public void setLastOpenedDate(String lastOpenedDate) {
        this.lastOpenedDate = lastOpenedDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }


    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public MainModel(String taskTitle, String taskId, String alarm, String taskDesc, String date,
                     String isComplete, String lastOpenedDate, String creationDate, String taskPassword, String completedDate) {
        this.taskTitle = taskTitle;
        this.taskId = taskId;
        this.alarm = alarm;
        this.taskDesc = taskDesc;
        this.date = date;
        this.isComplete = isComplete;
        this.lastOpenedDate = lastOpenedDate;
        this.creationDate = creationDate;
        this.taskPassword = taskPassword;
        this.completedDate = completedDate;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getTaskPassword() {
        return taskPassword;
    }

    public void setTaskPassword(String taskPassword) {
        this.taskPassword = taskPassword;
    }

}