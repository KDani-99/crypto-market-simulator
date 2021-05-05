package cryptogame.model.services.manager.scene;

import cryptogame.Main;
import cryptogame.common.Refreshable;
import cryptogame.controllers.*;
import cryptogame.controllers.dialog.PurchaseDialogController;
import cryptogame.controllers.login.LoginController;
import cryptogame.controllers.main.MainController;
import cryptogame.controllers.main.navbar.NavbarController;
import cryptogame.controllers.main.market.MarketController;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.controllers.main.stats.StatsController;
import cryptogame.controllers.main.stats.components.StatsComponent;
import cryptogame.controllers.main.wallet.WalletController;
import cryptogame.controllers.main.wallet.components.WalletComponent;
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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

@Component
public class SceneManagerImplementation implements SceneManager {

    @lombok.Data
    static class ControllerInfo {
        private final URL url;
        private boolean loaded;

        public ControllerInfo(URL url) {
            this.url = url;
        }
    }

    private static final Logger logger = LogManager.getLogger(SceneManagerImplementation.class);

    private final ApplicationContext context;

    private final HashMap<Class<?>, ControllerInfo> controllerCollection;
    private final HashMap<Class<?>, ControllerInfo> mainControllerComponents;
    private final HashMap<Class<?>, URL> prototypeComponentControllers;
    private final HashMap<Class<?>, URL> dialogControllerCollection;

    private final HashMap<Class<?>,Stage> activeStages;

    private final Stage primaryStage;

    @Autowired
    public SceneManagerImplementation(ApplicationContext context, Stage primaryStage) {
        this.context = context;
        this.primaryStage = primaryStage;
        this.controllerCollection = new HashMap<>();
        this.mainControllerComponents = new HashMap<>();
        this.dialogControllerCollection = new HashMap<>();
        this.prototypeComponentControllers = new HashMap<>();

        activeStages = new HashMap<>();

        this.addControllers();
        this.addMainControllerComponents();
        this.addPrototypeComponentControllers();
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
        // The scene itself
        this.controllerCollection.put(MainController.class,new ControllerInfo(getResourceURL("/views/app/AppView.fxml")));
        this.controllerCollection.put(LoginController.class, new ControllerInfo(getResourceURL("/views/login/LoginView.fxml")));
        this.controllerCollection.put(RegistrationController.class,new ControllerInfo(getResourceURL("/views/registration/RegistrationView.fxml")));
    }
    private void addMainControllerComponents() {
        // Loaded into a scene
        this.mainControllerComponents.put(NavbarController.class,new ControllerInfo(getResourceURL("/views/app/components/navbar/Navbar.fxml")));
        this.mainControllerComponents.put(MarketController.class,new ControllerInfo(getResourceURL("/views/app/components/market/MarketView.fxml")));
        this.mainControllerComponents.put(StatsController.class,new ControllerInfo(getResourceURL("/views/app/components/stats/StatsView.fxml")));
        this.mainControllerComponents.put(WalletController.class,new ControllerInfo(getResourceURL("/views/app/components/wallet/WalletView.fxml")));
    }

    private void addPrototypeComponentControllers() {
        // Reusable component
        this.prototypeComponentControllers.put(CurrencyComponent.class, getResourceURL("/views/app/components/market/components/CurrencyComponent.fxml"));
        this.prototypeComponentControllers.put(WalletComponent.class, getResourceURL("/views/app/components/wallet/components/WalletComponent.fxml"));
        this.prototypeComponentControllers.put(StatsComponent.class,getResourceURL("/views/app/components/stats/components/StatsComponent.fxml"));
    }

    private void addDialogControllers() {
        // Dialog windows
        this.dialogControllerCollection.put(PurchaseDialogController.class,getResourceURL("/views/dialog/purchase/PurchaseDialogView.fxml"));
    }

    private <T> T loadFXML(URL url, Controller controllerInstance) throws IOException {
        var loader = new FXMLLoader(url);
        loader.setController(controllerInstance);

        return loader.load();
    }

    private <T extends WindowController> WindowController loadController(Class<T> controller) throws Exception {

        var controllerInstance = context.getBean(controller);

        var controllerInfo = controllerCollection.get(controller);

        if(!controllerInfo.isLoaded()) {
            var view = (Parent) loadFXML(controllerInfo.url, controllerInstance);
            controllerInstance.createScene(view);
            controllerInstance.initScene();
            controllerInfo.setLoaded(true);
        }

        return controllerInstance;
    }

    private <T extends Controller> Controller loadControllerComponent(Class<T> controller) throws Exception {
        var controllerInstance = context.getBean(controller);

        var controllerInfo = mainControllerComponents.get(controller);

        if(!controllerInfo.isLoaded()) {
            loadFXML(controllerInfo.url, controllerInstance);
            controllerInfo.setLoaded(true);
        }

        return controllerInstance;
    }

    private <T extends Controller> Controller createPrototypeComponentController(Class<T> controller) throws Exception {

        var controllerInstance = context.getBean(controller);

        var resourceUrl = this.prototypeComponentControllers.get(controller);

        loadFXML(resourceUrl, controllerInstance);

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

        primaryStage.setScene(controllerInstance.getScene());
        primaryStage.setResizable(controllerInstance.isResizable());
        primaryStage.setTitle(title);
        primaryStage.show();

        return controllerInstance;
    }

    private void setStageMinSizeProperties(double width, double height) {
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
    }
    private void setStageMaxSizeProperties(double width, double height) {
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
    }
    private void setStageSizeProperties(double width, double height) {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }

    @Override
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    @Override
    public WindowController showMainScene() throws Exception {
        setStageMinSizeProperties(850,500);
        setStageMaxSizeProperties(Double.MAX_VALUE,Double.MAX_VALUE);
        return showScene(MainController.class,"Main");
    }

    @Override
    public WindowController showLoginScene() throws Exception {
        setStageMinSizeProperties(400,500);
        setStageSizeProperties(400,500);
        setStageMaxSizeProperties(400,500);
        return showScene(LoginController.class,"Login");
    }

    @Override
    public WindowController showRegistrationScene() throws Exception {
        setStageMinSizeProperties(400,500);
        setStageSizeProperties(400,500);
        setStageMaxSizeProperties(400,500);
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
    public Controller getStatsController() throws Exception {
        return loadControllerComponent(StatsController.class);
    }

    @Override
    public Controller getWalletController() throws Exception {
        return loadControllerComponent(WalletController.class);
    }

    @Override
    public Controller createStatsComponent() throws Exception {
        return createPrototypeComponentController(StatsComponent.class);
    }

    @Override
    public Controller createCurrencyComponent() throws Exception {
        return createPrototypeComponentController(CurrencyComponent.class);
    }

    @Override
    public Controller createWalletComponent() throws Exception {
        return createPrototypeComponentController(WalletComponent.class);
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
    public void closeAllDialog() {
        for(var dialog : activeStages.values()) {
            dialog.close();
        }
        activeStages.clear();
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


    private <T> Optional<ControllerInfo> findControllerInfoOfType(Class<T> controllerType) {
        // Finds the refreshable components of a given class

        for(var controller : controllerCollection.keySet()) {
            if(controller.isAssignableFrom(controllerType))
                return Optional.of(controllerCollection.get(controller));
        }

        for(var controller : mainControllerComponents.keySet()) {
            if(controller.isAssignableFrom(controllerType))
                return Optional.of(mainControllerComponents.get(controller));
        }

        return Optional.empty();
    }

    @Override
    public void refresh() {
        try {
            for(var controller : context.getBeansOfType(Refreshable.class).values()) {
                var controllerInfo = findControllerInfoOfType(controller.getClass());
                if(controllerInfo.isPresent() && controllerInfo.get().isLoaded()) {
                    controller.refresh();
                }
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
    }
    @Override
    public void onExit() {
        for(var controller : context.getBeansOfType(Controller.class).values()) {
            controller.onExit();
            logger.info("Called onExit event on " + controller.getClass().getName() + "controller instance");
        }
    }
}
