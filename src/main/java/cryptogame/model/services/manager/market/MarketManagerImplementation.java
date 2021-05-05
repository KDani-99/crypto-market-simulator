package cryptogame.model.services.manager.market;

import cryptogame.containers.CryptoCurrency;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import cryptogame.controllers.main.market.MarketController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("marketManager")
public class MarketManagerImplementation implements MarketManager {

    private static final Logger logger = LogManager.getLogger(MarketController.class);
    private static final long TIMEOUT = 10 * 60; // 600 seconds

    private final Duration timeout = Duration.ofMinutes(1);
    private final HttpClient httpClient;
    private final String apiURL = "https://api.coincap.io/v2";
    private final ObjectMapper mapper = new ObjectMapper();

    private final HashMap<String, CryptoCurrency> currencies = new HashMap<>();

    private ScheduledExecutorService executorService;

    private long refreshTimestamp;
    private boolean hasLoaded = false;

   public MarketManagerImplementation() {
        this.httpClient = buildHttpClient();
    }

    private HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    private HttpRequest buildHttpRequest(String ep) {
        return HttpRequest.newBuilder()
                .uri(URI.create(apiURL + "/" + ep))
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }

    private void clearCurrencies() {
        currencies.clear();
    }

    @Override
    public void loadAssets() {
       try {
           refreshTimestamp = Calendar.getInstance().getTimeInMillis();

           clearCurrencies();

           String ep = "assets";
           var request = buildHttpRequest(ep);

           var result = httpClient
                   .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                   .get();

           var assets = mapper.readTree(result.body()).get("data");
           for(var currency : assets) {
               var tmp = mapper.treeToValue(currency,CryptoCurrency.class);
               currencies.put(tmp.getId(),tmp);
           }

           hasLoaded = true;
       } catch (Exception exception) {
            logger.error(exception);
       }
    }

    @Override
    public void startAssetLoadingService() {
        Runnable marketLoadRunnable = this::loadAssets;

        executorService = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true); // this will make it shut down on exit
            return thread;
        });
        executorService.scheduleAtFixedRate(marketLoadRunnable, 0,TIMEOUT, TimeUnit.SECONDS);

        logger.info("Started asset loader service");
    }

    @Override
    public void stopAssetLoadingService() {
        executorService.shutdown();
        logger.info("Stopped asset loader service");
    }

    @Override
    public Collection<CryptoCurrency> getCurrencies() {
        return this.currencies.values();
    }

    @Override
    public long getRemainingTimeUntilRefresh() {
        return (refreshTimestamp + TIMEOUT * 1000) - Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public boolean hasLoaded() {
        return hasLoaded;
    }

    @Override
    public void onExit() {
       if(executorService != null) executorService.shutdownNow();
    }
}
