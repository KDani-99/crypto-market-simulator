package cryptogame.controllers.main;

import cryptogame.controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NavbarController implements Controller {

    @FXML public VBox vBox;

    @FXML private Button homeButton;
    @FXML private Button marketButton;
    @FXML private Button settingsButton;
    @FXML private Button statsButton;

    @FXML private Label loggedInUsernameLabel;
    @FXML private Label balanceLabel;

    @Override
    public void initialize() throws Exception {

    }

    @Override
    public Node getRoot() {
        return this.vBox;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

}
