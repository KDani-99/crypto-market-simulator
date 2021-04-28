package cryptogame.controllers.main.stats;

import cryptogame.containers.CryptoCurrency;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.controllers.main.stats.components.StatsComponent;
import cryptogame.models.ActionHistoryModel;
import cryptogame.services.Service;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsControllerImplementation implements StatsController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane headerGrid;

    private final Service serviceHandler;

    private boolean initialized = false;

    @Autowired
    public StatsControllerImplementation(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public Node getRoot() {
        return scrollPane;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public void initialize() throws Exception {

        if(initialized) return; // TODO: Fix init call

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        this.loadStats();

        initialized = true;
    }

    private void loadStats() {
        try {

            var user = serviceHandler.getUserDao()
                    .getEntity(serviceHandler.getSession().getActiveUserId());

            var purchaseHistory = user.get().getPurchaseHistory();
            var sellHistory = user.get().getSellHistory();

            for(var action : purchaseHistory) {

                var statsComponent = (StatsComponent) serviceHandler.getSceneManager().createStatsComponent();
                statsComponent.setAction(action);
                statsComponent.setActionType(StatsComponent.ActionType.PURCHASE);
                statsComponent.initialize();

                vBox.getChildren().add(statsComponent.getRoot());
            }

            for(var action : sellHistory) {

                var statsComponent = (StatsComponent) serviceHandler.getSceneManager().createStatsComponent();
                statsComponent.setAction(action);
                statsComponent.setActionType(StatsComponent.ActionType.SELL);
                statsComponent.initialize();

                vBox.getChildren().add(statsComponent.getRoot());

            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void refreshUser() {

        this.vBox.getChildren().clear();
        this.vBox.getChildren().add(headerGrid);

        this.initialized = false;
        try {
            this.initialize();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
