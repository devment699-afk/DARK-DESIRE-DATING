package com.dark.desire.dating.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.WebSocketManager;
import com.dark.desire.dating.ui.fragments.ChatsFragment;
import com.dark.desire.dating.ui.fragments.DiscoverFragment;
import com.dark.desire.dating.ui.fragments.MatchFragment;
import com.dark.desire.dating.ui.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebSocketManager.getInstance().connect();

        bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selected = null;
            if (itemId == R.id.nav_match) {
                selected = new MatchFragment();
            } else if (itemId == R.id.nav_discover) {
                selected = new DiscoverFragment();
            } else if (itemId == R.id.nav_chats) {
                selected = new ChatsFragment();
            } else if (itemId == R.id.nav_profile) {
                selected = new SettingsFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selected)
                        .commit();
                return true;
            }
            return false;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new MatchFragment())
                    .commit();
        }
    }
}
