package cryptogame.controllers.main.market;

import cryptogame.Main;
import cryptogame.common.Initializable;
import cryptogame.containers.CryptoCurrency;
import cryptogame.controllers.Controller;
import cryptogame.controllers.dialog.PurchaseDialogController;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.services.Service;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class MarketController implements Initializable, Controller {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;

    @FXML
    private Label rankHeaderLabel;

    private boolean sorted = true;

    private Service serviceHandler;

    private boolean initialized = false;

    @Autowired
    public MarketController(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initialize() {

        if(initialized) return; // TODO: Fix init call

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

        initialized = true;
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

            var rankNode = (Label)child.lookup("#rankLabel");
            if(rankNode == null) {
                return 0;
            }

            var rankNode2 = (Label)child2.lookup("#rankLabel");
            if(rankNode2 == null) {
                return 0;
            }

            String rankStr = rankNode.getText().split("#")[1];
            String rankStr2 = rankNode2.getText().split("#")[1];

            int val1 = Integer.parseInt(rankStr);
            int val2 = Integer.parseInt(rankStr2);

            return Integer.compare(val1,val2) * (sorted ? 1 : -1);

        }).collect(Collectors.toList());

        this.vBox.getChildren().clear();
        this.vBox.getChildren().addAll(sortedList);
    }

    private void loadMarket() {
        try {
            var currencies = sorted
                    ?
                    serviceHandler.getMarketManager().getCurrencies().stream().sorted().collect(Collectors.toList())
                    :
                    serviceHandler.getMarketManager().getCurrencies().stream().sorted(Comparator.comparingInt(CryptoCurrency::getRank).reversed()).collect(Collectors.toList());

            for(var currency : currencies) {

                // TODO: Load component from SceneManager
               /* System.out.println("LOAD");
                var loader = new FXMLLoader(Main.class.getResource("/views/app/components/market/components/CurrencyComponent.fxml"));
                Node node = loader.load();

                var nodeController = (CurrencyComponent)loader.getController();
                nodeController.setCurrency(currency);
                nodeController.initialize();

                vBox.getChildren().add(node);*/

                var currencyComponent = (CurrencyComponent) serviceHandler.getSceneManager().createCurrencyComponent();
                currencyComponent.setCurrency(currency);
                currencyComponent.initialize();

                vBox.getChildren().add(currencyComponent.getRoot());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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
}
