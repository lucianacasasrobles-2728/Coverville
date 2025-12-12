/* this shit holds all the lists. It is THE memory until its saved to file
I suppose the get residents by Id is funny becasuse it has to go through 
all Id's like a dumbass until it gets the correct one to work.
I would take this and 2nd choice to write about because of all the aggregation
This class does not inherit anything but contains pretty much everything (I know sounds dumb af and it will be till you move to C#)
It also initializes the emptuy lists.
Has a logic that allows us to have multiple people with same name(some call it a bug I call that shit a feature) 
Coppy default tasks is fun cus it goes through the list
because instead of coppying the default tasks it creates the new tasks under the goals 
Also if your went through it this covers encapsulation (communalPointsPool)
makes it so that the everthing else in the code must call this shit to modyfi the communal points*/
package com.greenvile.model;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<Resident> residents;
    private List<GreenAction> greenActions;
    private List<CommunalGoal> communalGoals;
    private List<Trade> trades;
    private List<CommunalTask> defaultTasks;
    private int communalPointsPool;

    public DataManager() {
        this.residents = new ArrayList<>();
        this.greenActions = new ArrayList<>();
        this.communalGoals = new ArrayList<>();
        this.trades = new ArrayList<>();
        this.defaultTasks = new ArrayList<>();
        this.communalPointsPool = 0;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    public void setResidents(List<Resident> residents) {
        this.residents = residents;
    }

    public List<GreenAction> getGreenActions() {
        return greenActions;
    }

    public void setGreenActions(List<GreenAction> greenActions) {
        this.greenActions = greenActions;
    }

    public List<CommunalGoal> getCommunalGoals() {
        return communalGoals;
    }

    public void setCommunalGoals(List<CommunalGoal> communalGoals) {
        this.communalGoals = communalGoals;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<CommunalTask> getDefaultTasks() {
        return defaultTasks;
    }

    public void setDefaultTasks(List<CommunalTask> defaultTasks) {
        this.defaultTasks = defaultTasks;
    }

    public int getCommunalPointsPool() {
        return communalPointsPool;
    }

    public void setCommunalPointsPool(int communalPointsPool) {
        this.communalPointsPool = communalPointsPool;
    }

    public void addToCommunalPool(int points) {
        this.communalPointsPool += points;
    }

    public int generateNewResidentId() {
        int maxId = 0;
        for (Resident r : residents) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
        }
        return maxId + 1;
    }

    public int generateNewGreenActionId() {
        int maxId = 0;
        for (GreenAction a : greenActions) {
            if (a.getId() > maxId) {
                maxId = a.getId();
            }
        }
        return maxId + 1;
    }

    public int generateNewCommunalGoalId() {
        int maxId = 0;
        for (CommunalGoal g : communalGoals) {
            if (g.getId() > maxId) {
                maxId = g.getId();
            }
        }
        return maxId + 1;
    }

    public int generateNewTaskId(CommunalGoal goal) {
        int maxId = 0;
        for (CommunalTask t : goal.getTasks()) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
        }
        return maxId + 1;
    }

    public int generateNewDefaultTaskId() {
        int maxId = 0;
        for (CommunalTask t : defaultTasks) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
        }
        return maxId + 1;
    }

    public int generateNewTradeId() {
        int maxId = 0;
        for (Trade t : trades) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
        }
        return maxId + 1;
    }

    public Resident getResidentById(int id) {
        for (Resident r : residents) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public List<CommunalTask> copyDefaultTasks(CommunalGoal goal) {
        List<CommunalTask> copiedTasks = new ArrayList<>();
        for (CommunalTask defaultTask : defaultTasks) {
            CommunalTask newTask = new CommunalTask(
                generateNewTaskId(goal),
                defaultTask.getTitle(),
                defaultTask.getDescription(),
                defaultTask.getPointsAwarded()
            );
            copiedTasks.add(newTask);
            goal.addTask(newTask);
        }
        return copiedTasks;
    }
}
