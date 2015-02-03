package com.meizu.taskmanager.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.meizu.taskmanager.TaskManager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height=800;
        config.width=450;
        config.disableAudio = true;
		new LwjglApplication(new TaskManager(), config);
	}
}
