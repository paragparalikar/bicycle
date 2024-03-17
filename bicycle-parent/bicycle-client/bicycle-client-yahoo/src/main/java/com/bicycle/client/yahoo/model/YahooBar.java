package com.bicycle.client.yahoo.model;

import lombok.Builder;

@Builder
public record YahooBar(long date, float open, float high, float low, float close, int volume) {

}
