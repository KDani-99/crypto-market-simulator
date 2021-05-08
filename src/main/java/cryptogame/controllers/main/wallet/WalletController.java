package cryptogame.controllers.main.wallet;

import cryptogame.common.interfaces.Refreshable;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.wallet.components.WalletComponent;
import cryptogame.model.models.CryptoCurrencyModel;
import cryptogame.model.services.Service;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WalletController implements Controller, Refreshable {

    private static final Logger logger = LogManager.getLogger(WalletController.class);

    @FXML private ScrollPane scrollPane;
    @FXML private VBox vBox;
    @FXML private GridPane headerGrid;

    private final Service serviceHandler;

    @Autowired
    public WalletController(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @FXML
    public void initialize() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        new Thread(this::loadWallet).start();
    }

    private void initializeWithErrHandling() {
        try {
            this.initialize();
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    @Override
    public Node getRoot() {
        return this.scrollPane;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    private void resetBox() {
        vBox.getChildren().clear();
        vBox.getChildren().add(headerGrid);
    }

    private void loadWalletComponent(CryptoCurrencyModel currencyModel) throws Exception {
        var statsComponent = (WalletComponent) serviceHandler.getSceneManager().createWalletComponent();
        statsComponent.setCurrencyModel(currencyModel);
        statsComponent.initialize();

        vBox.getChildren().add(statsComponent.getRoot());
    }

    private void loadWalletComponentWithErrHandling(CryptoCurrencyModel model) {
        try {
            loadWalletComponent(model);
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    private void loadWalletComponents(Set<CryptoCurrencyModel> wallet) {
        for(var currency : wallet) {
            loadWalletComponentWithErrHandling(currency);
        }
    }

    private void loadWallet() {
        try {

            var user = serviceHandler.getUserDao().getEntity(serviceHandler.getSession().getActiveUserId())
                    .get();

            var wallet = user.getWallet();

            Platform.runLater(() -> {
                this.resetBox();
                this.loadWalletComponents(wallet);
            });

        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    @Override
    public void refresh() {
        resetBox();
        initializeWithErrHandling();
    }
}
