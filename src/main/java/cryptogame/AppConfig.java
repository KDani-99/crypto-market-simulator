package cryptogame;

import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@ComponentScan
@EnableTransactionManagement
public class AppConfig {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory );
        transactionManager.setNestedTransactionAllowed(true);

        return transactionManager;
    }

    @Primary
    @Bean
    public Stage primaryStage() {
        return Main.primaryStage;
    }

}
