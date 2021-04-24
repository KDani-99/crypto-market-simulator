package cryptogame.controller.main.market;

import cryptogame.Main;
import cryptogame.common.Initializable;
import cryptogame.containers.CryptoCurrency;
import cryptogame.controller.main.market.components.CurrencyComponent;
import cryptogame.service.exception.ValidationException;
import cryptogame.service.manager.market.MarketManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MarketController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;

    @FXML
    private Label rankHeaderLabel;

    private boolean sorted = true;
    private MarketManager marketManager;

    public void setMarketManager(MarketManager marketManager) {
        this.marketManager = marketManager;
    }

    @Override
    public void initialize() {

        if(this.marketManager == null) return; // TODO: Fix init call

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        rankHeaderLabel.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                try {

                    sorted = !sorted;
                    reOrderMarketView();

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });

        this.loadMarket();
    }

    private void changeRankHeaderOrderIndicator() {

        var text = rankHeaderLabel.getText();
        text = text.substring(0,text.length()-2);

        if(sorted) {
            rankHeaderLabel.setText(text + " △");
        } else {

            rankHeaderLabel.setText(text + " ▽");
        }
    }

    private void reOrderMarketView() {

        changeRankHeaderOrderIndicator();

        var children = new ArrayList<>(this.vBox.getChildren());

        var sortedList = children.stream().sorted((child,child2) -> {

            var controller = (CurrencyComponent)child.getProperties().get("foo");
            if(controller == null) {
                return 0;
            }

            var controller2 = (CurrencyComponent)child2.getProperties().get("foo");
            if(controller2 == null) {
                return 0;
            }

            return controller2.getCurrency().compareTo(controller.getCurrency()) * (sorted ? -1 : 1);
        }).collect(Collectors.toList());

        this.vBox.getChildren().clear();
        this.vBox.getChildren().addAll(sortedList);
    }

    private void loadMarket() {
        try {
            var currencies = sorted
                    ?
                    marketManager.getCurrencies().stream().sorted().collect(Collectors.toList())
                    :
                    marketManager.getCurrencies().stream().sorted(Comparator.comparingInt(CryptoCurrency::getRank).reversed()).collect(Collectors.toList());

            for(var currency : currencies) {
                var loader = new FXMLLoader(Main.class.getResource("/views/app/components/market/components/CurrencyComponent.fxml"));
                Node node = loader.load();

                var nodeController = (CurrencyComponent)loader.getController();
                nodeController.setCurrency(currency);
                nodeController.initialize();

                vBox.getChildren().add(node);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
