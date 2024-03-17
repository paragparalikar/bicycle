package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteProduct;
import com.bicycle.core.order.Product;

public class KiteProductMapper {

    public KiteProduct toKiteProduct(Product product) {
        if(null == product) return null;
        switch(product) {
        case CNC:return KiteProduct.CNC;
        case MIS:return KiteProduct.MIS;
        case NRML:return KiteProduct.NRML;
        default: throw new IllegalArgumentException(String.format("Product %s is not supported by kite", product.name()));
        }
    }
    
    public Product toProduct(KiteProduct kiteProduct) {
        if(null == kiteProduct) return null;
        switch(kiteProduct) {
        case CNC:return Product.CNC;
        case MIS:return Product.MIS;
        case NRML:return Product.NRML;
        default: throw new IllegalArgumentException(String.format("KiteProduct %s is not supported", kiteProduct.name()));
        }
    }
    
}
