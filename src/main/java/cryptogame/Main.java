package cryptogame;

import cryptogame.controllers.MainController;
import cryptogame.controllers.RegistrationController;
import cryptogame.services.manager.scene.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main extends Application {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.primaryStage = primaryStage;

       /* services = new DefaultServiceHandler();

        services.addServiceInstance(Stage.class,primaryStage);
        services.addServiceInstance(EntityManager.class,entityManagerFactory.createEntityManager());
        services.addService(MarketManager.class, DefaultMarketManager.class);

        services.addService(UserDao.class,UserDao.class);
        //services.addService(ISession.class,SessionManager.class);
        services.addService(SceneManager.class, DefaultSceneManager.class);

        //entityManagerFactory.close();*/

       // ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);


        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        /*var loader = new FXMLLoader(Main.class.getResource("/views/app/AppView.fxml"));
        Node n = loader.load();

       // MainController controller = ctx.getBean(MainController.class);
       // temp.setScene(controller.getScene());
        temp.setScene(n.getScene());
        temp.setResizable(true);
        temp.show();*/

        var test = ctx.getBean(SceneManager.class);
        test.showMainScene();


    }
    public static void main(String[] args) {
        launch(args);
    }
}