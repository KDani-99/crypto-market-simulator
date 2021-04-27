package cryptogame.controllers;

import cryptogame.Main;
import cryptogame.controllers.main.HomeController;
import cryptogame.controllers.main.market.MarketController;
import cryptogame.controllers.main.NavbarController;
import cryptogame.services.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController extends BaseController {

    private NavbarController navbarController;
    private HomeController homeController;
    private MarketController marketController;

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

        this.serviceHandler.getSceneManager()
                .getPrimaryStage()
                .setMinHeight(450);

        this.setNavbar();

        this.setMarket(); // @test -> default
    }

    private void setNavbar() {
        try {

            var loader = new FXMLLoader(Main.class.getResource("/views/app/components/navbar/Navbar.fxml"));
            hBox.getChildren().add(loader.load());

            navbarController = loader.getController();

            navbarController.vBox.setMaxHeight(Double.MAX_VALUE);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void setHome() {
        try {

            var loader = new FXMLLoader(Main.class.getResource("/views/app/components/home/HomeView.fxml"));
            hBox.getChildren().add(loader.load());

            homeController = loader.getController();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void setMarket() {
        try {

            var marketController = serviceHandler.getSceneManager().getMarketComponentController();

            hBox.getChildren().add(marketController.getRoot());
            marketController.initialize();


        } catch (Exception ex) {
            System.out.println("Error => "+ex.getMessage());
        }
    }

    @Override
    public void showError(String message, String alertMessage) {

    }

    @Override
    public void hideError() {

    }

    @Override
    public void initialize() throws Exception {

    }
}
