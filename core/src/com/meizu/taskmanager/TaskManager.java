package com.meizu.taskmanager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.meizu.taskmanager.policy.ManagerService;
import com.meizu.taskmanager.ui.HorizontalGroup;


public class TaskManager implements ApplicationListener {
    private PagerStage stage;
    Texture background;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new PagerStage();

        Gdx.graphics.setContinuousRendering(true);

        InputMultiplexer multiplexer = new InputMultiplexer(new GestureDetector(stage.flickScrollListener), stage);
        Gdx.input.setInputProcessor(multiplexer);
        background = new Texture("launcher.png");
    }

    @Override
    public void resize(int width, int height) {
        pause();
        resume();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void pause() {
        stage.clear();
    }

    @Override
    public void resume() {

        HorizontalGroup mGroup = new HorizontalGroup();
        mGroup.space(50f);
        mGroup.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        final TaskItem[] taskItems = ManagerService.getTaskItem();
        for (int i = 0; i < taskItems.length; i++) {
            mGroup.addActor(new TaskActor(taskItems[i]));
        }
        mGroup.layout();
        stage.addActor(mGroup);
        stage.bindGroup(mGroup);
        stage.updateRect();
        stage.init();

    }

    @Override
    public void dispose() {
        stage.dispose();
        stage = null;
    }

    public class TaskActor extends Actor {
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
            float speed;

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
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                speed = 0;
                if (getOriginY() == 0) {
                    setOriginY(getY());
                }
                super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if ((Math.abs(speed) <= 150 && speed != 0) || (speed == 0 && getY() != getOriginY())) {
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(getX(), getOriginY());
                    moveToAction.setDuration(0.5f);
                    addAction(moveToAction);
                }
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                speed = velocityY;

                if (Math.abs(speed) > 150) {
                    addAction(new RemoveTaskAction());
                } else {
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(getX(), getOriginY());
                    moveToAction.setDuration(0.3f);
                    addAction(moveToAction);
                }
                super.fling(event, velocityX, velocityY, button);
            }
        };


        public class RemoveTaskAction extends Action {

            public boolean act(float delta) {
                target.remove();
                return true;
            }
        }
    }
}
