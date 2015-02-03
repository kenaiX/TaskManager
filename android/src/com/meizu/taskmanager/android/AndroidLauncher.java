package com.meizu.taskmanager.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.meizu.taskmanager.TaskManager;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.disableAudio = true;
        config.useAccelerometer = false;
        config.useGLSurfaceView20API18 = true;
        initialize(new TaskManager(), config);

    }
}
