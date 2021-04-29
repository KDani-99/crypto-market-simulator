package cryptogame.controllers.main;

import cryptogame.controllers.Controller;
import cryptogame.services.Service;
import cryptogame.services.auth.AuthService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavbarController implements Controller {

    @FXML public VBox vBox;

    @FXML private Button marketButton;
    @FXML private Button settingsButton;
    @FXML private Button statsButton;

    @FXML private Label loggedInUsernameLabel;
    @FXML private Label balanceLabel;

    @FXML private Pane marketButtonPane;
    @FXML private Pane statsButtonPane;

    private boolean initialized = false;

    private final Service serviceHandler;

    @Autowired
    public NavbarController(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initialize() {
        if(initialized) return;

        this.setupMarketButton();
        this.setupStatsButton();

        initialized = true;
    }

    private void setupMarketButton() {
        this.marketButton.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {

                removeButtonStyles();
                marketButtonPane.getStyleClass().add("selected");

                serviceHandler.getSceneManager().getMainController()
                        .setMarket();
            }
        });
    }
    private void setupSettingsButton() {
        this.settingsButton.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
    }

    private void removeButtonStyles() {
        marketButtonPane.getStyleClass().remove("selected");
        statsButtonPane.getStyleClass().remove("selected");
    }

    private void setupStatsButton() {
        this.statsButton.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {

                removeButtonStyles();
                statsButtonPane.getStyleClass().add("selected");

                serviceHandler.getSceneManager().getMainController()
                .setStats();
            }
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
