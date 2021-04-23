package cryptogame.controller.main.market.components;

import cryptogame.common.Initializable;
import cryptogame.containers.CryptoCurrency;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CurrencyComponent implements Initializable {

    private CryptoCurrency currency;

    @FXML private Label rankLabel;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Label marketCapLabel;
    @FXML private Label supplyLabel;
    @FXML private Label changePercentLabel;

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    @Override
    public void initialize() {
        if(currency == null) return;

        this.handleChange(); // @test
        this.bindCurrencyData();
    }

    private void bindCurrencyData() {

        this.rankLabel.setText("#"+currency.getRank());

        this.nameLabel.setText(currency.getName() + "(" + "$"+ currency.getId() +")");

        this.priceLabel.setText("$"+currency.getPriceUsd());

        this.marketCapLabel.setText("$"+currency.getMarketCapUsd()); // !! -> add 'b'

        this.supplyLabel.setText(Double.toString(currency.getSupply())); // !! -> add 'b'
    }

    private void handleChange() {
        if(currency.getChangePercent24Hr() >= 0.0d) {
            changePercentLabel.getStyleClass().add("posChangePercent");
        } else {
            changePercentLabel.getStyleClass().add("negChangePercent");
        }
    }

}
