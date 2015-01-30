package com.meizu.taskmanager.policy;

import com.badlogic.gdx.graphics.Texture;
import com.meizu.taskmanager.TaskItem;

public class ManagerService {
    static TaskItem[] taskItems;

    public static void kill(){

    }

    public static TaskItem[] getTaskItem() {
        if (taskItems == null) {
            taskItems = new TaskItem[18];
            Texture texture1 = new Texture("music.jpg");
            Texture texture2 = new Texture("vedio.jpg");
            Texture texture3 = new Texture("weather.jpg");

            for (int i = 0; i < taskItems.length; i++) {
                double random = Math.random();
                if (random < 0.3) {
                    taskItems[i] = new TaskItem().setScreenShot(texture1);
                } else if (random < 0.6) {
                    taskItems[i] = new TaskItem().setScreenShot(texture2);
                } else {
                    taskItems[i] = new TaskItem().setScreenShot(texture3);
                }
            }
        }
        return taskItems;
    }
}
