package cryptogame.controller;

import cryptogame.Main;
import cryptogame.controller.main.HomeController;
import cryptogame.controller.main.market.MarketController;
import cryptogame.controller.main.NavbarController;
import cryptogame.service.manager.scene.SceneManager;
import cryptogame.model.services.session.ISession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class MainController extends BaseController {

    private NavbarController navbarController;
    private HomeController homeController;
    private MarketController marketController;

    @FXML private BorderPane mainComponent;

    @FXML private HBox hBox;

    public MainController(ISession sessionManager, SceneManager sceneManager) {super(sessionManager,sceneManager,"/views/app/AppView.fxml",true,1024,768);}

    @Override
    public void initScene() {

        this.sceneManager.getPrimaryStage()
                .setMinHeight(450);

        this.setNavbar();
      //  this.setHome();
        this.setMarket(); // @test
    }

    private void setNavbar() {
        try {

            var loader = new FXMLLoader(Main.class.getResource("/views/app/components/navbar/Navbar.fxml"));
            hBox.getChildren().add(loader.load());
           // mainComponent.setLeft(loader.load());
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

           // mainComponent.setCenter(loader.load());
            homeController = loader.getController();

           // splitPane.

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void setMarket() {
        try {

            var loader = new FXMLLoader(Main.class.getResource("/views/app/components/market/MarketView.fxml"));
            Node n = loader.load();
            n.prefWidth(Double.MAX_VALUE);

            hBox.getChildren().add(n);
            marketController = loader.getController();

            marketController.initialize();


        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void showError(String message, String alertMessage) {

    }

    @Override
    public void hideError() {

    }
}
