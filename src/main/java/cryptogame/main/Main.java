package cryptogame.main;

import cryptogame.model.services.Service;
import cryptogame.model.services.managers.scene.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The main JavaFX application class.
 */
public class Main extends Application {

    /**
     * The primary stage.
     * Only one primary stage instance should exist.
     */
    public static Stage primaryStage;

    /**
     * Spring application context.
     */
    private AnnotationConfigApplicationContext context;

    /**
     * Application start method.
     * Also configures the Spring application context
     * and initializes the scene manager.
     *
     * @param primaryStage passed by the application
     */
    @Override
    public void start(Stage primaryStage) {

        Main.primaryStage = primaryStage;

        context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        var sceneManager = context.getBean(SceneManager.class);
        sceneManager.initialize();

    }

    /**
     * Raises the {@link Service#onExit()} event on the service class.
     */
    @Override
    public void stop(){
        context.getBean(Service.class).onExit();
    }

    /**
     * Entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}