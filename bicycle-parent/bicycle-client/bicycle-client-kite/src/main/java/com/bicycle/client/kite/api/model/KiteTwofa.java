package com.bicycle.client.kite.api.model;

import lombok.Data;

@Data
public class KiteTwofa {

	private String userId;
	private String requestId;
	private String twofaType;
	private String[] twofaTypes;
	private String twofaStatus;
	private boolean captcha;
	private boolean locked;
	private KiteProfile profile;
	
}
