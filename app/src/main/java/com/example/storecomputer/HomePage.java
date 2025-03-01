
package com.example.storecomputer;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (savedInstanceState == null) {
            updateFragments(new HeaderHome(), new FragmentPromotion());

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