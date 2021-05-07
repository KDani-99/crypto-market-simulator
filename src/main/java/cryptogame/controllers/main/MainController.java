package cryptogame.controllers.main;

import cryptogame.controllers.BaseController;
import cryptogame.common.interfaces.Refreshable;
import cryptogame.controllers.WindowController;
import cryptogame.controllers.main.navbar.NavbarController;
import cryptogame.model.services.Service;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.*;

@Component
public class MainController extends BaseController implements WindowController, Refreshable {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    private NavbarController navbarController;

    @FXML private BorderPane mainComponent;

    @FXML private HBox hBox;

    private final Service serviceHandler;

    @Autowired
    public MainController(Service serviceHandler) {
        super(true,1024,768);
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initScene() {
        this.setNavbar();
        this.setUserInfo();
        this.setMarket();
    }

    private void setUserInfo() {

        var user = serviceHandler.getUserDao()
                .getEntity(serviceHandler.getSession().getActiveUserId()).get();

        navbarController.setLoggedInUsernameLabelText(user.getUsername());
        navbarController.setBalanceLabelText(String.format("$%s",serviceHandler.formatNumber(user.getBalance())));
    }

    private void setEmpty() {
        if(this.hBox.getChildren().size() > 1)
            this.hBox.getChildren()
                    .remove(1);
    }

    private void setNavbar() {
        try {

            var navbarController = serviceHandler.getSceneManager().getNavbarController();
            this.hBox.getChildren().add(navbarController.getRoot());
            this.navbarController = (NavbarController) navbarController;
            this.navbarController.initialize();

        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    private void displayMainNode(Node node) {
        hBox.getChildren().add(node);
    }

    public void setMarket() {
        try {

            this.setEmpty();

            var marketController = serviceHandler.getSceneManager().getMarketComponentController();
            marketController.initialize();
            displayMainNode(marketController.getRoot());

        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    public void setStats() {
        try {

            this.setEmpty();

            var statsController = serviceHandler.getSceneManager().getStatsController();
            statsController.initialize();

            displayMainNode(statsController.getRoot());

        } catch (Exception exception){
            logger.error(exception);
        }
    }

    public void setWallet() {
        try {
            this.setEmpty();

            var walletController = serviceHandler.getSceneManager().getWalletController();
            walletController.initialize();

            displayMainNode(walletController.getRoot());

        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    @Override
    public void refresh() {
        this.setUserInfo();
    }

}
