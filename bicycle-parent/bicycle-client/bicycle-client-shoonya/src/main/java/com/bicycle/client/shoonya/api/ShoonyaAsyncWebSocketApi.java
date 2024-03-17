package com.bicycle.client.shoonya.api;

import com.bicycle.client.shoonya.api.model.ShoonyaOrder;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import com.bicycle.client.shoonya.api.model.ShoonyaTick;
import com.bicycle.client.shoonya.credentials.ShoonyaCredentials;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

@Slf4j
public class ShoonyaAsyncWebSocketApi implements WebSocketListener, AutoCloseable {
    private static final long PING_INTERVAL = 2500;
    private static final long PONG_INTERVAL = 2500;
    private static final long RECONNECT_CHECK_DELAY = 500;
    private static final long RECONNECT_CHECK_INTERVAL = 5000;
    private static final String URL = "wss://api.shoonya.com/NorenWSTP/";
    
    private volatile WebSocket webSocket;
    private final ObjectMapper objectMapper;
    private final AsyncHttpClient httpClient;
    private final ShoonyaCredentials credentials;
    private final ShoonyaDataPublisher dataPublisher;
    private final Supplier<ShoonyaTokens> tokensSupplier;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Collection<ShoonyaSymbol> symbols = new HashSet<>();
    private volatile ScheduledFuture<?> reOpenFuture, pingFuture, pongFuture;

    @Builder
    public ShoonyaAsyncWebSocketApi(ObjectMapper objectMapper, AsyncHttpClient httpClient, 
            ShoonyaCredentials credentials, Supplier<ShoonyaTokens> tokensSupplier, 
            ShoonyaDataPublisher dataPublisher, ScheduledExecutorService scheduledExecutorService) {
        super();
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.credentials = credentials;
        this.dataPublisher = dataPublisher;
        this.tokensSupplier = tokensSupplier;
        this.scheduledExecutorService = scheduledExecutorService;
    }
    
    public synchronized void start() {
        if(null == webSocket || !webSocket.isOpen()) {
            try {
                open();
            } catch (Exception e) {
                log.error("Could not open kite websocket for " + credentials.username(), e);
            }
        }
    }
    
    @Override
    public void close() throws Exception {
        cancel(reOpenFuture);
        cancel(pingFuture);
        cancel(pongFuture);
        if(null != webSocket) {
            if(webSocket.isOpen()) {
                webSocket.sendCloseFrame();
            }
            webSocket = null;
        }
    }
    
    private void cancel(ScheduledFuture<?> future) {
        if(null != future && !future.isDone() && !future.isCancelled()) {
            future.cancel(false);
        }
    }
    
    private void open() throws Exception {
        webSocket = httpClient.preparePost(URL)
            .execute(new WebSocketUpgradeHandler(Collections.singletonList(this)))
            .get();
    }

    @Override
    @SneakyThrows
    public void onOpen(WebSocket webSocket) {
        this.webSocket = webSocket;
        final ShoonyaTokens tokens = tokensSupplier.get();
        final ShoonyaWSConnectRequest connectRequest = new ShoonyaWSConnectRequest(tokens);
        webSocket.sendTextFrame(objectMapper.writeValueAsString(connectRequest));
        if(null == reOpenFuture || reOpenFuture.isDone() || reOpenFuture.isCancelled()) {
            reOpenFuture = scheduledExecutorService.scheduleWithFixedDelay(this::start, 
                    RECONNECT_CHECK_DELAY, RECONNECT_CHECK_INTERVAL, TimeUnit.MILLISECONDS);
        }
        if(null == pingFuture || pingFuture.isDone() || pingFuture.isCancelled()) {
            pingFuture = scheduledExecutorService.scheduleAtFixedRate(webSocket::sendPingFrame, 
                    PING_INTERVAL, PING_INTERVAL, TimeUnit.MILLISECONDS);
        }
        if(null == pongFuture || pongFuture.isDone() || pongFuture.isCancelled()) {
            pongFuture = scheduledExecutorService.scheduleAtFixedRate(webSocket::sendPongFrame, 
                    PONG_INTERVAL, PONG_INTERVAL, TimeUnit.MILLISECONDS);
        }
        subscribe(symbols);
    }
    
    @SneakyThrows
    public void subscribe(Collection<ShoonyaSymbol> symbols) {
        this.symbols.addAll(symbols);
        if(null != webSocket && webSocket.isOpen()) {
            final String scriplist = symbols.stream()
                    .map(symbol -> String.join("|", symbol.getExchange().name(), String.valueOf(symbol.getToken())))
                    .collect(Collectors.joining("#"));
            final ShoonyaTouchlineRequest request = new ShoonyaTouchlineRequest("t", scriplist);
            webSocket.sendTextFrame(objectMapper.writeValueAsString(request));
        }
    }

    @Override
    public void onClose(WebSocket websocket, int code, String reason) {
        log.warn("Shoonya websocket closed with code {} and reason : {}", code, reason);
        try {
            open();
        } catch (Exception e) {
            log.error("Could not open Shoonya websocket for " + credentials.username(), e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error in Shoonya websocket client for " + credentials.username(), throwable);
    }
    
    @Override
    public void onTextFrame(final String payload, boolean finalFragment, int rsv) {
        ForkJoinPool.commonPool().execute(() -> {
            try {
                final JsonNode node = objectMapper.readTree(payload);
                switch(node.get("t").textValue()) {
                    case "ck" : log.info("Shoonya websocket connection acknowledged for {} with message {}", 
                            node.get("uid").textValue(), node.get("s").textValue()); 
                    onConnectAck();
                    break;
                    case "tk" : log.info("Shoonya touchline subscription acknowledged for {} : {}",
                            node.get("e").textValue(), node.get("ts").textValue()); break;
                    case "tf" : dataPublisher.publish(objectMapper.readValue(payload, ShoonyaTick.class)); break;
                    case "om" :
                    case "uk" : log.info("Shoonya touchline unsubscribe acknowledged for {}",
                            node.get("k").textValue()); break;
                    case "dk" : log.info("Shoonya depth subscription acknowledged for {}",
                            node.get("e").textValue(), node.get("tk").textValue()); break;
                    case "df" : log.warn("Discarding Shoonya depth subscription feed received for {}",
                            node.get("e").textValue(), node.get("tk").textValue()); break;
                    case "udk" : log.info("Shoonya depth unsubscribe acknowledged for {}",
                            node.get("k").textValue()); break;
                    case "ok" : dataPublisher.publish(objectMapper.readValue(payload, ShoonyaOrder.class)); break;
                    case "o" : log.info("Shoonya order subscription acknowledged for {}",
                            node.get("actid").textValue()); break;
                    case "ud" : 
                    case "uok" : log.info("Shoonya unsubscribe order update acknowledged"); break;
                }
            } catch(Exception e) {
                log.error("Error in Shoonya websocket api for " + credentials.username(), e);
            }
        });
    }
    
    @SneakyThrows
    private void onConnectAck() {
        final ShoonyaTokens tokens = tokensSupplier.get();
        final ShoonyaOrderUpdateRequest request = new ShoonyaOrderUpdateRequest("o", tokens.actid());
        webSocket.sendTextFrame(objectMapper.writeValueAsString(request));
    }
    
}

@Value
class ShoonyaWSConnectRequest {
    
    private final String t, uid, actid, source, susertoken;
    
    public ShoonyaWSConnectRequest(ShoonyaTokens tokens) {
        t = "c";
        uid = tokens.uid();
        actid = tokens.actid();
        source = "API";
        susertoken = tokens.jkey();
    }
}

@Value
class ShoonyaTouchlineRequest {
    
    private final String t, k;
    
}

@Value
class ShoonyaOrderUpdateRequest {
    
    private final String t, actid;
    
}
