package com.meizu.taskmanager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.meizu.taskmanager.policy.ManagerService;

public class TaskManager implements ApplicationListener {
    private Stage stage;

    @Override
    public void create() {
        stage = new Stage();
        Gdx.graphics.setContinuousRendering(true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        pause();
        resume();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void pause() {
        stage.clear();
    }

    @Override
    public void resume() {
        final TaskItem[] taskItems = ManagerService.getTaskItem();
        final TaskActor[] taskActor = new TaskActor[taskItems.length];
        for (int i = 0; i < taskActor.length; i++) {
            taskActor[i] = new TaskActor(taskItems[i]);
        }
        HorizontalGroup verticalGroup = new HorizontalGroup();
        verticalGroup.space(50);
        for (int i = 0; i < taskActor.length; i++) {
            verticalGroup.addActor(taskActor[i]);
        }
        ScrollPane mScrollPane = new ScrollPane(verticalGroup);
        mScrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mScrollPane.setY((Gdx.graphics.getHeight() - mScrollPane.getHeight()) / 2);
        mScrollPane.setForceScroll(true, false);
        stage.addActor(mScrollPane);


    }

    @Override
    public void dispose() {
        stage.dispose();
        stage = null;
    }

    class TaskActor extends Actor {
        Sprite mSprite;

        TaskActor(TaskItem item) {
            addCaptureListener(actorGestureListener);

            final float zoom = Gdx.graphics.getWidth() * 0.6f / item.getScreenShot().getWidth();

            mSprite = new Sprite(item.getScreenShot());
            mSprite.setSize(mSprite.getWidth() * zoom, mSprite.getHeight() * zoom);
            setWidth(mSprite.getWidth());
            setHeight(mSprite.getHeight());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            mSprite.setPosition(getX(), getY());
            mSprite.draw(batch);
        }

        ActorGestureListener actorGestureListener = new ActorGestureListener() {
            boolean catchFocus;

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                Stage stage = getStage();
                if (!catchFocus && Math.abs(deltaX) < Math.abs(deltaY)) {
                    catchFocus = true;
                    if (stage != null)
                        stage.cancelTouchFocusExcept(actorGestureListener, TaskActor.this);
                }
                if (catchFocus) {
                    MoveByAction moveByAction = new MoveByAction();
                    moveByAction.setAmountY(deltaY);
                    addAction(moveByAction);
                }
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                addAction(new RemoveActorAction());
                super.fling(event, velocityX, velocityY, button);
            }
        };

    }
}
