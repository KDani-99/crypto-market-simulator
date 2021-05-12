package cryptogame.controllers.dialog;

import cryptogame.containers.CurrencyContainer;
import cryptogame.controllers.BaseController;
import cryptogame.controllers.Controller;
import cryptogame.controllers.scene.SceneManager;
import cryptogame.model.exception.EntityDoesNotExistException;
import cryptogame.model.models.UserModel;
import cryptogame.model.services.Service;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PurchaseDialogController extends BaseDialogController {

    private static final Logger logger = LogManager.getLogger(PurchaseDialogController.class);

    private CurrencyContainer currency;

    @Autowired
    public PurchaseDialogController(Service serviceHandler, SceneManager sceneManager) {
        super(serviceHandler, sceneManager);
    }

    @Override
    protected void bindButton() {
        this.actionButton.setOnMouseClicked(event -> {
            var amount = new BigDecimal(amountTextField.getText());
            new Thread(() -> purchaseAction(amount)).start();
        });

    }

    private void purchaseAction(BigDecimal amount) {
        try {

            var compareAmountToZero = new BigDecimal(0).compareTo(amount);

            if(compareAmountToZero > -1) {
                throw new IllegalArgumentException("Unable to purchase 0 or less item");
            }

            // Get user object
            var userId = serviceHandler.getSession().getActiveUserId();

            var user = serviceHandler.getUserDao().getEntity(userId); // get id from session
            if(user.isEmpty()) {
                throw new EntityDoesNotExistException(UserModel.class);
            }

            if(!user.get().canPurchaseGivenCurrency(currency, amount)) {
                throw new IllegalArgumentException("You can't afford to buy that much of the given currency");
            }

            var price = amount.multiply(currency.getPriceUsd());

            // Purchase the given currency
            serviceHandler.getUserDao().purchaseCurrency(user.get(),amount,currency);

            logger.info(
                    String.format("Purchased %f * `%s` for $%f @ %f by `%s`",amount,currency.getName(),price, currency.getPriceUsd(), user.get().getUsername())
            );

            // Call on main UI thread
            Platform.runLater(() -> {
                refreshData();
                sceneManager.closeAllDialog();
            });

        }
        catch (Exception exception) {

            Platform.runLater(() -> sceneManager.createAlert(Alert.AlertType.ERROR,"An error has occurred",exception.getMessage()));
            logger.error(exception);
        }
    }

    private void setCryptoDetails(String name,BigDecimal price) {
        headerLabel.setText(
                String.format("Purchase `%s` @ $%s",name,serviceHandler.formatNumber(price))
        );
    }

    public void setCurrencyContainer(CurrencyContainer currency) {
        this.currency = currency;
        this.setCryptoDetails(currency.getName(), currency.getPriceUsd());
    }
}
