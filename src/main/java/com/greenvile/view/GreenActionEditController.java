package com.greenvile.view;

import com.greenvile.model.GreenAction;
import com.greenvile.model.Resident;
import com.greenvile.viewmodel.GreenActionViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class GreenActionEditController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField pointsField;
    @FXML private TextField pictureField;
    @FXML private ComboBox<Resident> participantCombo;
    @FXML private Button saveButton;
    @FXML private Label errorLabel;

    private GreenActionViewModel greenActionViewModel;
    private GreenAction action;
    private boolean isNewAction;

    public void setGreenActionViewModel(GreenActionViewModel greenActionViewModel) {
        this.greenActionViewModel = greenActionViewModel;
        loadResidents();
    }

    public void setAction(GreenAction action) {
        this.action = action;
        this.isNewAction = (action == null);

        if (!isNewAction) {
            titleField.setText(action.getTitle());
            descriptionArea.setText(action.getDescription());
            pointsField.setText(String.valueOf(action.getPointsAwarded()));
            pictureField.setText(action.getPicturePath());

            for (Resident r : participantCombo.getItems()) {
                if (r.getId() == action.getParticipantId()) {
                    participantCombo.getSelectionModel().select(r);
                    break;
                }
            }
        }
    }

    @FXML
    private void initialize() {
        saveButton.setDisable(true);
        errorLabel.setText("");

        pointsField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                pointsField.setText(oldVal);
            }
            checkChanges();
        });

        titleField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        pictureField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
    }

    private void loadResidents() {
        participantCombo.getItems().addAll(greenActionViewModel.getAllResidents());
    }

    private void checkChanges() {
        boolean titleValid = !titleField.getText().trim().isEmpty();
        boolean descValid = !descriptionArea.getText().trim().isEmpty();
        boolean pointsValid = false;
        
        try {
            int pts = Integer.parseInt(pointsField.getText().trim());
            pointsValid = pts > 0;
        } catch (NumberFormatException e) {
            pointsValid = false;
        }
        
        if (!titleValid || !descValid || !pointsValid) {
            errorLabel.setText("Title, Description, and Points (>0) are required");
            saveButton.setDisable(true);
            return;
        }
        
        errorLabel.setText("");
        saveButton.setDisable(false);
    }

    @FXML
    private void onBrowsePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(pictureField.getScene().getWindow());
        if (file != null) {
            pictureField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void onSave() {
        int points = Integer.parseInt(pointsField.getText().trim());

        int participantId = 0;
        Resident selectedResident = participantCombo.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            participantId = selectedResident.getId();
        }

        if (isNewAction) {
            GreenAction newAction = new GreenAction(
                0,
                titleField.getText().trim(),
                descriptionArea.getText().trim(),
                pictureField.getText().trim(),
                points
            );
            newAction.setParticipantId(participantId);
            greenActionViewModel.addAction(newAction);
        } else {
            action.setTitle(titleField.getText().trim());
            action.setDescription(descriptionArea.getText().trim());
            action.setPointsAwarded(points);
            action.setPicturePath(pictureField.getText().trim());
            action.setParticipantId(participantId);
            greenActionViewModel.updateAction(action);
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
