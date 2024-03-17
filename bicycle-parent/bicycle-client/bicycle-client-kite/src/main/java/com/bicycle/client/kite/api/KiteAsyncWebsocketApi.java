package com.bicycle.client.kite.api;

import com.bicycle.client.kite.KiteConstant;
import com.bicycle.client.kite.api.model.KiteOrder;
import com.bicycle.client.kite.api.model.KiteQuoteMode;
import com.bicycle.client.kite.api.model.KiteSymbol;
import com.bicycle.client.kite.api.model.KiteTick;
import com.bicycle.client.kite.credentials.KiteCredentials;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

@Slf4j
@RequiredArgsConstructor
public class KiteAsyncWebsocketApi implements WebSocketListener, AutoCloseable {
    private static final long PING_INTERVAL = 2500;
    private static final long PONG_INTERVAL = 2500;
    private static final long RECONNECT_CHECK_DELAY = 500;
    private static final long RECONNECT_CHECK_INTERVAL = 500;
    private static final String MODE_FULL = "full", MODE_QUOTE = "quote", MODE_LTP = "ltp"; 
    private static final String MESSAGE_SUBSCRIBE = "subscribe", MESSAGE_SET_MODE = "mode";

    private volatile WebSocket webSocket;
    private final AsyncHttpClient httpClient;
    private final KiteCredentials credentials;
    private final KiteDataPublisher dataPublisher;
    private final Supplier<String> enctokenSupplier;
    private final Collection<KiteSymbol> symbols = new HashSet<>();
    private final ScheduledExecutorService scheduledExecutorService;
    private volatile ScheduledFuture<?> reOpenFuture, pingFuture, pongFuture;
    private final KiteBinaryMessageParser kiteBinaryMessageParser = new KiteBinaryMessageParser();
    
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
        final String username = credentials.username();
        final String enctoken = URLEncoder.encode(enctokenSupplier.get(), "UTF-8");
        webSocket = httpClient.prepareGet(KiteConstant.URL_WS + "/")
            .addQueryParam("api_key", KiteConstant.APIKEY)
            .addQueryParam("user_id", username)
            .addQueryParam("enctoken", enctoken)
            .addQueryParam("uid", String.valueOf(new Date().getTime()))
            .addQueryParam("user-agent", KiteConstant.USER_AGENT_KITE)
            .addQueryParam("version", KiteConstant.WS_VERSION)
            .execute(new WebSocketUpgradeHandler(Collections.singletonList(this)))
            .get();
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        this.webSocket = webSocket;
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
    
    public void subscribe(Collection<KiteSymbol> symbols) {
        this.symbols.addAll(symbols);
        if(null != webSocket && webSocket.isOpen()) {
            final List<Integer> tokens = symbols.stream().map(KiteSymbol::getInstrumentToken).toList();
            webSocket.sendTextFrame(createTickerMessagge(tokens, MESSAGE_SUBSCRIBE));
            webSocket.sendTextFrame(createModeMessage(tokens, KiteQuoteMode.OHLC));
        }
    }
    
    @Override
    public void onBinaryFrame(byte[] payload, boolean finalFragment, int rsv) {
        kiteBinaryMessageParser.parseBinary(payload).forEach(dataPublisher::publish);
    }
    
    @Override
    @SneakyThrows
    public void onTextFrame(String payload, boolean finalFragment, int rsv) {
        final JsonNode data = KiteConstant.JSON.readTree(payload);
        if(!data.has("type")) return;
        final String type = data.get("type").asText();
        if(type.equals("order")) {
            final KiteOrder order = KiteConstant.JSON.readValue(data.get("data").toString(), KiteOrder.class);
            dataPublisher.publish(order);
        } else if(type.equals("error")) {
            log.error(data.get("data").toPrettyString());
        } else if(type.equals("instruments_meta")) { 
            // noop
        } else {
            log.error("Unknown \"type\" recevied from kite : {}, data : {}", type, data.get("data").toPrettyString());
        }
    }

    @Override
    public void onClose(WebSocket websocket, int code, String reason) {
        log.warn("Kite websocket closed with code {} and reason : {}", code, reason);
        try {
            open();
        } catch (Exception e) {
            log.error("Could not open kite websocket for " + credentials.username(), e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error in kite websocket client for " + credentials.username(), throwable);
    }

    
    @SneakyThrows
    private String createTickerMessagge(final Collection<Integer> tokens, final String action) {
        final ObjectNode objectNode = KiteConstant.JSON.createObjectNode();
        final ArrayNode list = objectNode.arrayNode();
        tokens.forEach(list::add);
        objectNode.set("v", list);
        objectNode.put("a", action);
        return objectNode.toString();
    }
    
    @SneakyThrows
    private String createModeMessage(final Collection<Integer> tokens, final KiteQuoteMode mode) {
        final ObjectNode objectNode = KiteConstant.JSON.createObjectNode();
        final ArrayNode list = objectNode.arrayNode();
        final ArrayNode listMain = objectNode.arrayNode();
        listMain.add(resolveKiteQuoteMode(mode));
        tokens.forEach(list::add);
        listMain.add(list);
        objectNode.put("a", MESSAGE_SET_MODE);
        objectNode.set("v", listMain);
        return objectNode.toString();
    }
    
    private String resolveKiteQuoteMode(final KiteQuoteMode mode) {
        switch(mode) {
        case FULL: return MODE_FULL;
        case OHLC: return MODE_QUOTE;
        case LTP: return MODE_LTP;
        default: return MODE_FULL;
        }
    }
    
}


class KiteBinaryMessageParser {
    @SuppressWarnings("unused")
    private static final int SEGMENT_NSE_CD = 3, SEGMENT_BSE_CD = 6, SEGMENT_INDICES = 9;
    Collection<KiteTick> parseBinary(final byte [] binaryPackets) {
        ArrayList<KiteTick> ticks = new ArrayList<KiteTick>();
        ArrayList<byte[]> packets = splitPackets(binaryPackets);
        for (int i = 0; i < packets.size(); i++) {
            byte[] bin = packets.get(i);
            byte[] t = Arrays.copyOfRange(bin, 0, 4);
            int x = ByteBuffer.wrap(t).getInt();

            //int token = x >> 8;
            int segment = x & 0xff;

            int dec1 = (segment == SEGMENT_NSE_CD) ? 10000000 : (segment == SEGMENT_BSE_CD)? 10000 : 100;

            if(bin.length == 8) {
                KiteTick tick = getLtpQuote(bin, x, dec1);
                ticks.add(tick);
            }else if(bin.length == 28 || bin.length == 32) {
                KiteTick tick = getIndeciesData(bin, x);
                ticks.add(tick);
            }else if(bin.length == 44) {
                KiteTick tick = getQuoteData(bin, x, dec1);
                ticks.add(tick);
            } else if(bin.length == 184) {
                KiteTick tick = getQuoteData(bin, x, dec1);
               // tick.setMode(MODE_FULL);
                ticks.add(getFullData(bin, tick));
            }
        }
        return ticks;
    }
    
    private KiteTick getIndeciesData(byte[] bin, int x){
        int dec = 100;
        KiteTick tick = new KiteTick();
        tick.setToken(x);
        float lastTradedPrice = convertToFloat(getBytes(bin, 4, 8)) / dec;
        tick.setLastTradedPrice(lastTradedPrice);
        
       /* tick.setMode(MODE_QUOTE);
        tick.setTradable(tradable);
        tick.setHighPrice(convertToFloat(getBytes(bin, 8, 12)) / dec);
        tick.setLowPrice(convertToFloat(getBytes(bin, 12, 16)) / dec);
        tick.setOpenPrice(convertToFloat(getBytes(bin, 16, 20)) / dec);
        float closePrice = convertToFloat(getBytes(bin, 20, 24)) / dec;
        tick.setClosePrice(closePrice);
        // here exchange is sending absolute value, hence we change that to %change
        //tick.setNetPriceChangeFromClosingPrice(convertToDouble(getBytes(bin, 24, 28)) / dec);
        setChangeForTick(tick, lastTradedPrice, closePrice);
        if(bin.length > 28) {
            tick.setMode(MODE_FULL);
            final long tickTimestamp = convertToInt(getBytes(bin, 28, 32)) * 1000;
            tick.setTickTimestamp(isValidDate(tickTimestamp) ? tickTimestamp : System.currentTimeMillis());
        } */
        return tick;
    }

    private KiteTick getLtpQuote(final byte[] bin, final int x, final int dec1){
        final KiteTick tick = new KiteTick();
       // tick.setMode(MODE_LTP);
       // tick.setTradable(tradable);
        tick.setToken(x);
        tick.setLastTradedPrice(convertToFloat(getBytes(bin, 4, 8)) / dec1);
        tick.setLastTradedTime(System.currentTimeMillis());
        return tick;
    }
    
    private KiteTick getQuoteData(final byte[] bin, final int x, final int dec1){
        final KiteTick tick = new KiteTick();
        tick.setToken(x);
        final float lastTradedPrice = convertToFloat(getBytes(bin, 4, 8)) / dec1;
        tick.setLastTradedPrice(lastTradedPrice);
        tick.setVolumeTradedToday(convertToInt(getBytes(bin, 16, 20)));
        tick.setLastTradedTime(System.currentTimeMillis());
        
       /* tick.setMode(MODE_QUOTE);
        tick.setTradable(tradable);
        tick.setLastTradedQuantity(convertToInt(getBytes(bin, 8, 12)));
        tick.setAverageTradePrice(convertToFloat(getBytes(bin, 12, 16)) / dec1);
        tick.setTotalBuyQuantity(convertToInt(getBytes(bin, 20, 24)));
        tick.setTotalSellQuantity(convertToInt(getBytes(bin, 24, 28)));
        tick.setOpenPrice(convertToFloat(getBytes(bin, 28, 32)) / dec1);
        tick.setHighPrice(convertToFloat(getBytes(bin, 32, 36)) / dec1);
        tick.setLowPrice(convertToFloat(getBytes(bin, 36, 40)) / dec1);
        final float closePrice = convertToFloat(getBytes(bin, 40, 44)) / dec1;
        tick.setClosePrice(closePrice);
        setChangeForTick(tick, lastTradedPrice, closePrice); */
        
        return tick;
    }

    private KiteTick getFullData(final byte[] bin, final KiteTick tick){
        tick.setLastTradedTime(convertToInt(getBytes(bin, 44, 48)) * 1000);
       /* tick.setOi(convertToInt(getBytes(bin, 48, 52)));
        tick.setOiDayHigh(convertToFloat(getBytes(bin, 52, 56)));
        tick.setOiDayLow(convertToFloat(getBytes(bin, 56, 60)));
        final long tickTimestamp = convertToInt(getBytes(bin, 60, 64)) * 1000;
        tick.setTickTimestamp(isValidDate(tickTimestamp) ? tickTimestamp : System.currentTimeMillis());
        tick.setDepth(getDepthData(bin, dec, 64, 184)); */
        return  tick;
    }

    private ArrayList<byte []> splitPackets(final byte[] bin){
        final ArrayList<byte []> packets = new ArrayList<byte []>();
        final int noOfPackets = getLengthFromByteArray(getBytes(bin, 0, 2)); //in.read(bin, 0, 2);
        int j = 2;
        for(int i = 0; i < noOfPackets; i++){
            int sizeOfPacket = getLengthFromByteArray(getBytes(bin, j, j + 2));//in.read(bin, j, j+2);
            byte[] packet = Arrays.copyOfRange(bin, j + 2, j + 2 + sizeOfPacket);
            packets.add(packet);
            j = j + 2 + sizeOfPacket;
        }
        return packets;
    }

    private byte[] getBytes(final byte[] bin, final int start, final int end){
        return Arrays.copyOfRange(bin, start, end);
    }

    @SuppressWarnings("unused") 
    private double convertToDouble(final byte[] bin){
        final ByteBuffer bb = ByteBuffer.wrap(bin);
        bb.order(ByteOrder.BIG_ENDIAN);
        if(bin.length < 4)
            return bb.getShort();
        else if(bin.length < 8)
            return bb.getInt();
        else
            return bb.getDouble();
    }
    
    private float convertToFloat(final byte[] bin) {
        final ByteBuffer bb = ByteBuffer.wrap(bin);
        bb.order(ByteOrder.BIG_ENDIAN);
        if(bin.length < 4)
            return bb.getShort();
        else if(bin.length < 8)
            return bb.getFloat();
        else
            return (float) bb.getDouble();
    }

    private int convertToInt(final byte[] bin){
        final ByteBuffer bb = ByteBuffer.wrap(bin);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getInt();
    }

    private int getLengthFromByteArray(final byte[] bin){
        final ByteBuffer bb = ByteBuffer.wrap(bin);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getShort();
    }
    
}

