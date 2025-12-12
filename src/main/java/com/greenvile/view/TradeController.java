package com.greenvile.view;

import com.greenvile.model.Resident;
import com.greenvile.model.Trade;
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

public class TradeController {
    @FXML private ListView<Trade> tradeListView;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button completeButton;
    @FXML private Button toggleDisplayButton;

    private MainViewModel mainViewModel;
    private TradeViewModel tradeViewModel;

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        this.tradeViewModel = new TradeViewModel(mainViewModel.getDataManager());
        loadTrades();
    }

    @FXML
    private void initialize() {
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        completeButton.setDisable(true);
        toggleDisplayButton.setDisable(true);

        tradeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            updateButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
            completeButton.setDisable(!hasSelection || (newVal != null && newVal.isCompleted()));
            toggleDisplayButton.setDisable(!hasSelection);
        });
    }

    private void loadTrades() {
        ObservableList<Trade> trades = FXCollections.observableArrayList(tradeViewModel.getTradeList());
        tradeListView.setItems(trades);
    }

    @FXML
    private void onAdd() {
        openEditDialog(null);
    }

    @FXML
    private void onUpdate() {
        Trade selected = tradeListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditDialog(selected);
        }
    }

    @FXML
    private void onDelete() {
        Trade selected = tradeListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Trade");
            alert.setContentText("Are you sure you want to delete " + selected.getTitle() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                tradeViewModel.deleteTrade(selected.getId());
                mainViewModel.saveAllData();
                loadTrades();
            }
        }
    }

    @FXML
    private void onComplete() {
        Trade selected = tradeListView.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.isCompleted()) {
            Dialog<Resident> dialog = new Dialog<>();
            dialog.setTitle("Select Buyer");
            dialog.setHeaderText("Select who is buying this trade\nCost: " + selected.getPointsCost() + " points");

            ButtonType confirmButtonType = new ButtonType("Complete", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            ComboBox<Resident> buyerCombo = new ComboBox<>();
            buyerCombo.getItems().addAll(tradeViewModel.getAllResidents());
            buyerCombo.setPromptText("Select buyer...");

            Label errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: red;");

            javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
            content.getChildren().addAll(new Label("Buyer:"), buyerCombo, errorLabel);
            dialog.getDialogPane().setContent(content);

            Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
            confirmButton.setDisable(true);

            buyerCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    if (newVal.getPersonalPoints() < selected.getPointsCost()) {
                        errorLabel.setText("Not enough points! Has: " + newVal.getPersonalPoints());
                        confirmButton.setDisable(true);
                    } else if (newVal.getId() == selected.getResidentId()) {
                        errorLabel.setText("Buyer cannot be the seller!");
                        confirmButton.setDisable(true);
                    } else {
                        errorLabel.setText("");
                        confirmButton.setDisable(false);
                    }
                } else {
                    confirmButton.setDisable(true);
                }
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    return buyerCombo.getValue();
                }
                return null;
            });

            Optional<Resident> result = dialog.showAndWait();
            result.ifPresent(buyer -> {
                boolean success = tradeViewModel.completeTrade(selected.getId(), buyer.getId());
                if (success) {
                    mainViewModel.saveAllData();
                    loadTrades();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Trade Complete");
                    successAlert.setHeaderText("Trade completed successfully!");
                    successAlert.setContentText(buyer.getFullName() + " bought " + selected.getTitle() + " for " + selected.getPointsCost() + " points.");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Trade failed");
                    errorAlert.setContentText("Could not complete the trade.");
                    errorAlert.showAndWait();
                }
            });
        }
    }

    @FXML
    private void onToggleDisplay() {
        Trade selected = tradeListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tradeViewModel.toggleWebsiteDisplay(selected.getId());
            mainViewModel.saveAllData();
            loadTrades();
        }
    }

    private void openEditDialog(Trade trade) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/TradeEditDialog.fxml"));
            Parent root = loader.load();
            TradeEditController controller = loader.getController();
            controller.setTradeViewModel(tradeViewModel);
            controller.setTrade(trade);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(trade == null ? "Add Trade" : "Update Trade");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            mainViewModel.saveAllData();
            loadTrades();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
