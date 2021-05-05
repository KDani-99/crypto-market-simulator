package cryptogame.controllers.main.market;

import cryptogame.common.Initializable;
import cryptogame.containers.CryptoCurrency;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.model.services.Service;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MarketController implements Initializable, Controller {

    private static final Logger logger = LogManager.getLogger(MarketController.class);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;

    @FXML private Label rankHeaderLabel;

    @FXML private Label remaningTimeLabel;

    private boolean sorted = true;

    private final Service serviceHandler;

    private ScheduledExecutorService executorService;

    private boolean isInitialized;

    @Autowired
    public MarketController(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @FXML
    public void initialize() {

        if(isInitialized) return;

        this.setWindowProperties();

        this.bindRankSorting();

        this.serviceHandler.getMarketManager().startAssetLoadingService();
        this.loadMarketWhenReady();

        isInitialized = true;
    }

    private void initMarket() {
        this.setupRemainingRefreshTimeDisplay();
        this.loadMarket();
    }

    private void setWindowProperties() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);
    }
    private ScheduledExecutorService createExecutor() {
        return Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true); // this will make it shut down on exit
            return thread;
        });
    }

    private void loadMarketWhenReady() {

        Thread thread = new Thread(()->{
            while(!serviceHandler.getMarketManager().hasLoaded()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException exception) {
                    logger.error(exception);
                }
            }
            Platform.runLater(this::initMarket);
        });

        thread.start();

    }

    private void setupRemainingRefreshTimeDisplay() {
        Runnable marketLoadRunnable = () -> Platform.runLater(this::setNextRefreshTimeText);

        executorService = createExecutor();

        executorService.scheduleAtFixedRate(marketLoadRunnable, 0,1, TimeUnit.SECONDS);
    }

    private void setNextRefreshTimeText() {
        var minutes = serviceHandler.getMarketManager().getRemainingTimeUntilRefresh() / (60 * 1000);
        remaningTimeLabel.setText("Next refresh in: " + minutes + " minute(s)");
    }

    private void bindRankSorting() {
        rankHeaderLabel.setOnMouseClicked(event -> {
            try {

                sorted = !sorted;
                reOrderMarketView();

            } catch (Exception exception) {
                logger.error(exception);
            }
        });
    }

    private void changeRankHeaderOrderIndicator() {

        var text = rankHeaderLabel.getText();
        text = text.substring(0,text.length()-2);

        if(sorted) {
            rankHeaderLabel.setText(text + " △");
        } else {

            rankHeaderLabel.setText(text + " ▽");
        }
    }

    private void reOrderMarketView() {

        changeRankHeaderOrderIndicator();

        var children = new ArrayList<>(this.vBox.getChildren());

        var sortedList = children.stream().sorted((child,child2) -> {

            var rankNode = (Label)child.lookup("#rankLabel");
            if(rankNode == null) {
                return 0;
            }

            var rankNode2 = (Label)child2.lookup("#rankLabel");
            if(rankNode2 == null) {
                return 0;
            }

            String rankStr = rankNode.getText().split("#")[1];
            String rankStr2 = rankNode2.getText().split("#")[1];

            int val1 = Integer.parseInt(rankStr);
            int val2 = Integer.parseInt(rankStr2);

            return Integer.compare(val1,val2) * (sorted ? 1 : -1);

        }).collect(Collectors.toList());

        this.vBox.getChildren().clear();
        this.vBox.getChildren().addAll(sortedList);
    }

    private void loadCurrencyComponent(CryptoCurrency currency) throws Exception {
        var currencyComponent = (CurrencyComponent) serviceHandler.getSceneManager().createCurrencyComponent();
        currencyComponent.setCurrency(currency);
        currencyComponent.initialize();

        vBox.getChildren().add(currencyComponent.getRoot());
    }

    private void loadCurrencyComponentWithErrHandling(CryptoCurrency currency) {
        try {
            loadCurrencyComponent(currency);
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    private void loadMarket() {
        var currencies = sorted
                ?
                serviceHandler.getMarketManager().getCurrencies().stream().sorted().collect(Collectors.toList())
                :
                serviceHandler.getMarketManager().getCurrencies().stream().sorted(Comparator.comparingInt(CryptoCurrency::getRank).reversed()).collect(Collectors.toList());

        for(var currency : currencies) {
            loadCurrencyComponentWithErrHandling(currency);
        }
    }

    @Override
    public Node getRoot() {
        return this.scrollPane;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public void onExit() {
        if(executorService != null)
            executorService.shutdownNow();
    }

}
