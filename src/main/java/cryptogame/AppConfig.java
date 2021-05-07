package cryptogame.main;

import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Spring AppConfig class.
 * Configures the spring context.
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
public class AppConfig {

    /**
     * The global entityManagerFactory, provided by the persistence API.
     */
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("crypto_trading_game");

    /**
     * Returns the global entity manager factory.
     *
     * @return entity manager factory bean instance
     */
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Returns a new JpaTransactionManager instance with
     * an entity manager factory.
     *
     * @return transaction manager bean
     */
    @Bean
    public PlatformTransactionManager transactionManager(){

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setNestedTransactionAllowed(true);

        return transactionManager;
    }

    /**
     * Returns the single primary stage instance of the application,
     * found in the {@link Main} class.
     *
     * @return primary stage
     */
    @Primary
    @Bean
    public Stage primaryStage() {
        return CryptoGameApp.primaryStage;
    }

}
