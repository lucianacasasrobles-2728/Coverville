/* Copies the default actions, find tasks, finds residents, gives them the points
I'll write a better text next week explaining this class */
package com.greenvile.viewmodel;

import com.greenvile.model.*;
import java.util.List;

public class CommunalViewModel {
    private DataManager dataManager;

    public CommunalViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<CommunalGoal> getGoalList() {
        return dataManager.getCommunalGoals();
    }

    public void addGoal(CommunalGoal goal) {
        goal.setId(dataManager.generateNewCommunalGoalId());
        dataManager.getCommunalGoals().add(goal);
        dataManager.copyDefaultTasks(goal);
    }

    public void updateGoal(CommunalGoal goal) {
        for (int i = 0; i < dataManager.getCommunalGoals().size(); i++) {
            if (dataManager.getCommunalGoals().get(i).getId() == goal.getId()) {
                dataManager.getCommunalGoals().set(i, goal);
                break;
            }
        }
    }

    public void deleteGoal(int id) {
        dataManager.getCommunalGoals().removeIf(g -> g.getId() == id);
    }

    public void toggleGoalActive(int goalId) {
        for (CommunalGoal g : dataManager.getCommunalGoals()) {
            if (g.getId() == goalId) {
                g.setActive(!g.isActive());
                break;
            }
        }
    }

    public void addTaskToGoal(int goalId, CommunalTask task) {
        for (CommunalGoal g : dataManager.getCommunalGoals()) {
            if (g.getId() == goalId) {
                task.setId(dataManager.generateNewTaskId(g));
                g.addTask(task);
                break;
            }
        }
    }

    public void updateTask(int goalId, CommunalTask task) {
        for (CommunalGoal g : dataManager.getCommunalGoals()) {
            if (g.getId() == goalId) {
                for (int i = 0; i < g.getTasks().size(); i++) {
                    if (g.getTasks().get(i).getId() == task.getId()) {
                        g.getTasks().set(i, task);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void deleteTask(int goalId, int taskId) {
        for (CommunalGoal g : dataManager.getCommunalGoals()) {
            if (g.getId() == goalId) {
                g.getTasks().removeIf(t -> t.getId() == taskId);
                break;
            }
        }
    }

    public boolean canCompleteTask(CommunalTask task) {
        return task.hasParticipants();
    }

    public void markTaskCompleted(int goalId, int taskId) {
        for (CommunalGoal g : dataManager.getCommunalGoals()) {
            if (g.getId() == goalId) {
                for (CommunalTask t : g.getTasks()) {
                    if (t.getId() == taskId && t.hasParticipants()) {
                        t.setCompleted(true);
                        
                        for (int residentId : t.getParticipantIds()) {
                            Resident r = dataManager.getResidentById(residentId);
                            if (r != null) {
                                int pointsWithBonus = r.calculatePointsWithBonus(t.getPointsAwarded());
                                r.addPoints(pointsWithBonus);
                                r.updateLastTaskDate();
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    public void markGoalCompleted(int goalId) {
        for (CommunalGoal g : dataManager.getCommunalGoals()) {
            if (g.getId() == goalId) {
                g.setCompleted(true);
                g.setActive(false);
                break;
            }
        }
    }

    public CommunalGoal getGoalById(int id) {
        for (CommunalGoal g : dataManager.getCommunalGoals()) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    public List<CommunalTask> getDefaultTasks() {
        return dataManager.getDefaultTasks();
    }

    public void addDefaultTask(CommunalTask task) {
        task.setId(dataManager.generateNewDefaultTaskId());
        dataManager.getDefaultTasks().add(task);
    }

    public void updateDefaultTask(CommunalTask task) {
        for (int i = 0; i < dataManager.getDefaultTasks().size(); i++) {
            if (dataManager.getDefaultTasks().get(i).getId() == task.getId()) {
                dataManager.getDefaultTasks().set(i, task);
                break;
            }
        }
    }

    public void deleteDefaultTask(int id) {
        dataManager.getDefaultTasks().removeIf(t -> t.getId() == id);
    }

    public List<Resident> getAllResidents() {
        return dataManager.getResidents();
    }
}
