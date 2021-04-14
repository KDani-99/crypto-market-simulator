package cryptogame.controller;

import cryptogame.Main;
import cryptogame.controller.main.HomeController;
import cryptogame.controller.main.NavbarController;
import cryptogame.model.services.managers.ISceneManager;
import cryptogame.model.services.managers.SceneManager;
import cryptogame.model.services.session.ISession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainController extends BaseController {

    private NavbarController navbarController;
    private HomeController homeController;

    @FXML private BorderPane mainComponent;

    public MainController(ISession sessionManager, ISceneManager sceneManager) {super(sessionManager,sceneManager,true,1024,768);}

    @Override
    public void initScene() {

        this.sceneManager.getPrimaryStage()
                .setMinHeight(450);

        this.setNavbar();
        this.setHome();
    }

    private void setNavbar() {
        try {

            var loader = new FXMLLoader(Main.class.getResource("/views/app/components/navbar/Navbar.fxml"));

            mainComponent.setLeft(loader.load());
            navbarController = loader.getController();

            navbarController.vBox.setMaxHeight(Double.MAX_VALUE);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void setHome() {
        try {

            var loader = new FXMLLoader(Main.class.getResource("/views/app/components/home/HomeView.fxml"));

            mainComponent.setCenter(loader.load());
            homeController = loader.getController();

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
