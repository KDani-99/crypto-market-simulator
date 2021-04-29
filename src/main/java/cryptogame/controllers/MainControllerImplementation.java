package cryptogame.controllers;

import cryptogame.Main;
import cryptogame.controllers.main.NavbarController;
import cryptogame.services.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainControllerImplementation extends BaseController implements MainController {

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

    @Override
    public void initScene() {

        this.serviceHandler.getSceneManager()
                .getPrimaryStage()
                .setMinHeight(450);

        this.setNavbar();
        this.setUserInfo();

        this.setMarket(); // @test -> default
        //this.setStats();
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


        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void setMarket() {
        try {

            this.setEmpty();

            if(marketController == null) {
                marketController = serviceHandler.getSceneManager().getMarketComponentController();
                marketController.initialize();
            }

            hBox.getChildren().add(marketController.getRoot());

        } catch (Exception ex) {
            System.out.println("Error => "+ex.getMessage());
        }
    }
    @Override
    public void setBank() {
        try {

            this.setEmpty();

            var bankController = serviceHandler.getSceneManager().getBankController();

            hBox.getChildren().add(bankController.getRoot());
            bankController.initialize();


        } catch (Exception ex) {
            System.out.println("Error => "+ex.getMessage());
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

            hBox.getChildren().add(statsController.getRoot());

        } catch (Exception ex) {
            System.out.println("Error => "+ex.getMessage());
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
