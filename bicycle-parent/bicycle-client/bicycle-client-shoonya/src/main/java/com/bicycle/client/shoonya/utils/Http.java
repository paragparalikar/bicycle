package com.bicycle.client.shoonya.utils;

import java.time.Duration;
import java.util.Optional;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Realm;

public interface Http {

	String COOKIE = "Cookie";
	String SET_COOKIE = "Set-Cookie";
	String AUTHORIZATION = "Authorization";
	String CONTENT_ENCODING = "Content-Encoding";
	String GZIP = "gzip";
	String BR = "br";
	
	int BAD_REQUEST = 400;
	int FORBIDDEN = 403;
	
	public static DefaultAsyncHttpClientConfig.Builder createAsyncHttpClientConfig() {
	    final DefaultAsyncHttpClientConfig.Builder builder = Dsl.config()
	            .setKeepAlive(true)
	            .setSoKeepAlive(true)
	            .setFollowRedirect(true)
	            .setUseProxyProperties(true)
	            .setMaxRequestRetry(10)
	            .setHandshakeTimeout(300000)
	            .setConnectionTtl(Duration.ofDays(1))
	            .setReadTimeout(Duration.ofMinutes(5))
	            .setRequestTimeout(Duration.ofMinutes(5))
	            .setConnectTimeout(Duration.ofMinutes(5))
	            .setPooledConnectionIdleTimeout(Duration.ofDays(1))
	            .setThreadPoolName("async-http-client")
	            .setUseInsecureTrustManager(true);
	    Optional.ofNullable(System.getProperty("https.proxyHost")).ifPresent(host -> {
            final int port = Integer.getInteger("https.proxyPort");
            final String username = System.getProperty("https.proxyUser");
            final String password = System.getProperty("https.proxyPassword");
            final Realm.Builder realmBuilder = Dsl.ntlmAuthRealm(username, password);
            builder.setProxyServer(Dsl.proxyServer(host, port).setRealm(realmBuilder));
	    });
	    return builder;
	}

}
