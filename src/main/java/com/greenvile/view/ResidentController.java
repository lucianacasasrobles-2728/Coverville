package com.greenvile.view;

import com.greenvile.model.Resident;
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

public class ResidentController {
    @FXML private ListView<Resident> residentListView;
    @FXML private TextField searchField;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private MainViewModel mainViewModel;
    private ResidentViewModel residentViewModel;

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        this.residentViewModel = new ResidentViewModel(mainViewModel.getDataManager());
        loadResidents();
    }

    @FXML
    private void initialize() {
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        residentListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            updateButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterResidents(newVal);
        });
    }

    private void loadResidents() {
        ObservableList<Resident> residents = FXCollections.observableArrayList(residentViewModel.getResidentList());
        residentListView.setItems(residents);
    }

    private void filterResidents(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadResidents();
        } else {
            ObservableList<Resident> filtered = FXCollections.observableArrayList();
            for (Resident r : residentViewModel.getResidentList()) {
                if (r.getFullName().toLowerCase().contains(searchText.toLowerCase())) {
                    filtered.add(r);
                }
            }
            residentListView.setItems(filtered);
        }
    }

    @FXML
    private void onAdd() {
        openEditDialog(null);
    }

    @FXML
    private void onUpdate() {
        Resident selected = residentListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditDialog(selected);
        }
    }

    @FXML
    private void onDelete() {
        Resident selected = residentListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Resident");
            alert.setContentText("Are you sure you want to delete " + selected.getFullName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                residentViewModel.deleteResident(selected.getId());
                mainViewModel.saveAllData();
                loadResidents();
            }
        }
    }

    private void openEditDialog(Resident resident) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/ResidentEditDialog.fxml"));
            Parent root = loader.load();
            ResidentEditController controller = loader.getController();
            controller.setResidentViewModel(residentViewModel);
            controller.setResident(resident);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(resident == null ? "Add Resident" : "Update Resident");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            mainViewModel.saveAllData();
            loadResidents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
