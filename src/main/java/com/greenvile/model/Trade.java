/* This one also kinda simple but not since we got now the Buyer ID. Basically on wedesday I noticed that the trade system was ass so I added logic to move the points from one person to another. But in short another class to instanciate objects. Its almost all that the model does for us, creates objects that we want to use in the app
 */
package com.greenvile.model;

public class Trade {
    private int id;
    private String title;
    private String description;
    private String picturePath;
    private int pointsCost;
    private int residentId;
    private int buyerId;
    private boolean completed;
    private boolean displayOnWebsite;

    public Trade() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.picturePath = "";
        this.pointsCost = 0;
        this.residentId = 0;
        this.buyerId = 0;
        this.completed = false;
        this.displayOnWebsite = true;
    }

    public Trade(int id, String title, String description, String picturePath, int pointsCost, int residentId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.picturePath = picturePath;
        this.pointsCost = pointsCost;
        this.residentId = residentId;
        this.buyerId = 0;
        this.completed = false;
        this.displayOnWebsite = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getPointsCost() {
        return pointsCost;
    }

    public void setPointsCost(int pointsCost) {
        this.pointsCost = pointsCost;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isDisplayOnWebsite() {
        return displayOnWebsite;
    }

    public void setDisplayOnWebsite(boolean displayOnWebsite) {
        this.displayOnWebsite = displayOnWebsite;
    }

    public String toString() {
        String status = "";
        if (completed) {
            status = " [COMPLETED]";
        } else if (displayOnWebsite) {
            status = " [ACTIVE]";
        }
        return title + " (" + pointsCost + " pts)" + status;
    }
}
