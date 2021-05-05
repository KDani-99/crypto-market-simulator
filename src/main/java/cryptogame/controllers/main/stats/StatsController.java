package cryptogame.controllers.main.stats;

import cryptogame.common.Refreshable;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.stats.components.StatsComponent;
import cryptogame.model.models.ActionHistoryModel;
import cryptogame.model.services.Service;
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

@Component
public class StatsController implements Controller, Refreshable {

    private static final Logger logger = LogManager.getLogger(StatsController.class);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane headerGrid;

    private final Service serviceHandler;

    private boolean isInitialized = false;

    @Autowired
    public StatsController(Service serviceHandler) {
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

    @FXML
    public void initialize() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        this.loadStats();
    }

    private void loadStatsComponent(ActionHistoryModel action,StatsComponent.ActionType actionType) throws Exception {

        var statsComponent = (StatsComponent) serviceHandler.getSceneManager().createStatsComponent();
        statsComponent.setAction(action);
        statsComponent.setActionType(actionType);
        statsComponent.initialize();

        vBox.getChildren().add(statsComponent.getRoot());
    }

    private void loadStatsComponentWithErrHandling(ActionHistoryModel action,StatsComponent.ActionType actionType) {
        try {
            loadStatsComponent(action,actionType);
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    private void loadStats() {

        var elem = vBox.getChildren().remove(0);
        vBox.getChildren().clear();
        vBox.getChildren().add(elem);

        var user = serviceHandler.getUserDao()
                .getEntity(serviceHandler.getSession().getActiveUserId());

        var purchaseHistory = user.get().getPurchaseHistory();
        var sellHistory = user.get().getSellHistory();

        for(var action : purchaseHistory) {
            loadStatsComponentWithErrHandling(action,StatsComponent.ActionType.PURCHASE);
        }

        for(var action : sellHistory) {
            loadStatsComponentWithErrHandling(action,StatsComponent.ActionType.SELL);
        }
    }

    @Override
    public void refresh() {

        if(!isInitialized) return;

        this.vBox.getChildren().clear();
        this.vBox.getChildren().add(headerGrid);

        try {
            this.initialize();
        } catch (Exception exception) {
            logger.error(exception);
        }
    }
}
