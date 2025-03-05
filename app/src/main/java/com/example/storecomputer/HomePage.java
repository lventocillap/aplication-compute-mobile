
package com.example.storecomputer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomePage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Header headerHome = new Header();
        if (savedInstanceState == null) {
            updateFragments(headerHome.setTitle("Novedades"), new FragmentPromotion());

            // Crear instancia de NavigatorBottom
            NavigatorBottom navigatorBottom = new NavigatorBottom();
            navigatorBottom.setInstance(this); // Pasar MainActivity al fragmento

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottom_nav_fragment, navigatorBottom)
                    .commit();
        }
    }
    public void updateHeader(Fragment headerFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, headerFragment)
                .commit();
    }
    public void updateFragments(Fragment headerFragment, Fragment contentFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, headerFragment)
                .replace(R.id.fragment_content, contentFragment)
                .commit();
    }
}