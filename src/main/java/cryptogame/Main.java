package cryptogame;

import cryptogame.model.dao.SessionDao;
import cryptogame.model.dao.UserDao;
import cryptogame.model.session.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main extends Application {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");
    UserDao usersDao = null;
    SessionDao sessionDao = null;
    SessionManager sessionManager = null;
    EntityManager entityManager = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

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

        Parent root = FXMLLoader.load(Main.class.getResource("/views/MainView.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 600));
        primaryStage.setMinWidth(300);
        primaryStage.setMaxWidth(300);
        primaryStage.setMaxHeight(600);
        primaryStage.setMinHeight(600);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}