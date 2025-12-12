/* So after monday when we figured out how the points work I changed it a bit.
I've made it so that if lets say 2 or more tasks are active both of them recieve same ammount of points
from the green action but should work as bob wanted to */
package com.greenvile.viewmodel;

import com.greenvile.model.*;
import java.util.List;

public class GreenActionViewModel {
    private DataManager dataManager;

    public GreenActionViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<GreenAction> getActionList() {
        return dataManager.getGreenActions();
    }

    public void addAction(GreenAction action) {
        action.setId(dataManager.generateNewGreenActionId());
        dataManager.getGreenActions().add(action);
        distributePointsToActiveGoals(action.getPointsAwarded());
    }

    private void distributePointsToActiveGoals(int points) {
        for (CommunalGoal goal : dataManager.getCommunalGoals()) {
            if (goal.isActive() && !goal.isCompleted()) {
                goal.addPoints(points);
            }
        }
    }

    public void updateAction(GreenAction action) {
        for (int i = 0; i < dataManager.getGreenActions().size(); i++) {
            if (dataManager.getGreenActions().get(i).getId() == action.getId()) {
                dataManager.getGreenActions().set(i, action);
                break;
            }
        }
    }

    public void deleteAction(int id) {
        dataManager.getGreenActions().removeIf(a -> a.getId() == id);
    }

    public void toggleWebsiteDisplay(int id) {
        for (GreenAction a : dataManager.getGreenActions()) {
            if (a.getId() == id) {
                a.setDisplayOnWebsite(!a.isDisplayOnWebsite());
                break;
            }
        }
    }

    public List<Resident> getAllResidents() {
        return dataManager.getResidents();
    }
}
