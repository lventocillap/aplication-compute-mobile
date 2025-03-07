package com.example.storecomputer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EditText etEmail, etPassword;
    private TextView txtToken;
    private Button btnLogin;
    private OkHttpClient client;
    private static final String LOGIN_URL = "http://192.168.100.80:8000/api/login";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etEmail = view.findViewById(R.id.etUsuario);
        etPassword = view.findViewById(R.id.etContrasena);
        btnLogin = view.findViewById(R.id.btnIniciarSesion);

        client = new OkHttpClient();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor ingrese los datos", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        return view;
    }
    private void loginUser(String email, String password) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Error en la solicitud", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    getActivity().runOnUiThread(() ->
                            //Toast.makeText(getActivity(), "Error: " + response.code(), Toast.LENGTH_SHORT).show()
                            Toast.makeText(getActivity(), "Error: " + "Credenciales invalidas", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String responseData = response.body().string();
                Log.d("API_RESPONSE", responseData);

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    if (jsonObject.has("token")) {
                        String token = jsonObject.getString("token");

                        getActivity().runOnUiThread(() -> {
                            saveAuthToken(token); // Guarda el token en SharedPreferences
                            Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            Header headerUser = new Header();
                            updateFragments(headerUser.setTitle("Usuario"), new User());
                        });

                    } else {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                        );
                    }
                } catch (JSONException e) {
                    Log.e("JSON_ERROR", "Error al procesar JSON: " + e.getMessage());
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
    private void updateFragments(Fragment headerFragment, Fragment contentFragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, headerFragment)
                .replace(R.id.fragment_content, contentFragment)
                .addToBackStack(null) // Para que pueda volver atrás con el botón de retroceso
                .commit();
    }
    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }
}