package cryptogame;

import cryptogame.controller.*;
import cryptogame.model.session.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Controller {

    private final Properties appProperties;
    private final SessionManager sessionManager;
    private final Stage stage;

    private final Map<Class<? extends BaseController>, BaseController> controllers = new HashMap<>(){{
        put(RegistrationController.class,null);
        put(LoginController.class,null);
    }};

    private LoginController loginController;
    private RegistrationController registrationController;

    public Controller(Properties appProperties,Stage stage,SessionManager sessionManager){
        this.appProperties = appProperties;
        this.sessionManager = sessionManager;
        this.stage = stage;

        this.stage.setTitle("Crypto Trading Game");
    }

    private void registerMain(Scene main) {
        this.stage.setScene(main);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void setupScenes() throws IOException {

        // DI context object to access model? as a parameter for loadController? idk
        loadController("/views/login/LoginView.fxml",400,500);
        loadController("/views/registration/RegistrationView.fxml",400,500);

        this.registerMain(this.controllers.get(RegistrationController.class).getScene());
    }

    private void loadController(String path, int width, int height) throws IOException {
        var loader = new FXMLLoader(Main.class.getResource(path));
        Parent view = loader.load();
        BaseController controller = loader.getController();
        Scene scene = new Scene(view,width,height);

        controller.setPrimaryStage(this.stage);
        controller.setScene(scene);
        controller.setControllers(this.controllers);
        controller.setSessionManager(this.sessionManager);

        controller.setVersionLabelText("v"+appProperties.getProperty("version"));

        controller.initScene();

        this.controllers.replace(controller.getClass(),controller);
    }
}
