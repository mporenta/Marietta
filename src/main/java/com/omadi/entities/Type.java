package com.omadi.entities;

import java.util.ArrayList;
import java.util.List;

public enum Type {
    PD("pd"),
    GEN("gen"),
    TOW("tow"),
    GEICO("geico"),
    COD("cod"),
    SHOP_TOW("shop_tow"),
    MISC_INVOICE("misc_invoice"),
    SERVICE("service");

    private String type;

    Type(String type) {
        this.type = type;
    }

    public static List<Type> getTypes() {
        List<Type> typeList = new ArrayList<>();

        typeList.add(PD);
        typeList.add(GEN);
        typeList.add(TOW);
        typeList.add(GEICO);
        typeList.add(COD);
        typeList.add(SHOP_TOW);
        typeList.add(MISC_INVOICE);
        typeList.add(SERVICE);

        return typeList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
