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
import java.util.HashMap;
import java.util.Properties;

public class Main extends Application {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");

    UserDao usersDao = null;
    SessionDao sessionDao = null;
    SessionManager sessionManager = null;
    EntityManager entityManager = null;

    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{

        entityManager = entityManagerFactory.createEntityManager();
        usersDao = new UserDao(entityManager);
        sessionDao = new SessionDao(entityManager);

        sessionManager = new SessionManager(usersDao,sessionDao);


        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("/application.properties"));


        controller = new Controller(properties,primaryStage,sessionManager);
        controller.setupScenes();

    }

    public static void main(String[] args) {
        launch(args);
    }
}