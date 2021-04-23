package cryptogame.controller.main.market;

import cryptogame.Main;
import cryptogame.common.Initializable;
import cryptogame.containers.CryptoCurrency;
import cryptogame.controller.main.market.components.CurrencyComponent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MarketController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;

    @Override
    public void initialize() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        try {
            for(int i=0;i<30;i++) {
                var loader = new FXMLLoader(Main.class.getResource("/views/app/components/market/components/CurrencyComponent.fxml"));
                Node n = loader.load();

                vBox.getChildren().add(n);

                var x = (CurrencyComponent)loader.getController();

                var test = new CryptoCurrency();
                test.setName("Bitcoin (BTC)");
                test.setRank(13);
                test.setMarketCapUsd(1212132.21);
                var percent = Math.random() * 10 - 10 + 1;

                test.setChangePercent24Hr(percent);

                x.setCurrency(test);
                x.initialize();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
