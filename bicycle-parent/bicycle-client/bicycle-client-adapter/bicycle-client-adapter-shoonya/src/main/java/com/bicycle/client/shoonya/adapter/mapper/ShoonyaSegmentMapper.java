package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.symbol.Segment;

public class ShoonyaSegmentMapper {
    
    public String toShoonyaSegment(Segment segment) {
        if(null == segment) return null;
        switch(segment) {
            case CM: return "CM";
            case FO: return "FO";
            case FX: return "FX";
            case MCX: 
            default: throw new IllegalArgumentException(
                    String.format("Segment %s is not supported by shoonya", segment.name()));
        }
    }

}
