package com.example.storecomputer.Api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenerateSaleEmailPDF{
    private static final String BASE_URL = "http://192.168.100.80:8000/api/sale";
    private final OkHttpClient client;

    public GenerateSaleEmailPDF() {
        this.client = new OkHttpClient();
    }

    public String postGenerateSale(Context context) throws IOException {
        String token = getAuthToken(context);
        String url = BASE_URL;
        RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body() != null ? response.body().string() : null;
        }
    }
    public static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null); // Devuelve null si no hay token guardado
    }
}
