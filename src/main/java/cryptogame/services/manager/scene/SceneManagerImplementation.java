package cryptogame.services.manager.scene;

import cryptogame.Main;
import cryptogame.controllers.*;
import cryptogame.controllers.dialog.PurchaseDialogController;
import cryptogame.controllers.login.LoginController;
import cryptogame.controllers.main.NavbarController;
import cryptogame.controllers.main.bank.BankController;
import cryptogame.controllers.main.market.MarketController;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.controllers.main.stats.StatsController;
import cryptogame.controllers.main.stats.components.StatsComponent;
import cryptogame.controllers.registration.RegistrationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashMap;

@Component("sceneManager")
public class SceneManagerImplementation implements SceneManager {

    private static final Logger logger = LogManager.getLogger(SceneManagerImplementation.class);

    private final ApplicationContext context;

    private final HashMap<Class<?>, URL> controllerCollection;
    private final HashMap<Class<?>, URL> mainControllerComponents;
    private final HashMap<Class<?>, URL> dialogControllerCollection;

    private final HashMap<Class<?>,Stage> activeStages;

    private final HashMap<Class<?>, Controller> controllerInstances;

    private final Stage primaryStage;

    @Autowired
    public SceneManagerImplementation(ApplicationContext context, Stage primaryStage) {
        this.context = context;
        this.primaryStage = primaryStage;
        this.controllerCollection = new HashMap<>();
        this.mainControllerComponents = new HashMap<>();
        this.dialogControllerCollection = new HashMap<>();
        controllerInstances = new HashMap<>();

        activeStages = new HashMap<>();

        this.addControllers();
        this.addMainControllerComponents();
        this.addDialogControllers();
    }

    private void loadDefaultScene() {
        try {
            this.showLoginScene();
        } catch (Exception exception) {
            logger.fatal(exception);
        }
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
        this.mainControllerComponents.put(NavbarController.class,getResourceURL("/views/app/components/navbar/Navbar.fxml"));
        this.mainControllerComponents.put(StatsComponent.class,getResourceURL("/views/app/components/stats/components/StatsComponent.fxml"));
        this.mainControllerComponents.put(CurrencyComponent.class,getResourceURL("/views/app/components/market/components/CurrencyComponent.fxml"));

        this.mainControllerComponents.put(MarketController.class,getResourceURL("/views/app/components/market/MarketView.fxml"));
        this.mainControllerComponents.put(BankController.class,getResourceURL("/views/app/components/bank/BankView.fxml"));
        this.mainControllerComponents.put(StatsController.class,getResourceURL("/views/app/components/stats/StatsView.fxml"));
    }
    private void addDialogControllers() {
        this.dialogControllerCollection.put(PurchaseDialogController.class,getResourceURL("/views/dialog/purchase/PurchaseDialogView.fxml"));
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

    private <T extends Controller> Controller createDialog(Class<T> dialog,String title,int width, int height, boolean resizable) throws Exception {
        var controllerInstance = (Controller) context.getBean(dialog);

        var resourceUrl = dialogControllerCollection.get(dialog);

        var loader = new FXMLLoader(resourceUrl);
        loader.setController(controllerInstance);

        var stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);

        Scene scene = new Scene(loader.load(),width,height);
        stage.setResizable(resizable);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();

        activeStages.put(PurchaseDialogController.class,stage);

        return controllerInstance;
    }

    public <T extends WindowController> WindowController showScene(Class<T> controllerClass,String title) throws Exception {
        var controllerInstance = loadController(controllerClass);

        if(!controllerInstances.containsKey(controllerInstance.getClass())) {
            controllerInstances.put(controllerInstance.getClass(),controllerInstance);
        }

        primaryStage.setScene(controllerInstance.getScene());
        primaryStage.setResizable(controllerInstance.isResizable());
        primaryStage.setTitle(title);
        primaryStage.show();

        return controllerInstance;
    }

    @Override
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    @Override
    public WindowController showMainScene() throws Exception {
        return showScene(MainController.class,"Main");
    }

    @Override
    public WindowController showLoginScene() throws Exception {
        primaryStage.setWidth(400);
        primaryStage.setHeight(500);
        return showScene(LoginController.class,"Login");
    }

    @Override
    public WindowController showRegistrationScene() throws Exception {
        return showScene(RegistrationController.class,"Registration");
    }

    @Override
    public MainController getMainController() {
        return context.getBean(MainController.class);
    }

    @Override
    public Controller getNavbarController() throws Exception {
        return loadControllerComponent(NavbarController.class);
    }

    @Override
    public Controller getMarketComponentController() throws Exception {
        return loadControllerComponent(MarketController.class);
    }

    @Override
    public Controller getBankController() throws Exception {
        return loadControllerComponent(BankController.class);
    }

    @Override
    public StatsController getStatsController() throws Exception {
        return (StatsController) loadControllerComponent(StatsController.class);
    }

    @Override
    public Controller createStatsComponent() throws Exception {
        return loadControllerComponent(StatsComponent.class);
    }

    @Override
    public Controller createCurrencyComponent() throws Exception {
        return loadControllerComponent(CurrencyComponent.class);
    }

    @Override
    public Controller createPurchaseWindow() throws Exception {
        if(activeStages.containsKey(PurchaseDialogController.class)) {
            activeStages.get(PurchaseDialogController.class).close();
            activeStages.remove(PurchaseDialogController.class);
        }

        return createDialog(PurchaseDialogController.class,"Purchase currency",250,135,false);
    }

    @Override
    public void createAlert(Alert.AlertType alertType, String title,String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize() {
        this.loadDefaultScene();
    }
}
