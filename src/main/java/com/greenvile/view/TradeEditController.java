package com.greenvile.view;

import com.greenvile.model.Resident;
import com.greenvile.model.Trade;
import com.greenvile.viewmodel.TradeViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class TradeEditController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField pointsField;
    @FXML private TextField pictureField;
    @FXML private ComboBox<Resident> residentCombo;
    @FXML private Button saveButton;
    @FXML private Label errorLabel;

    private TradeViewModel tradeViewModel;
    private Trade trade;
    private boolean isNewTrade;

    public void setTradeViewModel(TradeViewModel tradeViewModel) {
        this.tradeViewModel = tradeViewModel;
        loadResidents();
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
        this.isNewTrade = (trade == null);

        if (!isNewTrade) {
            titleField.setText(trade.getTitle());
            descriptionArea.setText(trade.getDescription());
            pointsField.setText(String.valueOf(trade.getPointsCost()));
            pictureField.setText(trade.getPicturePath());

            for (Resident r : residentCombo.getItems()) {
                if (r.getId() == trade.getResidentId()) {
                    residentCombo.getSelectionModel().select(r);
                    break;
                }
            }
        }
    }

    @FXML
    private void initialize() {
        saveButton.setDisable(true);
        errorLabel.setText("");

        titleField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        pointsField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
        pictureField.textProperty().addListener((obs, oldVal, newVal) -> checkChanges());
    }

    private void loadResidents() {
        residentCombo.getItems().addAll(tradeViewModel.getAllResidents());
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

        int residentId = 0;
        Resident selectedResident = residentCombo.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            residentId = selectedResident.getId();
        }

        if (isNewTrade) {
            Trade newTrade = new Trade(
                0,
                titleField.getText().trim(),
                descriptionArea.getText().trim(),
                pictureField.getText().trim(),
                points,
                residentId
            );
            tradeViewModel.addTrade(newTrade);
        } else {
            trade.setTitle(titleField.getText().trim());
            trade.setDescription(descriptionArea.getText().trim());
            trade.setPointsCost(points);
            trade.setPicturePath(pictureField.getText().trim());
            trade.setResidentId(residentId);
            tradeViewModel.updateTrade(trade);
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
