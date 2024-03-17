package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.core.order.Product;

public class ShoonyaProductMapper {
    
    public Product toProduct(String shoonyaProduct) {
        if(null == shoonyaProduct) return null;
        switch(shoonyaProduct.toUpperCase()) {
            case "C" : return Product.CNC;
            case "M" : return Product.NRML;
            case "I" : return Product.MIS;
            case "B" : return Product.BO;
            case "H" : return Product.CO;
            default : throw new IllegalArgumentException(String.format(
                    "ShoonyaProduct %s is not supported", shoonyaProduct));
        }
    }
    
    public String toShoonyaProduct(Product product) {
        if(null == product) return null;
        switch(product) {
            case BO: return "B";
            case CNC: return "C";
            case CO: return "H";
            case MIS: return "I";
            case NRML: return "M";
            default : throw new IllegalArgumentException(String.format(
                    "Product %s is not supported by shoonya", product.name()));
        }
    }

}
