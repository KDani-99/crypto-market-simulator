package cryptogame.service.manager.scene;

import cryptogame.Main;
import cryptogame.controller.IController;
import cryptogame.controller.LoginController;
import cryptogame.controller.MainController;
import cryptogame.controller.RegistrationController;
import cryptogame.service.ServiceHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;

public class DefaultSceneManager implements SceneManager {

    private final HashMap<Class<?>, URL> controllerCollection;

    private final ServiceHandler services;
    private final Stage primaryStage;

    public DefaultSceneManager(Stage primaryStage, ServiceHandler services) throws Exception{
        this.primaryStage = primaryStage;
        this.services = services;

        this.controllerCollection = new HashMap<>();

        this.addControllers();

    }

    private void loadDefaultScene() throws Exception{
        this.showScene(MainController.class);
    }

    private URL getResourceURL(String path) {
        return Main.class.getResource(path);
    }

    private void addControllers() {
        this.controllerCollection.put(MainController.class,getResourceURL("/views/app/AppView.fxml"));
        this.controllerCollection.put(LoginController.class, getResourceURL("/views/login/LoginView.fxml"));
        this.controllerCollection.put(RegistrationController.class,getResourceURL("/views/registration/RegistrationView.fxml"));
    }

    private <T extends IController> IController loadController(Class<T> controller) throws Exception {
        var resourceUrl = controllerCollection.get(controller);

        var loader = new FXMLLoader(resourceUrl);
        var controllerInstance = (IController) services.injectDependencies(controller);
        loader.setController(controllerInstance);

        var view = (Parent)loader.load();
        controllerInstance.createScene(view);
        // controllerInstance.setVersionLabelText("v"+appProperties.getProperty("version"));
        controllerInstance.initScene();

        return controllerInstance;
    }

    public <T extends IController> void showScene(Class<T> controllerClass) throws Exception {
        var controllerInstance = loadController(controllerClass);

        primaryStage.setScene(controllerInstance.getScene());
        primaryStage.setResizable(controllerInstance.isResizable());
        primaryStage.show();
    }
    @Override
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    @Override
    public void initialize() throws Exception {
        this.loadDefaultScene();
    }
}
