package cryptogame.controllers.main.wallet.components;

import cryptogame.controllers.Controller;
import cryptogame.controllers.dialog.PurchaseDialogController;
import cryptogame.controllers.dialog.SellCurrencyDialogController;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.controllers.scene.SceneManager;
import cryptogame.model.models.CryptoCurrencyModel;
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

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WalletComponent implements Controller {

    private static final Logger logger = LogManager.getLogger(WalletComponent.class);

    private CryptoCurrencyModel currencyModel;
    private final Service serviceHandler;
    private final SceneManager sceneManager;

    @FXML private GridPane gridPane;

    @FXML private Label nameLabel;
    @FXML private Label amountLabel;
    @FXML private Label priceLabel;
    @FXML private Button sellButton;

    @Autowired
    public WalletComponent(Service serviceHandler, SceneManager sceneManager) {
        this.serviceHandler = serviceHandler;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize() {
        if(currencyModel == null) return;

        this.bindData();
        this.bindSellButton();
    }

    @Override
    public Node getRoot() {
        return this.gridPane;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    public void setCurrencyModel(CryptoCurrencyModel currencyModel) {
        this.currencyModel = currencyModel;
    }

    private void bindData() {
        this.formatName();
        this.formatPrice();
        this.formatAmount();
    }

    private void formatName() {
        this.nameLabel.setText(currencyModel.getIdName());
    }

    private void formatAmount() {
        this.amountLabel.setText(serviceHandler.formatNumber(currencyModel.getAmount()));
    }

    private void formatPrice() {
        var currencyOptional = serviceHandler.getMarketManager().getCurrencies()
                .stream()
                .filter(currency -> currency.getId().equals(currencyModel.getIdName()))
                .findFirst();

        var priceText = "$";

        if(currencyOptional.isEmpty()) {
            priceText = serviceHandler.formatNumber(new BigDecimal(0));
        } else {
            var price = currencyOptional.get().getPriceUsd().multiply(currencyModel.getAmount());
            priceText += serviceHandler.formatNumber(price);
        }

        this.priceLabel.setText(priceText);
    }

    private void bindSellButton() {
        this.sellButton.setOnMouseClicked(event -> {
            try {

                var purchaseWindow = (SellCurrencyDialogController) sceneManager
                        .createSellCurrencyWindow();

                purchaseWindow.setCryptoCurrencyModel(currencyModel);
            } catch (Exception exception) {
                logger.error(exception);
            }
        });
    }

}
