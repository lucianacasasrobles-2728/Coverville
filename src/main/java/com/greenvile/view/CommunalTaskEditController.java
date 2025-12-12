package com.greenvile.view;

import com.greenvile.model.CommunalTask;
import com.greenvile.model.Resident;
import com.greenvile.viewmodel.CommunalViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class CommunalTaskEditController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField pointsField;
    @FXML private ListView<Resident> participantListView;
    @FXML private Button saveButton;
    @FXML private Label errorLabel;

    private CommunalViewModel communalViewModel;
    private int goalId;
    private CommunalTask task;
    private boolean isNewTask;

    public void setCommunalViewModel(CommunalViewModel communalViewModel) {
        this.communalViewModel = communalViewModel;
        loadResidents();
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public void setTask(CommunalTask task) {
        this.task = task;
        this.isNewTask = (task == null);

        if (!isNewTask) {
            titleField.setText(task.getTitle());
            descriptionArea.setText(task.getDescription());
            pointsField.setText(String.valueOf(task.getPointsAwarded()));

            for (int i = 0; i < participantListView.getItems().size(); i++) {
                Resident r = participantListView.getItems().get(i);
                if (task.getParticipantIds().contains(r.getId())) {
                    participantListView.getSelectionModel().select(i);
                }
            }
        }
    }

    @FXML
    private void initialize() {
        participantListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        saveButton.setDisable(true);
        errorLabel.setText("");

        titleField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        pointsField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
    }

    private void loadResidents() {
        participantListView.getItems().addAll(communalViewModel.getAllResidents());
    }

    private void checkChanges() {
        boolean titleValid = !titleField.getText().trim().isEmpty();
        boolean descValid = !descriptionArea.getText().trim().isEmpty();
        
        if (!titleValid || !descValid) {
            errorLabel.setText("Title and Description are required");
            saveButton.setDisable(true);
            return;
        }
        
        errorLabel.setText("");
        saveButton.setDisable(false);
    }

    @FXML
    private void onSave() {
        int points = 0;
        try {
            points = Integer.parseInt(pointsField.getText());
        } catch (NumberFormatException e) {
            points = 0;
        }

        List<Integer> selectedParticipants = new ArrayList<>();
        for (Resident r : participantListView.getSelectionModel().getSelectedItems()) {
            selectedParticipants.add(r.getId());
        }

        if (isNewTask) {
            CommunalTask newTask = new CommunalTask(
                0,
                titleField.getText().trim(),
                descriptionArea.getText().trim(),
                points
            );
            newTask.setParticipantIds(selectedParticipants);
            communalViewModel.addTaskToGoal(goalId, newTask);
        } else {
            task.setTitle(titleField.getText().trim());
            task.setDescription(descriptionArea.getText().trim());
            task.setPointsAwarded(points);
            task.setParticipantIds(selectedParticipants);
            communalViewModel.updateTask(goalId, task);
        }

        closeWindow();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
