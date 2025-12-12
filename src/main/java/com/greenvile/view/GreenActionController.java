package com.greenvile.view;

import com.greenvile.model.GreenAction;
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

public class GreenActionController {
    @FXML private ListView<GreenAction> actionListView;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button toggleDisplayButton;

    private MainViewModel mainViewModel;
    private GreenActionViewModel greenActionViewModel;
    private ObservableList<GreenAction> actionList;

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        this.greenActionViewModel = new GreenActionViewModel(mainViewModel.getDataManager());
        loadActions();
    }

    @FXML
    private void initialize() {
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        toggleDisplayButton.setDisable(true);

        actionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            updateButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
            toggleDisplayButton.setDisable(!hasSelection);
        });
    }

    private void loadActions() {
        actionList = FXCollections.observableArrayList(greenActionViewModel.getActionList());
        actionListView.setItems(actionList);
    }

    @FXML
    private void onAdd() {
        openEditDialog(null);
    }

    @FXML
    private void onUpdate() {
        GreenAction selected = actionListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditDialog(selected);
        }
    }

    @FXML
    private void onDelete() {
        GreenAction selected = actionListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Green Action");
            alert.setContentText("Are you sure you want to delete " + selected.getTitle() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                greenActionViewModel.deleteAction(selected.getId());
                mainViewModel.saveAllData();
                loadActions();
            }
        }
    }

    @FXML
    private void onToggleDisplay() {
        GreenAction selected = actionListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            greenActionViewModel.toggleWebsiteDisplay(selected.getId());
            mainViewModel.saveAllData();
            loadActions();
        }
    }

    private void openEditDialog(GreenAction action) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/GreenActionEditDialog.fxml"));
            Parent root = loader.load();
            GreenActionEditController controller = loader.getController();
            controller.setGreenActionViewModel(greenActionViewModel);
            controller.setAction(action);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(action == null ? "Add Green Action" : "Update Green Action");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            mainViewModel.saveAllData();
            loadActions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
