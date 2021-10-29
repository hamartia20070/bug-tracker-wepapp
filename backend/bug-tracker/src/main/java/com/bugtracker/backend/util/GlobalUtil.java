package com.bugtracker.backend.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class GlobalUtil {
    public static int itemsPerPage =10;//sets how many items per page should be returned on get requests

    public static Map<String,String> writeErrorAsJson(String errorMessage){
        Map<String,String> error = new HashMap<>();
        error.put("error_message",errorMessage);
        return error;
    }
    //just a map I'm returning for a response body to pass client the tokens
    public static Map<String,String> writeTokenAsJson(String accessToken,String refreshToken, Date accessTokenExpires, Date refreshTokenExpires){
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("access_token_expires",df.format(accessTokenExpires));
        tokens.put("refresh_token", refreshToken);
        tokens.put("refresh_token_expires",df.format(refreshTokenExpires));
        return tokens;
    }
}
