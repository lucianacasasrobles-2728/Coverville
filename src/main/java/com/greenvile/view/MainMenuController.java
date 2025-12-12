/* a bunch of liseners, it looks for button presses. Basically alsmost every
class in the view layer is the same
when writing the project we should spend least ammount of time on this 
Appart from saying that everything was dumbed the fuck down for bob to understand. Each class just
Listens for a bunch of button presses and calls them and boom you are technically no longer in any of the clases */

package com.greenvile.view;

import com.greenvile.viewmodel.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {
    private MainViewModel mainViewModel;

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @FXML
    private void openResidents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/ResidentView.fxml"));
            Parent root = loader.load();
            ResidentController controller = loader.getController();
            controller.setMainViewModel(mainViewModel);
            Stage stage = new Stage();
            stage.setTitle("Residents");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openGreenActions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/GreenActionView.fxml"));
            Parent root = loader.load();
            GreenActionController controller = loader.getController();
            controller.setMainViewModel(mainViewModel);
            Stage stage = new Stage();
            stage.setTitle("Green Actions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCommunal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/CommunalView.fxml"));
            Parent root = loader.load();
            CommunalController controller = loader.getController();
            controller.setMainViewModel(mainViewModel);
            Stage stage = new Stage();
            stage.setTitle("Communal Tasks");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTrades() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenvile/fxml/TradeView.fxml"));
            Parent root = loader.load();
            TradeController controller = loader.getController();
            controller.setMainViewModel(mainViewModel);
            Stage stage = new Stage();
            stage.setTitle("Trades");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
