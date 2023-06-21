package com.example.client.controllers.content;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class ContentFactory {

    public static Content createShowable(JSONObject jsonObject){
        LogManager.getLogger().fatal(jsonObject.toString());
        switch (jsonObject.getString("type")){
            case "userName":
                return new UserName(jsonObject.getString("content"));
                ///more to add
            default:
                return null;
        }
    }
}
