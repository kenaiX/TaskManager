package com.meizu.taskmanager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.meizu.taskmanager.policy.BackgroundService;
import com.meizu.taskmanager.policy.ManagerService;
import com.meizu.taskmanager.ui.HorizontalGroup;


public class TaskManager implements ApplicationListener {
    private PagerStage mStage;
    private SpriteBatch mBatch;

    @Override
    public void create() {
        mBatch = new SpriteBatch();
        mStage = new PagerStage();

        Gdx.graphics.setContinuousRendering(true);

        InputMultiplexer multiplexer = new InputMultiplexer(new GestureDetector(mStage.flickScrollListener), mStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void resize(int width, int height) {
        pause();
        resume();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        mBatch.begin();
        mBatch.draw(BackgroundService.getBackGround(), 0, 0);
        mBatch.end();
        mStage.act(Gdx.graphics.getDeltaTime());
        mStage.draw();
    }

    @Override
    public void pause() {
        mStage.clear();
    }

    @Override
    public void resume() {

        HorizontalGroup mGroup = new HorizontalGroup();
        mGroup.space(50f);
        mGroup.setBounds(0, 0, mStage.getWidth(), mStage.getHeight());
        final TaskItem[] taskItems = ManagerService.getTaskItem();
        for (int i = 0; i < taskItems.length; i++) {
            mGroup.addActor(new TaskActor(taskItems[i]));
        }
        mGroup.layout();
        mStage.addActor(mGroup);
        mStage.bindGroup(mGroup);
        mStage.updateRect();
        mStage.init();

    }

    @Override
    public void dispose() {
        mStage.dispose();
        mStage = null;
    }

}
