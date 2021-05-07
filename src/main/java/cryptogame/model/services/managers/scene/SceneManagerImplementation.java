package cryptogame.model.services.managers.scene;

import cryptogame.main.Main;
import cryptogame.common.interfaces.Refreshable;
import cryptogame.controllers.*;
import cryptogame.controllers.dialog.PurchaseDialogController;
import cryptogame.controllers.dialog.SellCurrencyDialogController;
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

/**
 * The implementation of the {@link SceneManager} interface.
 */
@Component
public class SceneManagerImplementation implements SceneManager {

    /**
     * A POJO class for Controller info.
     */
    @lombok.Data
    static class ControllerInfo {
        /**
         * The URL of the fxml file.
         */
        private final URL url;
        /**
         * Marks whether the component is loaded.
         */
        private boolean loaded;

        /**
         * Creates a new instance of the ControllerInfo with the fxml url.
         *
         * @param url the fxml url
         */
        public ControllerInfo(URL url) {
            this.url = url;
        }
    }

    /**
     * Logger for logging.
     */
    private static final Logger logger = LogManager.getLogger(SceneManagerImplementation.class);

    /**
     * Spring application context injected by the framework.
     */
    private final ApplicationContext context;

    /**
     * A {@link HashMap} that contains controller class and controller info associations.
     */
    private final HashMap<Class<?>, ControllerInfo> controllerCollection;
    /**
     * A {@link HashMap} that contains controller component class and controller info associations.
     */
    private final HashMap<Class<?>, ControllerInfo> mainControllerComponents;
    /**
     * A {@link HashMap} that contains prototype controller class and fxml url associations.
     */
    private final HashMap<Class<?>, URL> prototypeComponentControllers;
    /**
     * A {@link HashMap} that contains dialog controller class and fxml url associations.
     */
    private final HashMap<Class<?>, URL> dialogControllerCollection;
    /**
     * A {@link HashMap} that contains active stages.
     */
    private final HashMap<Class<?>,Stage> activeStages;

    /**
     * The injected primaryStage of the application.
     */
    private final Stage primaryStage;

    /**
     * Creates a new SceneManagerImplementation and injects the given parameters, via the
     * {@link Autowired} annotation.
     *  Also initializes the default values of this class.
     *
     * @param context the spring application context
     * @param primaryStage the primary stage of the application
     */
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

    /**
     * Loads the default scene.
     */
    private void loadDefaultScene() {
        try {
            this.showLoginScene();
        } catch (Exception exception) {
            logger.fatal(exception);
        }
    }

    /**
     * A helper method that returns the resource url from a {@code String}.
     *
     * @param path url to be converted
     * @return the converted URL
     */
    private URL getResourceURL(String path) {
        return Main.class.getResource(path);
    }

    /**
     * Registers the controller and controller info associations.
     */
    private void addControllers() {
        // The scene itself
        this.controllerCollection.put(MainController.class,new ControllerInfo(getResourceURL("/views/app/AppView.fxml")));
        this.controllerCollection.put(LoginController.class, new ControllerInfo(getResourceURL("/views/login/LoginView.fxml")));
        this.controllerCollection.put(RegistrationController.class,new ControllerInfo(getResourceURL("/views/registration/RegistrationView.fxml")));
    }
    /**
     * Registers the main controller components and controller info associations.
     */
    private void addMainControllerComponents() {
        // Loaded into a scene
        this.mainControllerComponents.put(NavbarController.class,new ControllerInfo(getResourceURL("/views/app/components/navbar/Navbar.fxml")));
        this.mainControllerComponents.put(MarketController.class,new ControllerInfo(getResourceURL("/views/app/components/market/MarketView.fxml")));
        this.mainControllerComponents.put(StatsController.class,new ControllerInfo(getResourceURL("/views/app/components/stats/StatsView.fxml")));
        this.mainControllerComponents.put(WalletController.class,new ControllerInfo(getResourceURL("/views/app/components/wallet/WalletView.fxml")));
    }
    /**
     * Registers the prototype controller and fxml url associations.
     */
    private void addPrototypeComponentControllers() {
        // Reusable component
        this.prototypeComponentControllers.put(CurrencyComponent.class, getResourceURL("/views/app/components/market/components/CurrencyComponent.fxml"));
        this.prototypeComponentControllers.put(WalletComponent.class, getResourceURL("/views/app/components/wallet/components/WalletComponent.fxml"));
        this.prototypeComponentControllers.put(StatsComponent.class,getResourceURL("/views/app/components/stats/components/StatsComponent.fxml"));
    }
    /**
     * Registers the dialog controller and fxml url associations.
     */
    private void addDialogControllers() {
        // Dialog windows
        this.dialogControllerCollection.put(PurchaseDialogController.class,getResourceURL("/views/dialog/purchase/PurchaseDialogView.fxml"));
        this.dialogControllerCollection.put(SellCurrencyDialogController.class,getResourceURL("/views/dialog/sell/SellCurrencyDialogView.fxml"));
    }

    /**
     * A helper method that loads the given fxml file and sets the controller.
     *
     * @param url the fxml path
     * @param controllerInstance the instance of the controller
     * @param <T> the type of the scene node
     * @return the loaded scene node
     * @throws IOException if the file is not found
     */
    private <T> T loadFXML(URL url, Controller controllerInstance) throws IOException {
        var loader = new FXMLLoader(url);
        loader.setController(controllerInstance);

        return loader.load();
    }

    /**
     * A helper method that loads a {@link WindowController} of the given class.
     *
     * @param controller the controller's class
     * @param <T> the type of the controller
     * @return the controller instance from the application context
     * @throws Exception if it is not a valid {@link WindowController} type
     */
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

    /**
     * Loads the controller component of the given class
     * (that implements the Controller interface).
     *
     * @param controller the class of the controller
     * @param <T> the type of the controller
     * @return a new controller instance
     * @throws Exception if the controller could not be loaded
     */
    private <T extends Controller> Controller loadControllerComponent(Class<T> controller) throws Exception {
        var controllerInstance = context.getBean(controller);

        var controllerInfo = mainControllerComponents.get(controller);

        if(!controllerInfo.isLoaded()) {
            loadFXML(controllerInfo.url, controllerInstance);
            controllerInfo.setLoaded(true);
        }

        return controllerInstance;
    }

    /**
     * Creates a prototype component controller of the given class.
     * (that implements the Controller interface).
     *
     * @param controller the class of the controller
     * @param <T> the type of the controller
     * @return a new controller instance
     * @throws Exception if the controller could not be loaded
     */
    private <T extends Controller> Controller createPrototypeComponentController(Class<T> controller) throws Exception {

        var controllerInstance = context.getBean(controller);

        var resourceUrl = this.prototypeComponentControllers.get(controller);

        loadFXML(resourceUrl, controllerInstance);

        return controllerInstance;
    }

    /**
     * Creates a new dialog window.
     * Note that the dialogs are marked as prototypes, so each {@link ApplicationContext#getBean(String)}
     * call will result in a new instance.
     *
     * @param dialog the class of the dialog
     * @param title the title of the dialog window
     * @param width the width of the dialog window
     * @param height the height of the dialog window
     * @param resizable whether the window is resizable
     * @param <T> the type of the dialog
     * @return the controller of the newly created dialog
     * @throws Exception if the controller could not be loaded
     */
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

        activeStages.put(dialog,stage);

        return controllerInstance;
    }

    /**
     * Shows the given {@link WindowController} in the primary stage.
     * The title of the window will be set.
     *
     * @param controllerClass the class of the desired controller
     * @param title the desired title of the window
     * @param <T> the type of the controller class
     * @return a controller instance
     * @throws Exception if the scene could not be loaded
     */
    public <T extends WindowController> WindowController showScene(Class<T> controllerClass,String title) throws Exception {
        var controllerInstance = loadController(controllerClass);

        primaryStage.setScene(controllerInstance.getScene());
        primaryStage.setResizable(controllerInstance.isResizable());
        primaryStage.setTitle(title);
        primaryStage.show();

        return controllerInstance;
    }

    /**
     * Sets the minimum size properties of the primary stage.
     *
     * @param width the minimum width
     * @param height the minimum height
     */
    private void setStageMinSizeProperties(double width, double height) {
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
    }

    /**
     * Sets maximum size properties of the primary stage.
     *
     * @param width the maximum width
     * @param height the maximum height
     */
    private void setStageMaxSizeProperties(double width, double height) {
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
    }

    /**
     * Sets preferred size properties of the primary stage.
     *
     * @param width the preferred width
     * @param height the preferred height
     */
    private void setStageSizeProperties(double width, double height) {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }

    /**
     * A helper method that finds a controller of the given type.
     *
     * @param controllerType the controller's class
     * @param <T> the type of the controller
     * @return an {@link Optional} object with the {@link ControllerInfo},
     * otherwise an {@link ControllerInfo} o with null
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WindowController showMainScene() throws Exception {
        setStageMinSizeProperties(850,500);
        setStageMaxSizeProperties(Double.MAX_VALUE,Double.MAX_VALUE);
        return showScene(MainController.class,"Main");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WindowController showLoginScene() throws Exception {
        setStageMinSizeProperties(400,500);
        setStageSizeProperties(400,500);
        setStageMaxSizeProperties(400,500);
        return showScene(LoginController.class,"Login");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WindowController showRegistrationScene() throws Exception {
        setStageMinSizeProperties(400,500);
        setStageSizeProperties(400,500);
        setStageMaxSizeProperties(400,500);
        return showScene(RegistrationController.class,"Registration");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MainController getMainController() {
        return context.getBean(MainController.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller getNavbarController() throws Exception {
        return loadControllerComponent(NavbarController.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller getMarketComponentController() throws Exception {
        return loadControllerComponent(MarketController.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller getStatsController() throws Exception {
        return loadControllerComponent(StatsController.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller getWalletController() throws Exception {
        return loadControllerComponent(WalletController.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller createStatsComponent() throws Exception {
        return createPrototypeComponentController(StatsComponent.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller createCurrencyComponent() throws Exception {
        return createPrototypeComponentController(CurrencyComponent.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller createWalletComponent() throws Exception {
        return createPrototypeComponentController(WalletComponent.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller createPurchaseWindow() throws Exception {
        if(activeStages.containsKey(PurchaseDialogController.class)) {
            activeStages.get(PurchaseDialogController.class).close();
            activeStages.remove(PurchaseDialogController.class);
        }

        return createDialog(PurchaseDialogController.class,"Purchase currency",250,135,false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller createSellCurrencyWindow() throws Exception {
        if(activeStages.containsKey(SellCurrencyDialogController.class)) {
            activeStages.get(SellCurrencyDialogController.class).close();
            activeStages.remove(SellCurrencyDialogController.class);
        }

        return createDialog(SellCurrencyDialogController.class, "Sell currency", 250, 135, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeAllDialog() {
        for(var dialog : activeStages.values()) {
            dialog.close();
        }
        activeStages.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAlert(Alert.AlertType alertType, String title,String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        this.loadDefaultScene();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {

        for(var controller : context.getBeansOfType(Controller.class).values()) {
            controller.reset();
        }

        for(var controllerInfo : controllerCollection.values()) {
            controllerInfo.setLoaded(false);
        }
        for(var controllerInfo : mainControllerComponents.values()) {
            controllerInfo.setLoaded(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExit() {
        for(var controller : context.getBeansOfType(Controller.class).values()) {
            controller.onExit();
            logger.info("Called onExit event on " + controller.getClass().getName() + "controller instance");
        }
    }
}
