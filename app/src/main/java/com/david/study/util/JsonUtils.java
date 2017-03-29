package com.david.study.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Gson工具
 * Created by DavidChen on 2016/9/13.
 */
public class JsonUtils {
    public static String getMember(String json, String memberName) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        JsonElement jsonElement = jsonObject.get(memberName);
        return jsonElement == null ? null : jsonElement.getAsString();
    }

    public static JsonObject getJsonObject(String json, String memberName) throws JsonSyntaxException {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        JsonElement jsonElement = jsonObject.get(memberName);
        return jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
    }

    public static JsonArray getJsonArray(String json, String memberName) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        JsonElement jsonElement = jsonObject.get(memberName);
        return jsonElement.isJsonArray() ? jsonElement.getAsJsonArray() : null;
    }

    public static boolean hasMember(String json, String memberName) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        return jsonObject.has(memberName);
    }
}
