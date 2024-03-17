package com.bicycle.client.shoonya.credentials;

import lombok.Builder;

@Builder
public record ShoonyaCredentials(
        String portoflioId,
        String username, 
        String password, 
        String pin,
        String imei,
        String version,
        String vendorCode,
        String applicationKey) {}
