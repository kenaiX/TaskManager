package com.meizu.taskmanager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TaskManager implements ApplicationListener {
    private Stage stage;
    private FirstActor firstActor;
    @Override
    public void create() {
        stage = new Stage();
        firstActor = new FirstActor();
        stage.addActor(firstActor);
        firstActor.setX(20);
        firstActor.setY(20);
        Gdx.input.setInputProcessor(stage);
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
}

class FirstActor extends Actor {
    Sprite sprite;

    public FirstActor() {
        final Texture texture = new Texture(Gdx.files.internal("music.jpg"));
        sprite = new Sprite(texture);
        sprite.setPosition(10, 10);
        sprite.setRotation(15);
        sprite.setScale(0.6f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

}