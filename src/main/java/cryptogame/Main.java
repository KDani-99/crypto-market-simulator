package cryptogame;

import cryptogame.controller.LoginController;
import cryptogame.model.dao.SessionDao;
import cryptogame.model.dao.UserDao;
import cryptogame.model.session.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Main extends Application {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");
    UserDao usersDao = null;
    SessionDao sessionDao = null;
    SessionManager sessionManager = null;
    EntityManager entityManager = null;

    LoginController loginController;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // test

        // TEST
        //databaseHandler = new H2Service();
       // databaseHandler.connect();
        entityManager = entityManagerFactory.createEntityManager();
        usersDao = new UserDao(entityManager);
        sessionDao = new SessionDao(entityManager);

        sessionManager = new SessionManager(usersDao,sessionDao);
        try {
;
            sessionManager.register("Daniel","kdani98@mailbox.unideb.hu","ThiSisM@246","ThiSisM@246");
            System.out.println("Registered user");
        } catch (Exception ex) {
            System.out.println("ERROR (register)! "+ex.getMessage());
            try {
                sessionManager.login("Daniel","ThiSisM@246");
                System.out.println("Logged in");
            } catch (Exception ex2) {
                System.out.println("ERROR (login) ! "+ex2.getMessage());
            }
        }

       // Parent root = FXMLLoader.load(Main.class.getResource("/views/MainView.fxml"));
        Parent root = FXMLLoader.load(Main.class.getResource("/views/login/LoginView.fxml"));

        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("/application.properties"));

        primaryStage.setTitle("Crypto Trading Game");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.setResizable(false);

        ((Label)primaryStage.getScene().lookup("#label_version")).setText("v"+properties.get("version").toString());
       /* primaryStage.setMinWidth(300);
        primaryStage.setMaxWidth(300);
        primaryStage.setMaxHeight(450);
        primaryStage.setMinHeight(450);*/

        primaryStage.show();

        loginController = new LoginController(primaryStage,sessionManager);

    }

    public static void main(String[] args) {
        launch(args);
    }
}