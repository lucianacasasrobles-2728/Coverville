package com.greenvile.view;

import com.greenvile.model.CommunalGoal;
import com.greenvile.model.CommunalTask;
import com.greenvile.viewmodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class CommunalController {
    @FXML private ListView<CommunalGoal> goalListView;
    @FXML private ListView<CommunalTask> taskListView;
    @FXML private Button updateGoalButton;
    @FXML private Button deleteGoalButton;
    @FXML private Button completeGoalButton;
    @FXML private Button toggleActiveButton;
    @FXML private Button addTaskButton;
    @FXML private Button updateTaskButton;
    @FXML private Button deleteTaskButton;
    @FXML private Button completeTaskButton;
    @FXML private Button toggleTaskDisplayButton;

    private MainViewModel mainViewModel;
    private CommunalViewModel communalViewModel;

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        this.communalViewModel = new CommunalViewModel(mainViewModel.getDataManager());
        loadGoals();
    }

    @FXML
    private void initialize() {
        updateGoalButton.setDisable(true);
        deleteGoalButton.setDisable(true);
        completeGoalButton.setDisable(true);
        toggleActiveButton.setDisable(true);
        addTaskButton.setDisable(true);
        updateTaskButton.setDisable(true);
        deleteTaskButton.setDisable(true);
        completeTaskButton.setDisable(true);
        toggleTaskDisplayButton.setDisable(true);

        goalListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            updateGoalButton.setDisable(!hasSelection);
            deleteGoalButton.setDisable(!hasSelection);
            completeGoalButton.setDisable(!hasSelection);
            toggleActiveButton.setDisable(!hasSelection);
            addTaskButton.setDisable(!hasSelection);
            if (hasSelection) {
                loadTasks(newVal);
            } else {
                taskListView.getItems().clear();
            }
        });

        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            updateTaskButton.setDisable(!hasSelection);
            deleteTaskButton.setDisable(!hasSelection);
            completeTaskButton.setDisable(!hasSelection);
            toggleTaskDisplayButton.setDisable(!hasSelection);
        });
    }

    private void loadGoals() {
        ObservableList<CommunalGoal> goals = FXCollections.observableArrayList(communalViewModel.getGoalList());
        goalListView.setItems(goals);
    }

    private void loadTasks(CommunalGoal goal) {
        ObservableList<CommunalTask> tasks = FXCollections.observableArrayList(goal.getTasks());
        taskListView.setItems(tasks);
    }

    @FXML
    private void onAddGoal() {
        openGoalEditDialog(null);
    }

    @FXML
    private void onUpdateGoal() {
        CommunalGoal selected = goalListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openGoalEditDialog(selected);
        }
    }

    @FXML
    private void onDeleteGoal() {
        CommunalGoal selected = goalListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Communal Goal");
            alert.setContentText("Are you sure you want to delete " + selected.getTitle() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                communalViewModel.deleteGoal(selected.getId());
                mainViewModel.saveAllData();
                loadGoals();
            }
        }
    }

    @FXML
    private void onCompleteGoal() {
        CommunalGoal selected = goalListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            communalViewModel.markGoalCompleted(selected.getId());
            mainViewModel.saveAllData();
            loadGoals();
        }
    }

    @FXML
    private void onToggleActive() {
        CommunalGoal selected = goalListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            communalViewModel.toggleGoalActive(selected.getId());
            mainViewModel.saveAllData();
            loadGoals();
        }
    }

    @FXML
    private void onAddTask() {
        CommunalGoal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null) {
            openTaskEditDialog(selectedGoal.getId(), null);
        }
    }

    @FXML
    private void onUpdateTask() {
        CommunalGoal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
        CommunalTask selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null && selectedTask != null) {
            openTaskEditDialog(selectedGoal.getId(), selectedTask);
        }
    }

    @FXML
    private void onDeleteTask() {
        CommunalGoal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
        CommunalTask selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null && selectedTask != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Task");
            alert.setContentText("Are you sure you want to delete " + selectedTask.getTitle() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                communalViewModel.deleteTask(selectedGoal.getId(), selectedTask.getId());
                mainViewModel.saveAllData();
                loadTasks(selectedGoal);
            }
        }
    }

    @FXML
    private void onCompleteTask() {
        CommunalGoal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
        CommunalTask selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null && selectedTask != null) {
            if (!selectedTask.hasParticipants()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot Complete");
                alert.setHeaderText("No Participants");
                alert.setContentText("This task cannot be completed without at least one participant. Please update the task and add participants first.");
                alert.showAndWait();
                return;
            }
            communalViewModel.markTaskCompleted(selectedGoal.getId(), selectedTask.getId());
            mainViewModel.saveAllData();
            loadGoals();
            loadTasks(communalViewModel.getGoalById(selectedGoal.getId()));
        }
    }

    @FXML
    private void onToggleTaskDisplay() {
        CommunalGoal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
        CommunalTask selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null && selectedTask != null) {
            selectedTask.setDisplayOnWebsite(!selectedTask.isDisplayOnWebsite());
            mainViewModel.saveAllData();
            loadTasks(selectedGoal);
        }
    }

    @FXML
    private void onOpenDefaultTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/DefaultTasksView.fxml"));
            Parent root = loader.load();
            DefaultTasksController controller = loader.getController();
            controller.setMainViewModel(mainViewModel);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Default Tasks");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openGoalEditDialog(CommunalGoal goal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/CommunalGoalEditDialog.fxml"));
            Parent root = loader.load();
            CommunalGoalEditController controller = loader.getController();
            controller.setCommunalViewModel(communalViewModel);
            controller.setGoal(goal);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(goal == null ? "Add Goal" : "Update Goal");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            mainViewModel.saveAllData();
            loadGoals();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openTaskEditDialog(int goalId, CommunalTask task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/CommunalTaskEditDialog.fxml"));
            Parent root = loader.load();
            CommunalTaskEditController controller = loader.getController();
            controller.setCommunalViewModel(communalViewModel);
            controller.setGoalId(goalId);
            controller.setTask(task);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(task == null ? "Add Task" : "Update Task");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            mainViewModel.saveAllData();
            CommunalGoal goal = communalViewModel.getGoalById(goalId);
            if (goal != null) {
                loadTasks(goal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
