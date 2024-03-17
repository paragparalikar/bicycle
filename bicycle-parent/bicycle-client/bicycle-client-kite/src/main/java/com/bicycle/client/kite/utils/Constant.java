package com.bicycle.client.kite.utils;

import java.io.File;

public interface Constant {

    String HOME = System.getProperty("com.bicycle.client.kite.home", 
            System.getProperty("user.home") + File.separator + ".bicycle");
    
}
