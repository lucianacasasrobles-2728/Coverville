package com.greenvile.view;

import com.greenvile.model.CommunalGoal;
import com.greenvile.viewmodel.CommunalViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CommunalGoalEditController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField pointsNeededField;
    @FXML private Button saveButton;
    @FXML private Label errorLabel;

    private CommunalViewModel communalViewModel;
    private CommunalGoal goal;
    private boolean isNewGoal;

    public void setCommunalViewModel(CommunalViewModel communalViewModel) {
        this.communalViewModel = communalViewModel;
    }

    public void setGoal(CommunalGoal goal) {
        this.goal = goal;
        this.isNewGoal = (goal == null);

        if (!isNewGoal) {
            titleField.setText(goal.getTitle());
            descriptionArea.setText(goal.getDescription());
            pointsNeededField.setText(String.valueOf(goal.getPointsNeeded()));
        }
    }

    @FXML
    private void initialize() {
        saveButton.setDisable(true);
        errorLabel.setText("");

        titleField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        pointsNeededField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
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
        int pointsNeeded = 0;
        try {
            pointsNeeded = Integer.parseInt(pointsNeededField.getText());
        } catch (NumberFormatException e) {
            pointsNeeded = 0;
        }

        if (isNewGoal) {
            CommunalGoal newGoal = new CommunalGoal(
                0,
                titleField.getText().trim(),
                descriptionArea.getText().trim(),
                pointsNeeded
            );
            communalViewModel.addGoal(newGoal);
        } else {
            goal.setTitle(titleField.getText().trim());
            goal.setDescription(descriptionArea.getText().trim());
            goal.setPointsNeeded(pointsNeeded);
            communalViewModel.updateGoal(goal);
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
