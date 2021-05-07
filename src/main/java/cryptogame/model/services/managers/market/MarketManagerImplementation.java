package cryptogame.model.services.managers.market;

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
import cryptogame.model.services.managers.scene.SceneManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * The implementation of the {@link MarketManager} interface.
 */
@Component
public class MarketManagerImplementation implements MarketManager {

    /**
     * Logger for logging.
     */
    private static final Logger logger = LogManager.getLogger(MarketController.class);
    /**
     * The default timeout until the next refresh.
     */
    private static final long TIMEOUT = 300; // 300 seconds
    /**
     * The URL of the API.
     */
    private static final String API_URL = "https://api.coincap.io/v2";
    /**
     * Request timeout duration.
     */
    private final Duration timeout = Duration.ofMinutes(1);
    /**
     * The http client for sending requests.
     */
    private final HttpClient httpClient;
    /**
     * The JSON object mapper.
     */
    private final ObjectMapper mapper = new ObjectMapper();
    /**
     * A {@link HashMap} that contains all of the available currencies.
     */
    private final HashMap<String, CryptoCurrency> currencies = new HashMap<>();
    /**
     * Executor service.
     * Asset loading service.
     */
    private ScheduledExecutorService executorService;
    /**
     * The timestamp of the previous refresh.
     */
    private long refreshTimestamp;
    /**
     * Whether the market has been loaded into the context.
     */
    private boolean hasLoaded = false;

    /**
     * Creates a new {@link MarketManagerImplementation} instance
     * and sets the default properties of the class.
     */
    public MarketManagerImplementation() {
        this.httpClient = buildHttpClient();
    }

    /**
     * A helper method that creates a new pre-configured {@link HttpClient}.
     *
     * @return configured {@link HttpClient} instance
     */
    private HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    /**
     * Builds a new http request with the specified endpoint.
     *
     * @param endpoint the api endpoint
     * @return configured {@link HttpRequest} instance
     */
    private HttpRequest buildHttpRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + endpoint))
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }

    /**
     * Clears the currencies from the collection.
     */
    private void clearCurrencies() {
        currencies.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAssets() {
       try {
           refreshTimestamp = Calendar.getInstance().getTimeInMillis();

           clearCurrencies();

           String endpoint = "assets";
           var request = buildHttpRequest(endpoint);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAssetLoadingService() {

        if(executorService != null) {
            stopAssetLoadingService();
        }

        Runnable marketLoadRunnable = this::loadAssets;

        executorService = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true); // this will make it shut down on exit
            return thread;
        });
        executorService.scheduleAtFixedRate(marketLoadRunnable, 0,TIMEOUT, TimeUnit.SECONDS);

        logger.info("Started asset loader service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopAssetLoadingService() {
        if(executorService != null) {
            executorService.shutdown();
            executorService = null;
            logger.info("Stopped asset loader service");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CryptoCurrency> getCurrencies() {
        return this.currencies.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getRemainingTimeUntilRefresh() {
        return (refreshTimestamp + TIMEOUT * 1000) - Calendar.getInstance().getTimeInMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLoaded() {
        return hasLoaded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
       refreshTimestamp = 0;
       hasLoaded = false;
       currencies.clear();
       stopAssetLoadingService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExit() {
       if(executorService != null) executorService.shutdownNow();
    }
}
