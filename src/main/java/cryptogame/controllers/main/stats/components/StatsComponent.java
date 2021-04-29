package cryptogame.controllers.main.stats.components;

import cryptogame.controllers.Controller;
import cryptogame.models.ActionHistoryModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StatsComponent implements Controller {

    public enum ActionType {
        PURCHASE,
        SELL
    }

    private ActionHistoryModel action;
    private ActionType actionType;

    @FXML private GridPane gridPane;

    @FXML private Label transactionIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label transactionTypeLabel;
    @FXML private Label costLabel;
    @FXML private Label amountLabel;
    @FXML private Label actionDateLabel;

    @Override
    public Node getRoot() {
        return gridPane;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public void initialize(){

        if(action == null) return;

        this.bindData();
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    private void bindData() {
        this.formatTransactionId();
        this.formatName();
        this.formatCost();
        this.formatAmount();
        this.formatActionDate();
        this.formatTransactionType();
    }

    private void formatTransactionId() {
        this.transactionIdLabel.setText(Long.toString(action.getId()));
    }
    private void formatName() {
        this.nameLabel.setText(action.getName());
    }
    private void formatCost() {
        this.costLabel.setText(String.format("$%f",action.getCost()));
    }
    private void formatAmount() {
        this.amountLabel.setText(String.format("%.6f",action.getAmount()));
    }
    private void formatActionDate() {
        Date date = new Date(this.action.getActionTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        this.actionDateLabel.setText(
                dateFormat.format(date)
        );
    }
    private void formatTransactionType() {
        if(actionType.equals(ActionType.PURCHASE)) {
            this.transactionTypeLabel.setText("Purchase");
            this.gridPane.getStyleClass().add("purchased");
        } else {
            this.transactionTypeLabel.setText("Sold");
            this.gridPane.getStyleClass().add("sold");
        }
    }

    public void setAction(ActionHistoryModel action) {
        this.action = action;
    }
}
