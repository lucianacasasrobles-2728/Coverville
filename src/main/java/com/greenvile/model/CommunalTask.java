package com.greenvile.model;

import java.util.ArrayList;
import java.util.List;

public class CommunalTask {
    private int id;
    private String title;
    private String description;
    private int pointsAwarded;
    private boolean completed;
    private boolean displayOnWebsite;
    private List<Integer> participantIds;

    public CommunalTask() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.pointsAwarded = 0;
        this.completed = false;
        this.displayOnWebsite = false;
        this.participantIds = new ArrayList<>();
    }

    public CommunalTask(int id, String title, String description, int pointsAwarded) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pointsAwarded = pointsAwarded;
        this.completed = false;
        this.displayOnWebsite = false;
        this.participantIds = new ArrayList<>();
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

    public int getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
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

    public List<Integer> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Integer> participantIds) {
        this.participantIds = participantIds;
    }

    public void addParticipant(int residentId) {
        if (!participantIds.contains(residentId)) {
            participantIds.add(residentId);
        }
    }

    public void removeParticipant(int residentId) {
        participantIds.remove(Integer.valueOf(residentId));
    }

    public boolean hasParticipants() {
        return !participantIds.isEmpty();
    }

    public String toString() {
        String status = "";
        if (completed) {
            status = " [COMPLETED]";
        } else if (displayOnWebsite) {
            status = " [ACTIVE]";
        }
        return title + status;
    }
}
