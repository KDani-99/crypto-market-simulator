package cryptogame.controllers.main.navbar;

import cryptogame.controllers.Controller;
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
    @FXML private Button logOutButton;

    @FXML private Label loggedInUsernameLabel;
    @FXML private Label balanceLabel;

    @FXML private Pane marketButtonPane;
    @FXML private Pane statsButtonPane;

    private final Service serviceHandler;

    @Autowired
    public NavbarController(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initialize() {
        this.setupMarketButton();
        this.setupStatsButton();
        this.setupLogOutButton();

        this.vBox.setMaxHeight(Double.MAX_VALUE);
    }

    private void setupMarketButton() {
        this.marketButton.setOnMouseClicked(event -> {

            removeButtonStyles();
            marketButtonPane.getStyleClass().add("selected");

            serviceHandler.getSceneManager().getMainController()
                    .setMarket();
        });
    }
    private void setupLogOutButton() {
        this.logOutButton.setOnMouseClicked(event -> {

            serviceHandler.destroyActiveSession();
            serviceHandler.getMarketManager().stopAssetLoadingService();

            try {
                serviceHandler.getSceneManager().showLoginScene();
                logger.info("User logged out.");
            } catch (Exception exception) {
                logger.error(exception);
            }

        });
    }

    private void removeButtonStyles() {
        marketButtonPane.getStyleClass().remove("selected");
        statsButtonPane.getStyleClass().remove("selected");
    }

    private void setupStatsButton() {
        this.statsButton.setOnMouseClicked(event -> {

            removeButtonStyles();
            statsButtonPane.getStyleClass().add("selected");

            serviceHandler.getSceneManager().getMainController()
            .setStats();
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
