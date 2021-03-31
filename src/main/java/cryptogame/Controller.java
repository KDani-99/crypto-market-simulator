package cryptogame;

import cryptogame.controller.*;
import cryptogame.model.session.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Controller {

    private final SessionManager sessionManager;
    private final Stage stage;
    private final HashMap<String, Scene> scenes;

    private LoginController loginController;
    private RegistrationController registrationController;

    public Controller(Stage stage,SessionManager sessionManager){
        this.sessionManager = sessionManager;
        this.scenes = new HashMap<>();
        this.stage = stage;

        this.stage.setTitle("Crypto Trading Game");
    }

    private void registerMain(Scene main) {
        this.stage.setScene(main);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void setupScenes() throws IOException {
        Parent loginView = FXMLLoader.load(Main.class.getResource("/views/login/LoginView.fxml"));
        var loginScene = new Scene(loginView,400,500);
        this.registerMain(loginScene);
        this.scenes.put(LoginController.SCENE_NAME,loginScene);

        Parent registrationView = FXMLLoader.load(Main.class.getResource("/views/registration/RegistrationView.fxml"));
        this.scenes.put(RegistrationController.SCENE_NAME,new Scene(registrationView,400,500));

    }

    public void registerControllers() {
        this.loginController = new LoginController(this.stage,scenes,this.sessionManager);
        this.registrationController = new RegistrationController(this.stage,scenes,this.sessionManager);
    }
}
