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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //FrameLayout bottomNavigationView = findViewById(R.id.bottom_nav_fragment);
        if (savedInstanceState == null) {
            updateFragments(new HeaderHome(), new FragmentPromotion());
        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.bottom_nav_fragment, new NavigatorBottom())
//                .commit();
    }
    private void updateFragments(Fragment headerFragment, Fragment contentFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, headerFragment)
                .replace(R.id.fragment_content, contentFragment)
                .commit();
    }
}