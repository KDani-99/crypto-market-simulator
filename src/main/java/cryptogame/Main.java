package cryptogame;

import cryptogame.model.common.Initializable;
import cryptogame.model.dao.IDao;
import cryptogame.model.dao.SessionDao;
import cryptogame.model.dao.UserDao;
import cryptogame.model.database.jpa.entities.Session;
import cryptogame.model.database.json.entities.User;
import cryptogame.model.services.IServices;
import cryptogame.model.services.Services;
import cryptogame.model.services.managers.ISceneManager;
import cryptogame.model.services.managers.SceneManager;
import cryptogame.model.services.session.ISession;
import cryptogame.model.services.session.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

public class Main extends Application {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");

    IServices services = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        services = new Services();

        services.addServiceInstance(Stage.class,primaryStage);
        services.addServiceInstance(EntityManager.class,entityManagerFactory.createEntityManager());

        services.addService(IDao.class,UserDao.class);
        services.addService(IDao.class,SessionDao.class);
        services.addService(ISession.class,SessionManager.class);
        services.addService(ISceneManager.class,SceneManager.class);

    }
    public static void main(String[] args) {
        launch(args);
    }
}