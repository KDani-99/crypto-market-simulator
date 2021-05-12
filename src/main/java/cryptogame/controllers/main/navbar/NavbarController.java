package cryptogame.controllers.main.navbar;

import cryptogame.controllers.Controller;
import cryptogame.controllers.scene.SceneManager;
import cryptogame.model.services.Service;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavbarController implements Controller {

    private static final Logger logger = LogManager.getLogger(NavbarController.class);

    @FXML private VBox vBox;

    @FXML private Button marketButton;
    @FXML private Button settingsButton;
    @FXML private Button statsButton;
    @FXML private Button walletButton;
    @FXML private Button logOutButton;

    @FXML private Label loggedInUsernameLabel;
    @FXML private Label balanceLabel;

    @FXML private Pane marketButtonPane;
    @FXML private Pane statsButtonPane;
    @FXML private Pane walletButtonPane;

    private final Service serviceHandler;
    private final SceneManager sceneManager;

    @Autowired
    public NavbarController(Service serviceHandler, SceneManager sceneManager) {
        this.serviceHandler = serviceHandler;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize() {
        this.setupMarketButton();
        this.setupStatsButton();
        this.setupLogOutButton();
        this.setupWalletButton();

        this.vBox.setMaxHeight(Double.MAX_VALUE);
    }

    private void removeButtonStyles() {
        marketButtonPane.getStyleClass().remove("selected");
        statsButtonPane.getStyleClass().remove("selected");
        walletButtonPane.getStyleClass().remove("selected");
    }

    private void setupMarketButton() {
        this.marketButton.setOnMouseClicked(event -> {

            removeButtonStyles();
            marketButtonPane.getStyleClass().add("selected");

            sceneManager.getMainController()
                    .setMarket();
        });
    }

    private void setupLogOutButton() {
        this.logOutButton.setOnMouseClicked(event -> {

            serviceHandler.destroyActiveSession();
            serviceHandler.getMarketManager().stopAssetLoadingService();

            try {
                sceneManager.showLoginScene();
                logger.info("User logged out.");
            } catch (Exception exception) {
                logger.error(exception);
            }

        });
    }

    private void setupStatsButton() {
        this.statsButton.setOnMouseClicked(event -> {

            removeButtonStyles();
            statsButtonPane.getStyleClass().add("selected");

            sceneManager.getMainController()
            .setStats();
        });
    }

    private void setupWalletButton() {
        this.walletButton.setOnMouseClicked(event -> {

            removeButtonStyles();
            walletButtonPane.getStyleClass().add("selected");

            sceneManager.getMainController()
                    .setWallet();
        });
    }

    @Override
    public Node getRoot() {
        return this.vBox;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    public void setLoggedInUsernameLabelText(String text) {
        this.loggedInUsernameLabel.setText(text);
    }
    public void setBalanceLabelText(String text) {
        this.balanceLabel.setText(text);
    }
}
