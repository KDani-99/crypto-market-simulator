package cryptogame;

import cryptogame.services.manager.scene.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.primaryStage = primaryStage;

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        var sceneManager = ctx.getBean(SceneManager.class);
        sceneManager.showLoginScene();

    }
    public static void main(String[] args) {
        launch(args);
    }
}