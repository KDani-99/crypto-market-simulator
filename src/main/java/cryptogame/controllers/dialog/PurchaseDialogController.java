package cryptogame.controllers.dialog;

import cryptogame.containers.CurrencyContainer;
import cryptogame.controllers.Controller;
import cryptogame.exception.EntityDoesNotExistException;
import cryptogame.models.UserModel;
import cryptogame.services.Service;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PurchaseDialogController implements Controller {

    @FXML private Label headerLabel;
    @FXML private TextField amountTextField;
    @FXML private Button purchaseButton;

    private final Service serviceHandler;

    private CurrencyContainer currency;

    @Autowired
    public PurchaseDialogController(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initialize() {
        this.makeTextFieldAcceptNumberOnly();
        this.bindPurchaseButton();
    }

    private void bindPurchaseButton() {
        this.purchaseButton.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
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

                    if(user.get().getBalance() < (amount * currency.getPriceUsd())) {
                        System.out.println("Amount: " + amount + "\t Price would be: " + (amount * currency.getPriceUsd()));
                        throw new IllegalArgumentException("You can't afford to buy that much");
                    }

                    // Purchase the given currency
                    serviceHandler.getUserDao().purchaseCurrency(user.get(),amount,currency);

                    serviceHandler.getSceneManager().getMainController().refreshUser();
                    serviceHandler.getSceneManager().getStatsController().refreshUser();

                } catch (Exception ex) {
                    // TODO: log error !
                    //showError(ex.getMessage(),ex.getMessage());
                    System.out.println(ex.toString() + "\n" + ex.getStackTrace() + "\n" + ex.getMessage());
                }
            }
        });
    }

    private void makeTextFieldAcceptNumberOnly() {
        amountTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
               if (!t1.matches("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?")) {
                    amountTextField.setText(t1.replaceAll("[^\\d.]", ""));
                }
            }
        });
    }

    private void setCryptoDetails(String name,double price) {
        headerLabel.setText(
                String.format("Purchase %s @ $%.2f",name,price)
        );
    }

    public void setCurrencyContainer(CurrencyContainer currency) {
        this.currency = currency;
        this.setCryptoDetails(currency.getName(), currency.getPriceUsd());
    }

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}
