package com.meizu.taskmanager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TaskManager implements ApplicationListener {
    private TaskStage stage;
    private TaskActor[] taskActor;

    @Override
    public void create() {
        stage = new TaskStage();
        taskActor = new TaskActor[3];
        taskActor[0] = new TaskActor(new Texture("music.jpg"));
        taskActor[1] = new TaskActor(new Texture("vedio.jpg"));
        taskActor[2] = new TaskActor(new Texture("weather.jpg"));

        for (int i = 0; i < taskActor.length; i++) {
            stage.addActor(taskActor[i]);
            if (i > 0) {
                taskActor[i].setX(taskActor[i].getWidth() * i);
            }
        }
        Gdx.input.setInputProcessor(new GestureDetector(stageGestureListener));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    GestureDetector.GestureListener stageGestureListener = new GestureDetector.GestureListener() {
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return true;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            Vector3 position = stage.getCamera().position;
            position.add(-deltaX, 0, 0);
            stage.getCamera().update();
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    };

}

class TaskStage extends Stage {
//    @Override
//    public float getWidth() {
//        float width=0f;
//        Array<Actor> actors = getActors();
//        for(Actor actor :actors){
//            width+=actor.getWidth();
//        }
//        return width;
//    }
}

class TaskActor extends Actor {
    Sprite sprite;

    public TaskActor(Texture texture) {
        sprite = new Sprite(texture);
        sprite.setScale(0.6f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    @Override
    public float getWidth() {
        return sprite.getWidth();
    }

    @Override
    public void setX(float x) {
        sprite.setPosition(x, 0);
    }
}