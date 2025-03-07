package com.example.storecomputer.EnumAPI;

public enum APIEnum {
    DOMAIN("http://192.168.100.80:8000");
    private String url;

    // Constructor del enum, que recibe el valor de la URL
    APIEnum(String url) {
        this.url = url;
    }

    // Método getter para obtener el valor de la URL
    public String getUrl() {
        return url;
    }
}
