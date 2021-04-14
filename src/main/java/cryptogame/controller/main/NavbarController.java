package cryptogame.controller.main;

import cryptogame.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NavbarController {

    @FXML public VBox vBox;

    @FXML private Button homeButton;
    @FXML private Button marketButton;
    @FXML private Button settingsButton;
    @FXML private Button statsButton;

    @FXML private Label loggedInUsernameLabel;
    @FXML private Label balanceLabel;

}
