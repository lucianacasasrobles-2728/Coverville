/* just another class to instantiate something. in this case we doing the green action. note that the points from it are dirrected to the communal goal. */
package com.greenvile.model;

public class GreenAction {
    private int id;
    private String title;
    private String description;
    private String picturePath;
    private int pointsAwarded;
    private boolean displayOnWebsite;
    private int participantId;

    public GreenAction() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.picturePath = "";
        this.pointsAwarded = 0;
        this.displayOnWebsite = false;
        this.participantId = 0;
    }

    public GreenAction(int id, String title, String description, String picturePath, int pointsAwarded) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.picturePath = picturePath;
        this.pointsAwarded = pointsAwarded;
        this.displayOnWebsite = false;
        this.participantId = 0;
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

    public int getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public boolean isDisplayOnWebsite() {
        return displayOnWebsite;
    }

    public void setDisplayOnWebsite(boolean displayOnWebsite) {
        this.displayOnWebsite = displayOnWebsite;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public String toString() {
        String status = "";
        if (displayOnWebsite) {
            status = " [ACTIVE]";
        }
        return title + status;
    }
}
