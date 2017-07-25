package com.omadi.helpers;

import com.omadi.entities.Type;

public class Url {
    private static String MAIN_URL = "https://api.omadi.com/v1/";

    public static String getReportUrl(Type type) {
        return String.format(MAIN_URL + "node.json?type=%s", type.getType());
    }

    public static String getReportUrl(String query) {
        return MAIN_URL + "node.json?" + query;
    }

    public static String getNodeUrl(int id) {
        return String.format(MAIN_URL + "node/%s.json", id);
    }

    public static String getObjectUrl(String type, int id) {
        return String.format(MAIN_URL + "node/%s.json?type=%s", id, type);
    }
}
