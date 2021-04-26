package cryptogame.services.manager.scene;

import cryptogame.Main;
import cryptogame.controllers.*;
import cryptogame.controllers.main.market.MarketController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashMap;

@Component("sceneManager")
public class DefaultSceneManager implements SceneManager {

    private final ApplicationContext context;

    private final HashMap<Class<?>, URL> controllerCollection;
    private final HashMap<Class<?>, URL> mainControllerComponents;

    //private final ServiceHandler services;
    private final Stage primaryStage;

    @Autowired
    public DefaultSceneManager(ApplicationContext context, Stage primaryStage) {
        this.context = context;
        this.primaryStage = primaryStage;
        this.controllerCollection = new HashMap<>();
        this.mainControllerComponents = new HashMap<>();

        this.addControllers();
        this.addMainControllerComponents();
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
    private void addMainControllerComponents() {
        this.mainControllerComponents.put(MarketController.class,getResourceURL("/views/app/components/market/MarketView.fxml"));
    }

    private <T extends WindowController> WindowController loadController(Class<T> controller) throws Exception {

        var controllerInstance = context.getBean(controller);

        var resourceUrl = controllerCollection.get(controller);
        var loader = new FXMLLoader(resourceUrl);
        loader.setController(controllerInstance);

        var view = (Parent)loader.load();
        controllerInstance.createScene(view);
        controllerInstance.initScene();

        return controllerInstance;
    }

    private <T extends Controller> Controller loadControllerComponent(Class<T> controller) throws Exception {
        var controllerInstance = context.getBean(controller);

        var resourceUrl = mainControllerComponents.get(controller);

        var loader = new FXMLLoader(resourceUrl);
        loader.setController(controllerInstance);

        loader.load();

        return controllerInstance;
    }

    public <T extends WindowController> void showScene(Class<T> controllerClass) throws Exception {
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
    public void showMainScene() throws Exception {
        showScene(MainController.class);
    }

    @Override
    public void showLoginScene() throws Exception {
        showScene(LoginController.class);
    }

    @Override
    public void showRegistrationScene() throws Exception {
        showScene(RegistrationController.class);
    }

    @Override
    public Controller getMarketComponentController() throws Exception {
        return loadControllerComponent(MarketController.class);
    }

    @Override
    public void initialize() throws Exception {
        this.loadDefaultScene();
    }
}
