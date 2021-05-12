package cryptogame.controllers.main.market.components;

import cryptogame.containers.CryptoCurrency;
import cryptogame.controllers.Controller;
import cryptogame.controllers.dialog.PurchaseDialogController;
import cryptogame.controllers.scene.SceneManager;
import cryptogame.model.services.Service;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CurrencyComponent implements Controller {

    private static final Logger logger = LogManager.getLogger(CurrencyComponent.class);

    private CryptoCurrency currency;

    @FXML private GridPane gridPane;
    @FXML private Label rankLabel;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Label marketCapLabel;
    @FXML private Label supplyLabel;
    @FXML private Label changePercentLabel;
    @FXML private Button purchaseButton;

    private final Service serviceHandler;
    private final SceneManager sceneManager;

    @Autowired
    public CurrencyComponent(Service serviceHandler, SceneManager sceneManager) {
        this.serviceHandler = serviceHandler;
        this.sceneManager = sceneManager;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    @Override
    public void initialize() {
        if(currency == null) return;

        this.bindData();
        this.bindPurchaseButton();
    }

    private void bindData() {

        this.formatRank();

        this.formatName();

        this.formatPrice();

        this.formatMarketCap();

        this.formatSupply();

        this.formatChange();
    }

    private String formatBigNumber(BigDecimal number) {

        var compareNumberToBillion = number.compareTo(new BigDecimal(1_000_000_000));
        var compareNumberToMillion = number.compareTo(new BigDecimal(1_000_000));

        if(compareNumberToBillion > -1) {
            return String.format("$%.2fb",number.divide(new BigDecimal(1_000_000_000), RoundingMode.FLOOR));
        } else if(compareNumberToMillion > -1) {
            return String.format("$%.2fm",number.divide(new BigDecimal(1_000_000), RoundingMode.FLOOR));
        } else {
            return String.format("$%.2f",number);
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

        this.priceLabel.setText(String.format("$%s",serviceHandler.formatNumber(price)));
    }

    private void formatMarketCap() {
        var marketCap = currency.getMarketCapUsd();
        var text = formatBigNumber(marketCap);
        this.marketCapLabel.setText(text);
    }

    private void formatChange() {

        this.changePercentLabel.setText(String.format("%.2f",currency.getChangePercent24Hr()) + "%");

        if(currency.getChangePercent24Hr() == null) {
            return;
        }

        var compareChangePercentToZero = new BigDecimal(0).compareTo(currency.getChangePercent24Hr()) < 0;

        if(compareChangePercentToZero) {
            changePercentLabel.getStyleClass().add("posChangePercent");
        } else {
            changePercentLabel.getStyleClass().add("negChangePercent");
        }
    }

    private void bindPurchaseButton() {
        this.purchaseButton.setOnMouseClicked(event -> {
            try {
                var purchaseWindow = (PurchaseDialogController) sceneManager
                        .createPurchaseWindow();

                purchaseWindow.setCurrencyContainer(currency);
            } catch (Exception exception) {
                logger.error(exception);
            }
        });
    }

    @Override
    public Node getRoot() {
        return this.gridPane;
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}
