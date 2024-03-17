package com.bicycle.client.shoonya.credentials;

public interface ShoonyaCredentialRepository {

    ShoonyaCredentials findByPortfolioId(String portfolioId);

}