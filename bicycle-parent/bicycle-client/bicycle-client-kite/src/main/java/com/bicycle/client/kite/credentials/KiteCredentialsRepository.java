package com.bicycle.client.kite.credentials;

public interface KiteCredentialsRepository {

    KiteCredentials findByPortfolioId(String portfolioId);

}