package com.example.storecomputer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContendCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContendCategory extends Fragment {

    private HomePage homePage;

    public ContendCategory() {
        // Required empty public constructor
    }
    public static ContendCategory newInstance() {
        return new ContendCategory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener referencia de la actividad
        if (getActivity() instanceof HomePage) {
            homePage = (HomePage) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        setupCategoryButton(view, R.id.btnMonitor, "monitores");
        setupCategoryButton(view, R.id.btnProces, "procesadores");
        setupCategoryButton(view, R.id.btnMother, "placa madre");
        setupCategoryButton(view, R.id.btnPower, "fuente de poder");
        setupCategoryButton(view, R.id.btnGraph, "tarjeta grafica");
        setupCategoryButton(view, R.id.btnRam, "memoria RAM");
        setupCategoryButton(view, R.id.btnAlmac, "almacenamiento");
        setupCategoryButton(view, R.id.btnDisp, "refrigeración");
        setupCategoryButton(view, R.id.btnCase, "case");
        setupCategoryButton(view, R.id.btnTecl, "teclado");
        setupCategoryButton(view, R.id.btnMouse, "mouse");
        setupCategoryButton(view, R.id.btnAudi, "audifono");
        return view;
    }
    private void setupCategoryButton(View view, int buttonId, String categoryName) {
        view.findViewById(buttonId).setOnClickListener(v -> {
            if (homePage != null) {
                homePage.updateFragments(new HeaderCategory(), FragmentProducts.newInstance(categoryName));
            }
        });
    }
}