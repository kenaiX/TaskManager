package com.meizu.taskmanager.policy;

import com.badlogic.gdx.graphics.Texture;

public class BackgroundService {
    private static Texture background;

    public synchronized static final Texture getBackGround() {
        if (background == null) background = new Texture("launcher.png");
        return background;
    }
}