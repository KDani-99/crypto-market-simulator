package cryptogame;

import cryptogame.dao.IDao;
import cryptogame.dao.SessionDao;
import cryptogame.dao.UserDao;
import cryptogame.service.manager.market.DefaultMarketManager;
import cryptogame.service.manager.market.MarketManager;
import cryptogame.service.manager.scene.SceneManager;
import cryptogame.service.manager.scene.DefaultSceneManager;
import cryptogame.model.services.session.ISession;
import cryptogame.model.services.session.SessionManager;
import cryptogame.service.DefaultServiceHandler;
import cryptogame.service.ServiceHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main extends Application {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");

    ServiceHandler services = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        services = new DefaultServiceHandler();

        services.addServiceInstance(Stage.class,primaryStage);
        services.addServiceInstance(EntityManager.class,entityManagerFactory.createEntityManager());

        services.addService(IDao.class,UserDao.class);
        services.addService(IDao.class,SessionDao.class);
        services.addService(ISession.class,SessionManager.class);
        services.addService(SceneManager.class, DefaultSceneManager.class);

        services.addService(MarketManager.class, DefaultMarketManager.class);

        //entityManagerFactory.close();

    }
    public static void main(String[] args) {
        launch(args);
    }
}