package cryptogame.controller;

import cryptogame.Main;
import cryptogame.controller.main.NavbarController;
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

    @FXML private BorderPane mainComponent;

    @Override
    public void initScene() {
        this.setNavbar();
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

    @Override
    protected void showError(String message, String alertMessage) {

    }

    @Override
    protected void hideError() {

    }
}
