package com.example.storecomputer.Api;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.example.storecomputer.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VerificationUser extends Fragment{
    private static final String PREF_NAME = "AuthPrefs";
    private static final String AUTH_TOKEN_KEY = "auth_token";

    private OkHttpClient client;
    private ExecutorService executorService;

    public VerificationUser() {
        this.client = new OkHttpClient();
        this.executorService = Executors.newSingleThreadExecutor(); // Hilo en segundo plano
    }

    public void verifyUser(String token, VerificationCallback callback) {
        executorService.execute(() -> {
            boolean isSuccess = false;

            // Verificar si el token es nulo
            if (token == null || token.isEmpty()) {
                callback.onResult(false); // Retorna false si el token no es válido
                return;
            }

            Request request = new Request.Builder()
                    .url("http://192.168.100.80:8000/api/verification")
                    .addHeader("Authorization", token) // Asegurar formato correcto
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string().trim();
                    isSuccess = "1".equals(responseData);
                }
            } catch (IOException e) {
                System.out.println("Error del servidor");
                e.printStackTrace();
            }

            boolean finalIsSuccess = isSuccess;
            callback.onResult(finalIsSuccess);
        });
    }

    public static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null); // Devuelve null si no hay token guardado
    }
    private void updateContend(Fragment contentFragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, contentFragment)
                .addToBackStack(null) // Para que pueda volver atrás con el botón de retroceso
                .commit();
    }
    public static void clearAuthToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(AUTH_TOKEN_KEY); // Elimina solo el token
        editor.apply(); // Guarda cambios
    }

    public interface VerificationCallback {
        void onResult(boolean isSuccess);
    }
}
