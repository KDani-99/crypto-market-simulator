package cryptogame;

import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@ComponentScan
public class AppConfig {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return entityManagerFactory;
    }

    @Primary
    @Bean
    public Stage primaryStage() {
        return Main.primaryStage;
    }

}
