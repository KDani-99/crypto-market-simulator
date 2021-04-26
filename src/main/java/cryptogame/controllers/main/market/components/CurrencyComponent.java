package cryptogame.controllers.main.market.components;

import cryptogame.common.Initializable;
import cryptogame.containers.CryptoCurrency;
import cryptogame.controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class CurrencyComponent implements Initializable, Controller {

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
    public CryptoCurrency getCurrency() {
        return this.currency;
    }

    @Override
    public void initialize() {
        if(currency == null) return;

        this.bindData();
    }

    private void bindData() {

        this.formatRank();

        this.formatName();

        this.formatPrice();

        this.formatMarketCap();

        this.formatSupply();

        this.formatChange();
    }

    private String formatBigNumber(double n) {
        if(n >= 1_000_000_000) {
            return String.format("$%.2fb",n / 1_000_000_000d);
        } else if(n >= 1_000_000) {
            return String.format("$%.2fm",n / 1_000_000d);
        } else {
            return String.format("$%.2f",n);
        }
    }

    private void formatRank() {
        this.rankLabel.setText("#"+currency.getRank());
    }

    private void formatName() {
        this.nameLabel.setText(currency.getName());
    }

    private void formatSupply() {
        var supply = currency.getSupply();
        var text = formatBigNumber(supply);
        this.supplyLabel.setText(text);
    }

    private void formatPrice() {
        var price = currency.getPriceUsd();

        this.priceLabel.setText(String.format("$%.2f",price));
    }

    private void formatMarketCap() {
        var marketCap = currency.getMarketCapUsd();
        var text = formatBigNumber(marketCap);
        this.marketCapLabel.setText(text);
    }

    private void formatChange() {

        this.changePercentLabel.setText(String.format("%.2f",currency.getChangePercent24Hr()) + "%");

        if(currency.getChangePercent24Hr() >= 0.0d) {
            changePercentLabel.getStyleClass().add("posChangePercent");
        } else {
            changePercentLabel.getStyleClass().add("negChangePercent");
        }
    }

    @Override
    public Node getRoot() {
        return null;
    }
}
