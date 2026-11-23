package com.dark.desire.dating.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.dark.desire.dating.R;
import com.dark.desire.dating.ui.fragments.ChatsFragment;
import com.dark.desire.dating.ui.fragments.ProfileFragment;
import com.dark.desire.dating.ui.fragments.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selected = null;
            if (itemId == R.id.nav_chat) {
                selected = new ChatsFragment();
            } else if (itemId == R.id.nav_users) {
                selected = new UsersFragment();
            } else if (itemId == R.id.nav_profile) {
                selected = new ProfileFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selected)
                        .commit();
                return true;
            }
            return false;
        });

        // Default open CHAT tab
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new ChatsFragment())
                    .commit();
        }
    }
}
