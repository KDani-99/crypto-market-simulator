package cryptogame.services.manager.market;

import cryptogame.containers.CryptoCurrency;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component("marketManager")
public class MarketManagerImplementation implements MarketManager {

    private Duration timeout = Duration.ofMinutes(1);
    private HttpClient httpClient;
    private String apiURL = "https://api.coincap.io/v2";
    private ObjectMapper mapper = new ObjectMapper();

    private final HashMap<String, CryptoCurrency> currencies = new HashMap<>();

    private long previousRefreshTimestamp = 0L;

   public MarketManagerImplementation() throws Exception {
        this.httpClient = buildHttpClient();

        // test
        this.refreshAssets();
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
    public void refreshAssets() throws Exception {

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
    }
    @Override
    public Collection<CryptoCurrency> getCurrencies() {
        return this.currencies.values();
    }

}
