package com.example.storecomputer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Header#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Header extends Fragment {

    private OnNavigationItemSelectedListener listener;
    private String title;
    private TextView tvTitle;

    public interface OnNavigationItemSelectedListener {
        void onNavigationItemSelected(int buttonId);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Header() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeaderCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static Header newInstance(String param1, String param2) {
        Header fragment = new Header();
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

        View view =  inflater.inflate(R.layout.fragment_header, container, false);
        tvTitle = view.findViewById(R.id.txtTitle);
        tvTitle.setText(title);
        return view;
    }
    public Header setTitle(String title) {
        this.title = title;
        return this; // Permite encadenar métodos
    }
}