package com.bicycle.client.kite.credentials;

import lombok.Builder;

@Builder
public record KiteCredentials(
        String portoflioId,
        String username, 
        String password, 
        String pin) {
	
}
