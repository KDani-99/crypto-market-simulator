package cryptogame.controllers.main;

import cryptogame.controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;

public class HomeController implements Controller {

    @FXML public AreaChart<Double,Double>  areaChart;

    @Override
    public void initialize() throws Exception {

    }
    @Override
    public Node getRoot() {
        return this.areaChart;
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}
