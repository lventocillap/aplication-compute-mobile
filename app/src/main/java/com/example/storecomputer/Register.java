package com.example.storecomputer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.storecomputer.EnumAPI.APIEnum;

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
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    private EditText textUser, textPassword, textPasswordVerified, textCellphone, textDirecction, texDNI, textEmail;
    private Button btnRegister;
    private static final String URL = APIEnum.DOMAIN.getUrl()+"/api/register/user";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        textUser = view.findViewById(R.id.txtUsuario);
        textPassword =view.findViewById(R.id.txtContraseña);
        textPasswordVerified = view.findViewById(R.id.txtVerificarC);
        textCellphone = view.findViewById(R.id.txtTelefono);
        textDirecction  = view.findViewById(R.id.txtDireccion);
        texDNI = view.findViewById(R.id.txtDNI);
        textEmail = view.findViewById(R.id.txtCorreo);
        btnRegister = view.findViewById(R.id.btnRegistrarse);


        btnRegister.setOnClickListener(v -> {
            String name = textUser.getText().toString();
            String email = textEmail.getText().toString();
            String passwordVerified = textPasswordVerified.getText().toString();
            String password = textPassword.getText().toString();
            String cellphone = textCellphone.getText().toString();
            String direction = textDirecction.getText().toString();
            String dni = texDNI.getText().toString();

            if (password.equals(passwordVerified)) {
                // Si las contraseñas coinciden, llamar al método para registrar el usuario
                registerUser(name, email, password, cellphone, direction, dni);
            } else {
                // Si no coinciden, mostrar un mensaje de error
                Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void registerUser(String name, String email, String password, String cellphone, String direction, String dni) {
        // Crear cliente OkHttp
        OkHttpClient client = new OkHttpClient();

        // Crear el cuerpo de la solicitud JSON
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String jsonBody = "{\n" +
                "\"name\": \"" + name + "\",\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"" + password + "\",\n" +
                "\"cellphone\": \"" + cellphone + "\",\n" +
                "\"direcction\": \"" + direction + "\",\n" +
                "\"dni\": \"" + dni + "\"\n" +
                "}";

        RequestBody body = RequestBody.create(jsonBody, JSON);

        // Crear la solicitud POST
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        // Ejecutar la solicitud de manera asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Manejar el error
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Leer la respuesta como string
                    String responseBody = response.body().string();
                    try {
                        // Imprimir la respuesta completa para depuración
                        System.out.println("Respuesta completa: " + responseBody);

                        // Convertir el string a un objeto JSON
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Verificar si "token" existe en la respuesta
                        if (jsonResponse.has("token")) {
                            // Obtener el valor de "token"
                            String token = jsonResponse.getString("token");

                            // Mostrar el valor de "token" en consola
                            System.out.println("Token: " + token);

                            // Ejecutar en el hilo principal de la UI
                            getActivity().runOnUiThread(() -> {
                                saveAuthToken(token); // Guarda el token en SharedPreferences
                                Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                // Actualiza los fragmentos
                                Header headerUser = new Header();
                                updateFragments(headerUser.setTitle("Usuario"), new User());
                            });
                        } else {
                            // Si no existe la clave "token", mostrar un mensaje
                            System.out.println("No se encontró la clave 'token' en la respuesta.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Manejar el error en la respuesta
                    System.out.println("Error: " + response.code());
                }
            }
        });
    }
    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }
    private void updateFragments(Fragment headerFragment, Fragment contentFragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, headerFragment)
                .replace(R.id.fragment_content, contentFragment)
                .addToBackStack(null) // Para que pueda volver atrás con el botón de retroceso
                .commit();
    }
}