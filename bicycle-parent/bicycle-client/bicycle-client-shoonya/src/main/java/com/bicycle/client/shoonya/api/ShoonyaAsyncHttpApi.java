package com.bicycle.client.shoonya.api;

import com.bicycle.client.shoonya.api.model.ShoonyaBar;
import com.bicycle.client.shoonya.api.model.ShoonyaBarRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaBarResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaCancelOrderRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaCancelOrderResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaCreateOrderRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaCreateOrderResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaGetHoldingsRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaGetHoldingsResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaGetMarginRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaGetMarginResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaGetOrdersRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaGetOrdersResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaGetPositionsRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaGetPositionsResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaHolding;
import com.bicycle.client.shoonya.api.model.ShoonyaLoginRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaLoginResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaOrder;
import com.bicycle.client.shoonya.api.model.ShoonyaPosition;
import com.bicycle.client.shoonya.api.model.ShoonyaResponse;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import com.bicycle.client.shoonya.api.model.ShoonyaUpdateOrderRequest;
import com.bicycle.client.shoonya.api.model.ShoonyaUpdateOrderResponse;
import com.bicycle.client.shoonya.credentials.ShoonyaCredentials;
import com.bicycle.client.shoonya.utils.Http;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

@Slf4j
public class ShoonyaAsyncHttpApi implements ShoonyaApi {
    private static final String BASE_URL = "https://api.shoonya.com/NorenWClientTP/";

    private volatile ShoonyaTokens tokens;
    private final ShoonyaCredentials credentials;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final AsyncHttpClient httpClient = Dsl.asyncHttpClient(Http.createAsyncHttpClientConfig());
    
    @Delegate private final ShoonyaAsyncWebSocketApi shoonyaAsyncWebSocketApi;

    public ShoonyaAsyncHttpApi(
            ShoonyaCredentials credentials,
            ShoonyaDataPublisher shoonyaDataPublisher,
            ScheduledExecutorService scheduledExecutorService) {
        this.credentials = credentials;
        shoonyaAsyncWebSocketApi = ShoonyaAsyncWebSocketApi.builder()
                .httpClient(httpClient)
                .credentials(credentials)
                .objectMapper(objectMapper)
                .dataPublisher(shoonyaDataPublisher)
                .scheduledExecutorService(scheduledExecutorService)
                .tokensSupplier(() -> Optional.ofNullable(tokens).orElseGet(this::login))
                .build();
    }
 
    @Override
    public void init() {
        shoonyaAsyncWebSocketApi.start();
    }
    
    @Override
    public void close() throws Exception {
        if(null != shoonyaAsyncWebSocketApi) shoonyaAsyncWebSocketApi.close();
        if(null != httpClient) httpClient.close();
    }
    
    @SneakyThrows
    private <T extends ShoonyaResponse> T execute(String url, Object request, Class<T> responseType) {
        if(null == tokens) login();
        final Response response = httpClient.preparePost(BASE_URL + url)
                .addFormParam("jKey", tokens.jkey())
                .addFormParam("jData", objectMapper.writeValueAsString(request))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .execute().get();
        final int statusCode = response.getStatusCode();
        if(HttpStatus.SC_FORBIDDEN == statusCode || HttpStatus.SC_UNAUTHORIZED == statusCode) {
            tokens = null;
            log.warn("Relogging in to Shoonya for {} user, got {}", credentials.username(), 
                    statusCode + " : " + response.getStatusText());
            return execute(url, request, responseType);
        }
        if(300 <= statusCode) 
            throw new ShoonyaHttpClientException(statusCode, response.getStatusText());
        final T shoonyaResponse = objectMapper.readValue(response.getResponseBody(), responseType);
        return shoonyaResponse;
    }
    
    @SneakyThrows
    public ShoonyaTokens login() {
        final ShoonyaLoginRequest loginRequest = new ShoonyaLoginRequest(credentials);
        final Response response = httpClient.preparePost(BASE_URL + "QuickAuth")
                .addFormParam("jData", objectMapper.writeValueAsString(loginRequest))
                .execute().get();
        if(300 <= response.getStatusCode()) 
            throw new ShoonyaHttpClientException(response.getStatusCode(), response.getStatusText());
        final ShoonyaLoginResponse loginResponse = objectMapper.readValue(response.getResponseBody(), ShoonyaLoginResponse.class);
        return this.tokens = loginResponse.getTokens();
    }
    
    @Override 
    public List<ShoonyaBar> getBars(ShoonyaSymbol symbol, int interval, Date startDate, Date endDate){
        final long start = startDate.getTime() / 1000;
        final long end = endDate.getTime() / 1000;
        final ShoonyaBarRequest barRequest = ShoonyaBarRequest.builder()
                .endTime(end)
                .exchange(symbol.getExchange())
                .startTime(start)
                .token(symbol.getToken())
                .userId(tokens.uid())
                .intervalInMinutes(interval)
                .build();
        return execute("TPSeries", barRequest, ShoonyaBarResponse.class);
    }
    
    @Override
    public String create(ShoonyaOrder order) {
        order.setUserId(tokens.uid());
        order.setAccountId(tokens.actid());
        final ShoonyaCreateOrderResponse response = execute("PlaceOrder", 
                new ShoonyaCreateOrderRequest(order, tokens.uid(), tokens.actid()), 
                ShoonyaCreateOrderResponse.class);
        return response.getOrderId();
    }
    
    @Override
    public void update(ShoonyaOrder order) {
        execute("ModifyOrder", new ShoonyaUpdateOrderRequest(order, tokens.uid()), 
                ShoonyaUpdateOrderResponse.class);
    }
    
    @Override
    public void cancel(String orderId) {
        final ShoonyaCancelOrderRequest cancelOrderRequest = ShoonyaCancelOrderRequest.builder()
                .userId(tokens.uid()).orderId(orderId).build();
        execute("CancelOrder", cancelOrderRequest, ShoonyaCancelOrderResponse.class);
    }
    
    @Override
    public List<ShoonyaOrder> getOrders() {
        final ShoonyaGetOrdersRequest getOrderRequest = ShoonyaGetOrdersRequest.builder()
                .userId(tokens.uid())
                .build();
        return execute("OrderBook", getOrderRequest, ShoonyaGetOrdersResponse.class);
    }
    
    @Override
    public List<ShoonyaPosition> getPositions() {
        final ShoonyaGetPositionsRequest getPositionsRequest = ShoonyaGetPositionsRequest.builder()
                .userId(tokens.uid()).accountId(tokens.actid()).build();
        return execute("PositionBook", getPositionsRequest, ShoonyaGetPositionsResponse.class);
    }
    
    @Override
    public List<ShoonyaHolding> getHoldings(String product) {
        final ShoonyaGetHoldingsRequest getHoldingRequest = ShoonyaGetHoldingsRequest.builder()
                .product(product).userId(tokens.uid()).accountId(tokens.actid()).build();
        return execute("Holdings", getHoldingRequest, ShoonyaGetHoldingsResponse.class);
    }
    
    @Override
    public float getMargin(String segment) {
        final ShoonyaGetMarginRequest request = ShoonyaGetMarginRequest.builder()
                .userId(tokens.uid())
                .accountId(tokens.actid())
                .segment(segment)
                .build();
        final ShoonyaGetMarginResponse response = execute("Limits", request, ShoonyaGetMarginResponse.class);
        return response.getCash();
    }

}
