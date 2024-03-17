package com.bicycle.client.shoonya.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Value;

@Value
public class ShoonyaBar {

    @JsonProperty("stat") private ShoonyaStatusResponse status;
    @JsonProperty("time") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD/MM/YYYY hh:mm:ss") private Date time;
    @JsonProperty("into") private float open;
    @JsonProperty("inth") private float high;
    @JsonProperty("intl") private float low;
    @JsonProperty("intc") private float close;
    @JsonProperty("intv") private int volume;
    
    
}
