package com.bicycle.client.shoonya.utils;

import java.io.File;

public interface Constant {

    String HOME = System.getProperty("com.bicycle.client.shoonya.home", 
            System.getProperty("user.home") + File.separator + ".bicycle");
    
}
