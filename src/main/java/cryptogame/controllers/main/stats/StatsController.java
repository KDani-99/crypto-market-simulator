package cryptogame.controllers.main.stats;

import cryptogame.common.interfaces.Refreshable;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.stats.components.StatsComponent;
import cryptogame.model.models.ActionHistoryModel;
import cryptogame.model.models.UserModel;
import cryptogame.model.services.Service;
import javafx.application.Platform;
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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

        new Thread(this::loadStats).start();
    }

    private void initializeWithErrHandling() {
        try {
            this.initialize();
        } catch (Exception exception) {
            logger.error(exception);
        }
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

    private BigDecimal getTotalSpent(UserModel user) {
        var purchaseHistory = user.getPurchaseHistory();
        var totalSpent = new BigDecimal(0);

        for(var purchase : purchaseHistory) {
            totalSpent = totalSpent.add(purchase.getCost());
        }

        return totalSpent;
    }

    private BigDecimal getTotalEarned(UserModel user) {
        var sellHistory = user.getSellHistory();
        BigDecimal totalEarned = new BigDecimal(0);

        for(var action : sellHistory) {
            totalEarned = totalEarned.add(action.getCost());
        }

        return totalEarned;
    }

    private void setNetStats(UserModel user) {

        var netWorth = new BigDecimal(0);

        var currencies = serviceHandler.getMarketManager().getCurrencies();
        var wallet = user.getWallet();

        var mostExpensive = new BigDecimal(0);
        var mostExpensiveName = "";

        for(var walletCurrency : wallet) {

            var currencyModel = currencies.stream().filter(currency -> currency.getId().equals(walletCurrency.getIdName())).findFirst();

            if(currencyModel.isPresent()) {

                var price = currencyModel.get().getPriceUsd().multiply(walletCurrency.getAmount());//currencyModel.get().getPriceUsd() * walletCurrency.getAmount();
                netWorth = netWorth.add(price);

                var isPriceGreaterOrEqual = price.compareTo(mostExpensive);
                if(isPriceGreaterOrEqual  == 0 || isPriceGreaterOrEqual == 1) {
                    mostExpensive = price;
                    mostExpensiveName = walletCurrency.getName();
                }

            }
        }

        netWorthLabel.setText(String.format("$%s",serviceHandler.formatNumber(netWorth)));
        mostValuableLabel.setText(mostExpensiveName);
        numberOfAssetsLabel.setText(String.format("%d",user.getWallet().size()));

        var spent = getTotalSpent(user);
        var earned = getTotalEarned(user);

        setTotalSpentLabel(spent);
        setTotalEarnedLabel(earned.subtract(spent));
    }

    private void setTotalSpentLabel(BigDecimal spent) {
        totalSpentLabel.setText(String.format("$%s",serviceHandler.formatNumber(spent)));
    }

    private void setTotalEarnedLabel(BigDecimal profit) {

        var isProfit = profit.compareTo(new BigDecimal(0)) == -1;

        if(isProfit) {
            totalEarnedLabel.getStyleClass().add("spent");
        } else {
            totalEarnedLabel.getStyleClass().add("profit");
        }

        totalEarnedLabel.setText(String.format("$%s",serviceHandler.formatNumber(profit)));
    }

    private void resetBox() {
        vBox.getChildren().clear();
        this.vBox.getChildren().add(headerStatsGrid);
        this.vBox.getChildren().add(statsGrid);
        this.vBox.getChildren().add(headerGrid);
    }

    private void loadHistory(Set<ActionHistoryModel> transactionHistory, StatsComponent.ActionType type) {
        for(var action : transactionHistory) {
            loadStatsComponentWithErrHandling(action,type);
        }
    }

    private void loadStats() {
        try {

            var user = serviceHandler.getUserDao()
                    .getEntity(serviceHandler.getSession().getActiveUserId()).get();

            var purchaseHistory = new HashSet<ActionHistoryModel>(user.getPurchaseHistory());
            var sellHistory = new HashSet<ActionHistoryModel>(user.getSellHistory());

            Platform.runLater(() -> {
                this.resetBox();
                this.loadHistory(purchaseHistory,StatsComponent.ActionType.PURCHASE);
                this.loadHistory(sellHistory,StatsComponent.ActionType.SELL);
                this.setNetStats(user);
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
