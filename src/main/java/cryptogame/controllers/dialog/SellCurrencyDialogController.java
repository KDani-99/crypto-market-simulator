package cryptogame.controllers.dialog;

import cryptogame.containers.CryptoCurrency;
import cryptogame.model.exception.EntityDoesNotExistException;
import cryptogame.model.models.CryptoCurrencyModel;
import cryptogame.model.models.UserModel;
import cryptogame.model.services.Service;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SellCurrencyDialogController extends BaseDialogController {

    private static final Logger logger = LogManager.getLogger(SellCurrencyDialogController.class);

    private CryptoCurrencyModel cryptoCurrencyModel;

    @Autowired
    public SellCurrencyDialogController(Service serviceHandler) {
        super(serviceHandler);
    }

    private Optional<CryptoCurrency> getSelectedCurrency() {
        return serviceHandler.getMarketManager().getCurrencies()
                .stream()
                .filter(cryptoCurrency -> cryptoCurrency.getId().equals(cryptoCurrencyModel.getIdName()))
                .findFirst();
    }

    @Override
    protected void bindButton() {
        this.actionButton.setOnMouseClicked(event -> {
            try {

                // Get amount and parse it
                var amount = new BigDecimal(amountTextField.getText());

                var compareZeroToAmount = new BigDecimal(0).compareTo(amount);

                if(compareZeroToAmount > -1) {
                    throw new IllegalArgumentException("Unable to sell 0 or less item");
                }

                // Get user object
                var userId = serviceHandler.getSession().getActiveUserId();
                var user = serviceHandler.getUserDao().getEntity(userId); // get id from session
                if(user.isEmpty()) {
                    throw new EntityDoesNotExistException(UserModel.class);
                }

                var selectedCurrency = getSelectedCurrency();
                if(selectedCurrency.isEmpty()) {
                    throw new IllegalArgumentException("The selected currency is unavailable to be sold at this time.");
                }

                var isAmountGreater = amount.compareTo(cryptoCurrencyModel.getAmount()) > 0;

                if(isAmountGreater) {
                    throw new IllegalArgumentException(String.format("You can't sell more `%s `than you have! You have = %s - Input = %s",
                            cryptoCurrencyModel.getIdName(),
                            serviceHandler.formatNumber(cryptoCurrencyModel.getAmount()),
                            serviceHandler.formatNumber(amount)));
                }

                var price = amount.multiply(selectedCurrency.get().getPriceUsd());//amount * selectedCurrency.get().getPriceUsd();

                // Sell the selected currency
                serviceHandler.getUserDao().sellCurrency(user.get(),amount,selectedCurrency.get());

                refreshData();

                logger.info(
                        String.format("Sold %f * `%s` for $%s @ %f by `%s`",amount,selectedCurrency.get().getName(),serviceHandler.formatNumber(price), selectedCurrency.get().getPriceUsd(), user.get().getUsername())
                );

                refreshData();

                serviceHandler.getSceneManager().closeAllDialog();

            } catch (Exception exception) {
                serviceHandler.getSceneManager()
                        .createAlert(Alert.AlertType.ERROR,"An error has occurred",exception.getMessage());
                logger.error(exception);
            }
        });
    }

    private void setCryptoDetails(String name,BigDecimal price) {
        headerLabel.setText(
                String.format("Sell `%s` @ $%s",name,serviceHandler.formatNumber(price))
        );
    }

    public void setCryptoCurrencyModel(CryptoCurrencyModel cryptoCurrencyModel) {

        this.cryptoCurrencyModel = cryptoCurrencyModel;

        var selected = getSelectedCurrency();

        if(selected.isEmpty()) {
            return;
        }

        setCryptoDetails(cryptoCurrencyModel.getName(), selected.get().getPriceUsd());
    }

}
