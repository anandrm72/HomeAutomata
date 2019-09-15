package com.example.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
