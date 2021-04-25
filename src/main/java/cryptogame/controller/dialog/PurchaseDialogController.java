package cryptogame.controller.dialog;

import cryptogame.common.Initializable;
import cryptogame.containers.CurrencyContainer;
import cryptogame.dao.user.UserDao;
import cryptogame.exception.EntityDoesNotExistException;
import cryptogame.jpa.entities.User;
import cryptogame.service.exception.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PurchaseDialogController implements Initializable {

    @FXML private Label headerLabel;
    @FXML private TextField amountTextField;
    @FXML private Button purchaseButton;

    private UserDao userDao;
    private CurrencyContainer currency;

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
                    var user = userDao.getEntity(); // get id from session
                    if(user.isEmpty()) {
                        throw new EntityDoesNotExistException(User.class);
                    }

                    // Purchase the given currency
                    userDao.purchaseCurrency(user.get(),amount,currency);

                } catch (Exception ex) {
                    // TODO: log error ! -<> user factory
                    //showError(ex.getMessage(),ex.getMessage());
                    System.out.println(ex.toString());
                }
            }
        });
    }

    private void makeTextFieldAcceptNumberOnly() {
        amountTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {

                    var value = Double.parseDouble(t1);
                    amountTextField.setText(Double.toString(value));

                } catch (Exception ex) {
                    amountTextField.setText(t1.replaceAll("[^\\d^\\.]", ""));
                    // ignore
                }
               /* if (!t1.matches("\\d*")) {
                    amountTextField.setText(t1.replaceAll("[^\\d]", ""));
                }*/
            }
        });
    }

    private void setCryptoDetails(String name,double price) {
        headerLabel.setText(
                String.format("Purchase %s @ $%.2f",name,price)
        );
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setCurrencyContainer(CurrencyContainer currency) {
        this.currency = currency;
        this.setCryptoDetails(currency.getName(), currency.getPriceUsd());
    }
}
