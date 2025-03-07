package com.example.storecomputer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.storecomputer.Api.VerificationUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigatorBottom#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigatorBottom extends Fragment {

    private HomePage homePage; // Referencia a MainActivity

    public void setInstance(HomePage homePage) {
        this.homePage = homePage;
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FrameLayout header;
    private BottomNavigationView bottomNavigationView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NavigatorBottom() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavigatorBottom.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigatorBottom newInstance(String param1, String param2) {
        NavigatorBottom fragment = new NavigatorBottom();
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
        View view = inflater.inflate(R.layout.fragment_navigator_bottom, container, false);

        Header headerCategory = new Header();
        Header headerHome = new Header();
        Header headerLogin = new Header();
        Header headerError = new Header();
        Header headerShop = new Header();


        if (homePage != null) {
            // Configurar botones para cambiar el header
            view.findViewById(R.id.btnHome).setOnClickListener(v -> homePage.updateFragments(headerHome.setTitle("Novedades"), new FragmentPromotion()));
            view.findViewById(R.id.btnCategories).setOnClickListener(v -> homePage.updateFragments(headerCategory.setTitle("Categorias"), new ContendCategory()));
            view.findViewById(R.id.btnShoping).setOnClickListener(v -> {
                Context contextShop = v.getContext();
                verificationLogin(contextShop, headerShop, new FragmentListWish(), "Compras");
            });
            view.findViewById(R.id.btnProfile).setOnClickListener(v -> {
                System.out.println("Presionó el botón de perfil");
                Context context = v.getContext();
                verificationLogin(context, headerLogin, new User(), "Usuario");

            });

        }
        return view;
    }
    public void verificationLogin(Context context, Header header, Fragment fragmentContend, String headerName)
    {

        String token = VerificationUser.getAuthToken(context);
        System.out.println("Token obtenido: " + token); // 🔍 Verifica el token en consola

        if (token != null && !token.isEmpty()) { // ✅ Se verifica correctamente el token
            VerificationUser verificationUser = new VerificationUser();
            verificationUser.verifyUser(token, isSuccess -> {
                if (isSuccess) {
                    System.out.println("Token válido, mostrando User");
                    homePage.updateFragments(header.setTitle(headerName), fragmentContend);
                } else {
                    System.out.println("Token inválido, redirigiendo a Login...");
                    homePage.updateFragments(header.setTitle("Login"), new Login());
                }
            });
        } else {
            System.out.println("No hay token, redirigiendo a Login...");
            homePage.updateFragments(header.setTitle("Login"), new Login());
        }
    }
}