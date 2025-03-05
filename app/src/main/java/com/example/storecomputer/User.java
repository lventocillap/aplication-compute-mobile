package com.example.storecomputer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storecomputer.Api.VerificationUser;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link User#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User extends Fragment {

    private TextView textViewName, textViewEmail;
    private Button btnClose;
    private static final String API_URL = "http://192.168.100.80:8000/api/user";
    private static final String LOGOUT_URL = "http://192.168.100.80:8000/api/logout";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public User() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User.
     */
    // TODO: Rename and change types and number of parameters
    public static User newInstance(String param1, String param2) {
        User fragment = new User();
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
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        textViewName = view.findViewById(R.id.txtUsername);
        textViewEmail = view.findViewById(R.id.txtEmial);
        btnClose = view.findViewById(R.id.btnCloseSesion);

        btnClose.setOnClickListener(v ->{
            logout();
            Header headerLogin = new Header();
            updateFragments(headerLogin.setTitle("Login"), new Login());
        });

        fetchUserData();

        return view;
    }
    private void updateFragments(Fragment headerFragment, Fragment contentFragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, headerFragment)
                .replace(R.id.fragment_content, contentFragment)
                .addToBackStack(null) // Para que pueda volver atrás con el botón de retroceso
                .commit();
    }

    private void fetchUserData() {
        OkHttpClient client = new OkHttpClient();
        Context context = getContext();
        String token = VerificationUser.getAuthToken(context);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Error al obtener datos", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONObject userData = jsonObject.getJSONObject("data");
                        String name = userData.getString("name");
                        String email = userData.getString("email");

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                textViewName.setText(name);
                                textViewEmail.setText("Email: "+email);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void logout() {
        OkHttpClient clientLogout = new OkHttpClient();
        Context contextLogout = getContext();
        String tokenLogout = VerificationUser.getAuthToken(contextLogout);

        // Construcción de la solicitud POST con un cuerpo vacío
        Request requestLogout = new Request.Builder()
                .url(LOGOUT_URL)
                .post(RequestBody.create(null, new byte[0])) // Cuerpo vacío
                .addHeader("Authorization",tokenLogout) // Se agrega "Bearer"
                .addHeader("Content-Type", "application/json")
                .build();

        clientLogout.newCall(requestLogout).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            }
        });
    }


}