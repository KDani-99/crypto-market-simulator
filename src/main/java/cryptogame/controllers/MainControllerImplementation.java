package cryptogame.controllers;

import cryptogame.Main;
import cryptogame.controllers.main.NavbarController;
import cryptogame.services.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.*;

@Component
public class MainControllerImplementation extends BaseController implements MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    private NavbarController navbarController;

    private Controller marketController;
    private Controller statsController;

    @FXML private BorderPane mainComponent;

    @FXML private HBox hBox;

    private final Service serviceHandler;

    @Autowired
    public MainControllerImplementation(Service serviceHandler) {

        super(true,1024,768);
        this.serviceHandler = serviceHandler;

    }

    private void setWindowProperties() {
        var primaryStage = this.serviceHandler.getSceneManager()
                .getPrimaryStage();

        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(450);
    }

    @Override
    public void initScene() {

        this.setWindowProperties();

        this.setNavbar();
        this.setUserInfo();

        this.setMarket();
    }

    private void setUserInfo() {

        var user = serviceHandler.getUserDao()
                .getEntity(serviceHandler.getSession().getActiveUserId());

        navbarController.setLoggedInUsernameLabelText(user.get().getUsername());
        navbarController.setBalanceLabelText(String.format("~$%.4f",user.get().getBalance()));
    }

    private void setEmpty() {
        if(this.hBox.getChildren().size() > 1)
            this.hBox.getChildren()
                    .remove(1);
    }

    private void setNavbar() {
        try {

            var navbarController = serviceHandler.getSceneManager().getNavbarController();
            hBox.getChildren().add(navbarController.getRoot());
            this.navbarController = (NavbarController) navbarController;
            this.navbarController.vBox.setMaxHeight(Double.MAX_VALUE);

        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    private void displayMainNode(Node node) {
        hBox.getChildren().add(node);
    }

    @Override
    public void setMarket() {
        try {

            this.setEmpty();

            if(marketController == null) {
                marketController = serviceHandler.getSceneManager().getMarketComponentController();
                marketController.initialize();
            }

           displayMainNode(marketController.getRoot());

        } catch (Exception exception) {
            logger.error(exception);
        }
    }
    @Override
    public void setBank() {
        try {

            this.setEmpty();

            var bankController = serviceHandler.getSceneManager().getBankController();
            bankController.initialize();

            displayMainNode(bankController.getRoot());

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
    @Override
    public void setStats() {
        try {

            this.setEmpty();

            if(statsController == null) {
                statsController = serviceHandler.getSceneManager().getStatsController();
                statsController.initialize();
            }

            displayMainNode(statsController.getRoot());

        } catch (Exception exception){
            logger.error(exception);
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void refreshUser() {
        this.setUserInfo();
    }
}
