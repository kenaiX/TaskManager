package com.meizu.taskmanager;

import com.badlogic.gdx.graphics.Texture;

public class TaskItem {
    private int id;
    private Texture screenShot;
    private Texture icon;

    public int getId() {
        return id;
    }

    public TaskItem setId(int id) {
        this.id = id;
        return this;
    }

    public Texture getScreenShot() {
        return screenShot;
    }

    public TaskItem setScreenShot(Texture screenShot) {
        this.screenShot = screenShot;
        return this;
    }

    public Texture getIcon() {
        return icon;
    }

    public TaskItem setIcon(Texture icon) {
        this.icon = icon;
        return this;
    }
}
