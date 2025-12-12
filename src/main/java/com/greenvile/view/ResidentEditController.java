package com.greenvile.view;

import com.greenvile.model.Resident;
import com.greenvile.viewmodel.ResidentViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ResidentEditController {
    @FXML private TextField fullNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private Button saveButton;
    @FXML private Label errorLabel;

    private ResidentViewModel residentViewModel;
    private Resident resident;
    private boolean isNewResident;

    public void setResidentViewModel(ResidentViewModel residentViewModel) {
        this.residentViewModel = residentViewModel;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
        this.isNewResident = (resident == null);

        if (!isNewResident) {
            fullNameField.setText(resident.getFullName());
            phoneField.setText(resident.getPhoneNumber());
            emailField.setText(resident.getEmail());
            addressField.setText(resident.getAddress());
        }
    }

    @FXML
    private void initialize() {
        saveButton.setDisable(true);
        errorLabel.setText("");

        fullNameField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        addressField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
    }

    private void checkChanges() {
        boolean nameValid = !fullNameField.getText().trim().isEmpty();
        boolean addressValid = !addressField.getText().trim().isEmpty();
        
        if (!nameValid || !addressValid) {
            errorLabel.setText("Name and Address are required");
            saveButton.setDisable(true);
            return;
        }
        
        errorLabel.setText("");
        
        if (isNewResident) {
            saveButton.setDisable(false);
        } else {
            boolean changed = !fullNameField.getText().equals(resident.getFullName()) ||
                !phoneField.getText().equals(resident.getPhoneNumber()) ||
                !emailField.getText().equals(resident.getEmail()) ||
                !addressField.getText().equals(resident.getAddress());
            saveButton.setDisable(!changed);
        }
    }

    @FXML
    private void onSave() {
        if (isNewResident) {
            Resident newResident = new Resident(
                0,
                fullNameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                addressField.getText().trim(),
                0
            );
            residentViewModel.addResident(newResident);
        } else {
            resident.setFullName(fullNameField.getText().trim());
            resident.setPhoneNumber(phoneField.getText().trim());
            resident.setEmail(emailField.getText().trim());
            resident.setAddress(addressField.getText().trim());
            residentViewModel.updateResident(resident);
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
