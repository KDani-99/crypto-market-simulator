package cryptogame.controllers.dialog;

import cryptogame.containers.CurrencyContainer;
import cryptogame.controllers.BaseController;
import cryptogame.controllers.Controller;
import cryptogame.model.exception.EntityDoesNotExistException;
import cryptogame.model.models.UserModel;
import cryptogame.model.services.Service;
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

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PurchaseDialogController extends BaseDialogController {

    private static final Logger logger = LogManager.getLogger(PurchaseDialogController.class);

    private CurrencyContainer currency;

    @Autowired
    public PurchaseDialogController(Service serviceHandler) {
        super(serviceHandler);
    }

    @Override
    protected void bindButton() {
        this.actionButton.setOnMouseClicked(event -> {
            try {

                // Get amount and parse it
                var amount = Double.parseDouble(amountTextField.getText());
                if(amount <= 0) {
                    throw new IllegalArgumentException("Unable to purchase 0 or less item");
                }

                // Get user object
                var userId = serviceHandler.getSession().getActiveUserId();
                var user = serviceHandler.getUserDao().getEntity(userId); // get id from session
                if(user.isEmpty()) {
                    throw new EntityDoesNotExistException(UserModel.class);
                }

                var price = amount * currency.getPriceUsd();
                if(user.get().getBalance() < price) {
                    throw new IllegalArgumentException("You can't afford to buy that much of the given currency");
                }

                // Purchase the given currency
                serviceHandler.getUserDao().purchaseCurrency(user.get(),amount,currency);

                refreshData();

                logger.info(
                        String.format("Purchased %f * `%s` for $%f @ %f by `%s`",amount,currency.getName(),price, currency.getPriceUsd(), user.get().getUsername())
                );

                serviceHandler.getSceneManager().closeAllDialog();

            }
            catch (Exception exception) {
                serviceHandler.getSceneManager()
                        .createAlert(Alert.AlertType.ERROR,"An error has occurred",exception.getMessage());
                logger.error(exception);
            }
        });
    }

    private void setCryptoDetails(String name,double price) {
        headerLabel.setText(
                String.format("Purchase `%s` @ $%.2f",name,price)
        );
    }

    public void setCurrencyContainer(CurrencyContainer currency) {
        this.currency = currency;
        this.setCryptoDetails(currency.getName(), currency.getPriceUsd());
    }
}
