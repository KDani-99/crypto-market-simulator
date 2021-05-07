package cryptogame.main;

import cryptogame.model.services.Service;
import cryptogame.model.services.managers.scene.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The main application class.
 */
public class Main {
    /**
     * Entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(CryptoGameApp.class,args);
    }
}