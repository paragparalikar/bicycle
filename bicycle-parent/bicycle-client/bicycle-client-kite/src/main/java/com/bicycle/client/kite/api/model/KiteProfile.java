package com.bicycle.client.kite.api.model;

import java.util.List;

import lombok.Data;

@Data
public class KiteProfile {
	
	private String userId;
	
	private String twofaType;
	
	private String userName;
	
	private String userType;
	
	private String email;
	
	private String phone;
	
	private String broker;
	
	private KiteBankAccount bankAccount;
	
	private List<String> dpIds;
	
	private List<KiteProduct> products;
	
	private List<KiteLimitType> orderTypes;
	
	private List<KiteExchange> exchanges;
	
	private String pan;
	
	private String userShortname;
	
	private String avatarUrl;
	
	private List<String> tags;
	
}
