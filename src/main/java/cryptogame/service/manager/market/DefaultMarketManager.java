package cryptogame.service.manager.market;

import cryptogame.containers.CryptoCurrency;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultMarketManager implements MarketManager {

    private final Duration timeout = Duration.ofMinutes(1);
    private final HttpClient httpClient;
    private final String apiURL = "https://api.coincap.io/v2";
    private final ObjectMapper mapper = new ObjectMapper();

    private final HashMap<String, CryptoCurrency> currencies = new HashMap<>();
    private final HashMap<String, CryptoCurrency> previousCurrencies = new HashMap<>();

    private long previousRefreshTimestamp = 0L;

    public DefaultMarketManager() throws Exception {
        this.httpClient = buildHttpClient();

        // test
        this.refreshAssets();
    }

    private HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
               // .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
               // .authenticator(Authenticator.getDefault())
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

    @Override
    public void refreshAssets() throws Exception {

        previousCurrencies.replaceAll((key, value) -> currencies.get(key));
        currencies.clear();

        String ep = "assets";
        var request = buildHttpRequest(ep);

        var result = httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .get();
                /*.thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .get();*/

        var assets = mapper.readTree(result.body()).get("data");
        for(var currency : assets) {
            var tmp = mapper.treeToValue(currency,CryptoCurrency.class);
            currencies.put(tmp.getId(),tmp);
        }
        System.out.println("Length: " + currencies.size());
    }
    @Override
    public Collection<CryptoCurrency> getCurrencies() {
        return this.currencies.values();
    }

}
