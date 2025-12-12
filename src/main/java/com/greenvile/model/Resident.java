/* this is a child class of Person. This is the only reason why we only got 1 super class cuz this is the only one with inheritence (Extends meakes it inherit the person class). but this class also has the check logic for the boosting (the 10, 20 and 30% boosts). 
The boost is hardcoded and can not be changed by admin.
This is by far the #1 class we need to write about bcs:
It does a data check
It has iheritence
has the algorithmic bonus system
not just a buch of getter setter bs that would be done automatically in C# 
Has a validation logic 
calculated value output.
has @overider which overrides the points display.
We can yap bout this class a fuck ton and look smart*/
package com.greenvile.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Resident extends Person {
    private int personalPoints;
    private String lastTaskCompletionDate;

    public Resident() {
        super();
        this.personalPoints = 0;
        this.lastTaskCompletionDate = "";
    }

    public Resident(int id, String fullName, String phoneNumber, String email, String address, int personalPoints) {
        super(id, fullName, phoneNumber, email, address);
        this.personalPoints = personalPoints;
        this.lastTaskCompletionDate = "";
    }

    public int getPersonalPoints() {
        return personalPoints;
    }

    public void setPersonalPoints(int personalPoints) {
        this.personalPoints = personalPoints;
    }

    public void addPoints(int points) {
        this.personalPoints += points;
    }

    public boolean deductPoints(int points) {
        if (this.personalPoints >= points) {
            this.personalPoints -= points;
            return true;
        }
        return false;
    }

    public String getLastTaskCompletionDate() {
        return lastTaskCompletionDate;
    }

    public void setLastTaskCompletionDate(String date) {
        this.lastTaskCompletionDate = date;
    }

    public void updateLastTaskDate() {
        this.lastTaskCompletionDate = LocalDate.now().toString();
    }

    public int getBonusPercentage() {
        if (lastTaskCompletionDate == null || lastTaskCompletionDate.isEmpty()) {
            return 30;
        }
        LocalDate lastDate = LocalDate.parse(lastTaskCompletionDate);
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(lastDate, today);

        if (daysBetween >= 30) {
            return 30;
        } else if (daysBetween >= 14) {
            return 20;
        } else if (daysBetween >= 7) {
            return 10;
        }
        return 0;
    }

    public int calculatePointsWithBonus(int basePoints) {
        int bonus = getBonusPercentage();
        return basePoints + (basePoints * bonus / 100);
    }

    public String toString() {
        return getFullName() + " (" + personalPoints + " pts)";
    }
}
