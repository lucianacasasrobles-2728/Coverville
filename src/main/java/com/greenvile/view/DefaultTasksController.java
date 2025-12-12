package com.greenvile.view;

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

public class DefaultTasksController {
    @FXML private ListView<CommunalTask> taskListView;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private MainViewModel mainViewModel;
    private CommunalViewModel communalViewModel;

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        this.communalViewModel = new CommunalViewModel(mainViewModel.getDataManager());
        loadTasks();
    }

    @FXML
    private void initialize() {
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            updateButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
        });
    }

    private void loadTasks() {
        ObservableList<CommunalTask> tasks = FXCollections.observableArrayList(communalViewModel.getDefaultTasks());
        taskListView.setItems(tasks);
    }

    @FXML
    private void onAdd() {
        openEditDialog(null);
    }

    @FXML
    private void onUpdate() {
        CommunalTask selected = taskListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditDialog(selected);
        }
    }

    @FXML
    private void onDelete() {
        CommunalTask selected = taskListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Default Task");
            alert.setContentText("Are you sure you want to delete " + selected.getTitle() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                communalViewModel.deleteDefaultTask(selected.getId());
                mainViewModel.saveAllData();
                loadTasks();
            }
        }
    }

    private void openEditDialog(CommunalTask task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/DefaultTaskEditDialog.fxml"));
            Parent root = loader.load();
            DefaultTaskEditController controller = loader.getController();
            controller.setCommunalViewModel(communalViewModel);
            controller.setTask(task);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(task == null ? "Add Default Task" : "Update Default Task");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            mainViewModel.saveAllData();
            loadTasks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
