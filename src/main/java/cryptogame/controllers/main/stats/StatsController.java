package cryptogame.controllers.main.stats;

import cryptogame.common.Refreshable;
import cryptogame.containers.CryptoCurrency;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.stats.components.StatsComponent;
import cryptogame.model.models.ActionHistoryModel;
import cryptogame.model.models.CryptoCurrencyModel;
import cryptogame.model.models.UserModel;
import cryptogame.model.services.Service;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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

    @FXML private ScrollPane scrollPane;
    @FXML private VBox vBox;
    @FXML private GridPane headerGrid;
    @FXML private GridPane headerStatsGrid;
    @FXML private GridPane statsGrid;

    @FXML private Label netWorthLabel;
    @FXML private Label numberOfAssetsLabel;
    @FXML private Label totalSpentLabel;
    @FXML private Label totalEarnedLabel;
    @FXML private Label mostValuableLabel;

    private final Service serviceHandler;

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

    private double getTotalSpent(UserModel user) {
        var purchaseHistory = user.getPurchaseHistory();
        var totalSpent = 0.0d;

        for(var purchase : purchaseHistory) {
            totalSpent += purchase.getCost();
        }

        return totalSpent;
    }

    private void setNetStats(UserModel user) {

        var netWorth = 0.0d;

        var currencies = serviceHandler.getMarketManager().getCurrencies();
        var wallet = user.getWallet();

        var mostExpensive = 0.0d;
        var mostExpensiveName = "";

        for(var walletCurrency : wallet) {

            var currencyModel = currencies.stream().filter(currency -> currency.getId().equals(walletCurrency.getIdName())).findFirst();

            if(currencyModel.isPresent()) {

                var price = currencyModel.get().getPriceUsd() * walletCurrency.getAmount();
                netWorth += price;

                if(price >= mostExpensive) {
                    mostExpensive = price;
                    mostExpensiveName = walletCurrency.getName();
                }

            }
        }

        netWorthLabel.setText(String.format("$%.6f",netWorth));
        mostValuableLabel.setText(mostExpensiveName);
        numberOfAssetsLabel.setText(String.format("%d",user.getWallet().size()));

        var spent = getTotalSpent(user);
        setTotalSpentLabel(spent);
        setTotalEarnedLabel(netWorth - spent);
    }

    private void setTotalSpentLabel(double spent) {
        totalSpentLabel.setText(String.format("$%s",serviceHandler.formatDouble(spent)));
    }

    private void setTotalEarnedLabel(double profit) {

        if(profit < 0) {
            totalEarnedLabel.getStyleClass().add("spent");
        } else {
            totalEarnedLabel.getStyleClass().add("profit");
        }

        totalEarnedLabel.setText(String.format("$%s",serviceHandler.formatDouble(profit)));
    }

    private void resetBox() {
        vBox.getChildren().clear();
        this.vBox.getChildren().add(headerStatsGrid);
        this.vBox.getChildren().add(statsGrid);
        this.vBox.getChildren().add(headerGrid);
    }

    private void loadStats() {
        try {
            resetBox();

            var user = serviceHandler.getUserDao()
                    .getEntity(serviceHandler.getSession().getActiveUserId()).get();

            var purchaseHistory = user.getPurchaseHistory();
            var sellHistory = user.getSellHistory();

            for(var action : purchaseHistory) {
                loadStatsComponentWithErrHandling(action,StatsComponent.ActionType.PURCHASE);
            }

            for(var action : sellHistory) {
                loadStatsComponentWithErrHandling(action,StatsComponent.ActionType.SELL);
            }

            setNetStats(user);
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    @Override
    public void refresh() {

        resetBox();

        try {
            this.initialize();
        } catch (Exception exception) {
            logger.error(exception);
        }
    }
}
