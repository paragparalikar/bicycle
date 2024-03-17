package com.bicycle.client.kite.api;

import static com.bicycle.client.kite.KiteConstant.FORMATTER;
import static com.bicycle.client.kite.KiteConstant.URL_BARS;
import static com.bicycle.client.kite.KiteConstant.URL_BASE;
import static com.bicycle.client.kite.KiteConstant.URL_DASHBOARD;
import static com.bicycle.client.kite.KiteConstant.URL_HOLDINGS;
import static com.bicycle.client.kite.KiteConstant.URL_MARGIN;
import static com.bicycle.client.kite.KiteConstant.URL_ORDERS;
import static com.bicycle.client.kite.KiteConstant.URL_POSITIONS;
import static com.bicycle.client.kite.KiteConstant.URL_PROFILE;
import static com.bicycle.client.kite.KiteConstant.USER_AGENT_CHROME;
import com.bicycle.client.kite.KiteConstant;
import com.bicycle.client.kite.api.model.KiteCandle;
import com.bicycle.client.kite.api.model.KiteCandleSeries;
import com.bicycle.client.kite.api.model.KiteHolding;
import com.bicycle.client.kite.api.model.KiteInterval;
import com.bicycle.client.kite.api.model.KiteLimitType;
import com.bicycle.client.kite.api.model.KiteMargin;
import com.bicycle.client.kite.api.model.KiteOrder;
import com.bicycle.client.kite.api.model.KiteOrderId;
import com.bicycle.client.kite.api.model.KitePosition;
import com.bicycle.client.kite.api.model.KiteProfile;
import com.bicycle.client.kite.api.model.KiteQuote;
import com.bicycle.client.kite.api.model.KiteQuoteMode;
import com.bicycle.client.kite.api.model.KiteResponse;
import com.bicycle.client.kite.api.model.KiteSymbol;
import com.bicycle.client.kite.api.model.KiteTwofa;
import com.bicycle.client.kite.credentials.KiteCredentials;
import com.bicycle.client.kite.utils.Http;
import com.fasterxml.jackson.core.type.TypeReference;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

@Slf4j
public class KiteAsyncHttpApi implements KiteApi {
    
    private final UUID uuid;
    private volatile String enctoken;
    private final AsyncHttpClient httpClient;
    private final KiteCredentials credentials;
    private final KiteAsyncWebsocketApi webSocketClient;
    
    public KiteAsyncHttpApi(
            KiteCredentials credentials, 
            KiteDataPublisher kiteDataPublisher,
            ScheduledExecutorService scheduledExecutorService) {
        this.credentials = credentials;
        this.uuid = UUID.randomUUID();
        this.httpClient = Dsl
                .asyncHttpClient(Http.createAsyncHttpClientConfig()
                .setUserAgent(USER_AGENT_CHROME));
        this.webSocketClient = new KiteAsyncWebsocketApi(httpClient, credentials, 
                kiteDataPublisher, () -> Optional.ofNullable(enctoken).orElseGet(this::login), 
                scheduledExecutorService);
    }
    
    @Override
    public void init() {
        webSocketClient.start();
    }
    
    @Override
    public void close() throws Exception {
        if(null != webSocketClient) webSocketClient.close();
        if(null != httpClient) httpClient.close();
    }
    
    private BoundRequestBuilder withHeaders(BoundRequestBuilder builder) {
        builder.addHeader("Origin", URL_BASE)
                .addHeader("User-Agent", USER_AGENT_CHROME)
                .addHeader("Referer", URL_BASE + URL_DASHBOARD)
                .addHeader("Accept-Language", "en-US,en;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("x-kite-version", "3.0.7")
                .addHeader("x-kite-app-uuid", uuid.toString())
                .addHeader("x-kite-userid", credentials.username())
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("sec-fetch-site", "none")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-ch-ua-platform", "`\"Windows`\"")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"117\", \"Not;A=Brand\";v=\"8\", \"Chromium\";v=\"117\"");
        Optional.ofNullable(enctoken).ifPresent(token -> builder.setHeader("Authorization", "enctoken" + " " + enctoken));
        return builder;
    }
    
    @SneakyThrows
    private String getTotp(@NonNull String pin) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        final int secondOfMinute = localDateTime.getSecond();
        if(secondOfMinute >= 27 && secondOfMinute <= 29) Thread.sleep(30 - secondOfMinute);
        if(secondOfMinute >= 57 && secondOfMinute <= 59) Thread.sleep(60 - secondOfMinute);
        return String.format("%06d", new GoogleAuthenticator().getTotpPassword(pin)) ;
    }
    
    @Synchronized
    @SneakyThrows
    private String login() {
        if(null != enctoken) return enctoken;
        withHeaders(httpClient.prepareGet(KiteConstant.URL_BASE + "/"))
            .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .execute();
        final Response response = withHeaders(httpClient.preparePost(URL_BASE + KiteConstant.URL_LOGIN))
            .setHeader("Referer", URL_BASE + "/")
            .setHeader("x-kite-userid", credentials.username())
            .setHeader("Content-Type", "application/x-www-form-urlencoded")
            .addFormParam("user_id", credentials.username())
            .addFormParam("password", credentials.password())
            .execute().get();
        final KiteResponse<KiteTwofa> loginResponse = KiteConstant.JSON.readValue(
                response.getResponseBody(), 
                new TypeReference<KiteResponse<KiteTwofa>>(){});
        return twofa(loginResponse.getData());
    }
    
    private String twofa(KiteTwofa twofaInfo) throws IOException, InterruptedException, ExecutionException {
        if(null != enctoken) {
            return enctoken;
        }
        if(twofaInfo.isLocked()) {
            throw new IllegalStateException(String.format("Kite account is locked for %s. Manual intervention is required.", 
                    credentials.username()));
        }
        if(twofaInfo.isCaptcha()) {
            throw new IllegalStateException(String.format("Kite api has requested CAPTCHA for %s. Manual intervention is required.", 
                    credentials.username()));
        }
        withHeaders(httpClient.preparePost(URL_BASE + KiteConstant.URL_TWOFA))
            .setHeader("Referer", URL_BASE + "/")
            .setHeader("x-kite-userid", credentials.username())
            .setHeader("Content-Type", "application/x-www-form-urlencoded")
            .addFormParam("skip_session", "")
            .addFormParam("user_id", credentials.username())
            .addFormParam("request_id", twofaInfo.getRequestId())
            .addFormParam("twofa_type", twofaInfo.getTwofaType())
            .addFormParam("twofa_value", getTotp(credentials.pin()))
            .execute().get();
        this.enctoken = httpClient.getConfig().getCookieStore().getAll().stream()
                .filter(cookie -> "enctoken".equals(cookie.name()))
                .map(cookie -> cookie.value())
                .findFirst().orElseThrow();
        return this.enctoken;
    }
    
    @SneakyThrows
    private <T> T execute(BoundRequestBuilder builder, TypeReference<KiteResponse<T>> typeRef) {
        if(null == enctoken) login();
        final Response response = withHeaders(builder).execute().get();
        final int statusCode = response.getStatusCode();
        if(HttpStatus.SC_UNAUTHORIZED == statusCode || HttpStatus.SC_FORBIDDEN == statusCode) {
            enctoken = null;
            log.warn("Relogging in to kite for {} user, got {}", credentials.username(), response.getStatusText());
            return execute(builder, typeRef);
        }
        if(300 <= statusCode) {
            throw new KiteHttpClientException(statusCode, response.getStatusText());
        }
        final KiteResponse<T> kiteResponse = KiteConstant.JSON.readValue(response.getResponseBody(), typeRef);
        if(!"success".equalsIgnoreCase(kiteResponse.getStatus())) {
            final String message = String.join(" - ", 
                    response.getUri().toString(),
                    String.valueOf(statusCode),
                    response.getStatusText(),
                    kiteResponse.getStatus(), 
                    kiteResponse.getErrorType(), 
                    kiteResponse.getMessage());
            log.error(message);
            throw new KiteHttpClientException(statusCode, message);
        }
        return kiteResponse.getData();
    }
    
    @Override
    public List<KiteCandle> getData(KiteSymbol symbol, KiteInterval interval, ZonedDateTime from, ZonedDateTime to) {
        return getDataInternal(symbol, interval, from, to).getData();
    }
    
    private KiteCandleSeries getDataInternal(KiteSymbol symbol, KiteInterval interval, ZonedDateTime from, ZonedDateTime to) {
        final String url = String.join("/", URL_BASE + URL_BARS, 
                String.valueOf(symbol.getInstrumentToken()), interval.getText());
        final BoundRequestBuilder builder = httpClient.prepareGet(url)
                .addQueryParam("oi", "1")
                .addQueryParam("user_id", credentials.username())
                .addQueryParam("to", FORMATTER.format(to))
                .addQueryParam("from", FORMATTER.format(from));
        final KiteCandleSeries series = execute(builder, new TypeReference<KiteResponse<KiteCandleSeries>>(){});
        log.info("Downloaded {} candles from kite for timeframe {} symbol {} from {} to {}", 
                series.getData().size(), interval.name(), symbol.getName(), from, to);
        return series;
    }

    @Override
    public KiteProfile getProfile() {
        return execute(httpClient.prepareGet(URL_BASE + URL_PROFILE), new TypeReference<KiteResponse<KiteProfile>>(){});
    }

    @Override
    public KiteMargin getMargin() {
        return execute(httpClient.prepareGet(URL_BASE + URL_MARGIN), new TypeReference<KiteResponse<KiteMargin>>(){});
    }

    @Override
    public List<KiteHolding> getHoldings() {
        return execute(httpClient.prepareGet(URL_BASE + URL_HOLDINGS), new TypeReference<KiteResponse<List<KiteHolding>>>(){});
    }

    @Override
    public List<KitePosition> getPositions() {
        return execute(httpClient.prepareGet(URL_BASE + URL_POSITIONS), new TypeReference<KiteResponse<List<KitePosition>>>(){});
    }

    @Override
    public List<KiteOrder> getOrders() {
        return execute(httpClient.prepareGet(URL_BASE + KiteConstant.URL_ORDERS), new TypeReference<KiteResponse<List<KiteOrder>>>(){});
    }

    @Override
    public KiteOrderId create(KiteOrder order) {
        final String url = URL_BASE + KiteConstant.URL_ORDERS + "/" + order.getVariety().name();
        final BoundRequestBuilder builder = httpClient.preparePost(url)
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .addFormParam("user_id", credentials.username())
                .addFormParam("tradingsymbol", order.getTradingsymbol())
                .addFormParam("exchange", order.getExchange().name())
                .addFormParam("transaction_type", order.getTransactionType().name())
                .addFormParam("order_type", order.getLimitType().name())
                .addFormParam("quantity", String.valueOf(order.getQuantity()))
                .addFormParam("product", order.getProduct().name())
                .addFormParam("validity", order.getValidity().name())
                .addFormParam("disclosed_quantity", String.valueOf(order.getDisclosedQuantity()));
        if(KiteLimitType.LIMIT.equals(order.getLimitType()) || KiteLimitType.SL.equals(order.getLimitType())) {
            builder.addFormParam("price", String.valueOf(order.getPrice()));
        }
        if(KiteLimitType.SL.equals(order.getLimitType()) || KiteLimitType.SLM.equals(order.getLimitType())) {
            builder.addFormParam("trigger_price", String.valueOf(order.getTriggerPrice()));
        }
        if(0 < order.getStoploss()) {
            builder.addFormParam("stoploss", String.valueOf(order.getStoploss()));
        }
        if(0 < order.getSquareoff()) {
            builder.addFormParam("squareoff", String.valueOf(order.getSquareoff()));
        }
        if(0 < order.getTrailingStoploss()) {
            builder.addFormParam("trailing_stoploss", String.valueOf(order.getTrailingStoploss()));
        }
        return execute(builder, new TypeReference<KiteResponse<KiteOrderId>>(){});
    }

    @Override
    public KiteOrderId update(KiteOrder order) {
        final String url = URL_BASE + URL_ORDERS  + "/" + order.getVariety().name() + "/" + order.getOrderId();
        final BoundRequestBuilder builder = httpClient.preparePut(url)
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .addFormParam("order_type", order.getLimitType().name())
                .addFormParam("quantity", String.valueOf(order.getQuantity()))
                .addFormParam("validity", order.getValidity().name())
                .addFormParam("order_type", order.getLimitType().name())
                .addFormParam("disclosed_quantity", String.valueOf(order.getDisclosedQuantity()));
        if(KiteLimitType.LIMIT.equals(order.getLimitType()) || KiteLimitType.SL.equals(order.getLimitType())) {
            builder.addFormParam("price", String.valueOf(order.getPrice()));
        }
        if(KiteLimitType.SL.equals(order.getLimitType()) || KiteLimitType.SLM.equals(order.getLimitType())) {
            builder.addFormParam("trigger_price", String.valueOf(order.getTriggerPrice()));
        }
        return execute(builder, new TypeReference<KiteResponse<KiteOrderId>>(){});
    }
    
    @Override
    public KiteOrderId cancel(KiteOrder order) {
        final String url = URL_BASE + URL_ORDERS + "/" + order.getVariety().name() + "/" + order.getOrderId();
        return execute(httpClient.prepareDelete(url), new TypeReference<KiteResponse<KiteOrderId>>(){});
    }
    
    @Override
    public Collection<KiteQuote> getQuotes(Collection<KiteSymbol> instruments, KiteQuoteMode mode) {
        final StringBuilder urlBuilder = new StringBuilder();
        switch(mode) {
        case FULL: urlBuilder.append(KiteConstant.URL_QUOTE); break;
        case LTP: urlBuilder.append(KiteConstant.URL_QUOTE_LTP); break;
        case OHLC: urlBuilder.append(KiteConstant.URL_QUOTE_OHLC); break;
        }
        final String queryString = instruments.stream()
            .map(instrument -> "i=" + instrument.getExchange().name() + ":" + instrument.getTradingsymbol())
            .collect(Collectors.joining("&"));
        urlBuilder.append("?" + queryString);
        return execute(httpClient.prepareGet(urlBuilder.toString()), new TypeReference<KiteResponse<Map<String, KiteQuote>>>(){}).values();
    }
    
    @Override
    public void subscribe(Collection<KiteSymbol> symbols) {
        webSocketClient.subscribe(symbols);
    }

}
