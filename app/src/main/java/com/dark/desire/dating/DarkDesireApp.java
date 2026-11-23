package com.dark.desire.dating;

import android.app.Application;
import com.dark.desire.dating.network.WebSocketManager;
import com.dark.desire.dating.utils.PrefsManager;

public class DarkDesireApp extends Application {

    private static DarkDesireApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PrefsManager.init(this);
        WebSocketManager.getInstance().init(this);
    }

    public static DarkDesireApp getInstance() {
        return instance;
    }
}
