package cryptogame;

import cryptogame.model.services.Service;
import cryptogame.model.services.manager.scene.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    public static Stage primaryStage;

    private AnnotationConfigApplicationContext context;

    @Override
    public void start(Stage primaryStage) {

        Main.primaryStage = primaryStage;

        context= new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        var sceneManager = context.getBean(SceneManager.class);
        sceneManager.initialize();

    }

    @Override
    public void stop(){
        context.getBean(Service.class).onExit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}